/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.database

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.DataSourceConnectionSource
import com.j256.ormlite.table.DatabaseTableConfig
import com.j256.ormlite.table.DatabaseTableConfigLoader
import com.j256.ormlite.table.TableUtils
import com.zaxxer.hikari.HikariDataSource
import eu.mikroskeem.benjiauth.LoginManager
import eu.mikroskeem.benjiauth.asPlayer
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.currentUnixTimestamp
import eu.mikroskeem.benjiauth.database.models.User
import eu.mikroskeem.benjiauth.events.PlayerLoginEvent
import eu.mikroskeem.benjiauth.events.PlayerLogoutEvent
import eu.mikroskeem.benjiauth.events.PlayerRegisterEvent
import eu.mikroskeem.benjiauth.events.PlayerUnregisterEvent
import eu.mikroskeem.benjiauth.isReady
import eu.mikroskeem.benjiauth.pluginManager
import eu.mikroskeem.benjiauth.toIPString
import net.md_5.bungee.api.connection.ProxiedPlayer
import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.TimeUnit

/**
 * @author Mark Vainomaa
 */
class UserManager: LoginManager {
    private val hikari = HikariDataSource(config.database.asHikariConfig)
    private val dsWrapper: DataSourceConnectionSource
    private val dao: Dao<User, String>
    private val random = SecureRandom()
    private val forcefullyLoggedIn: MutableSet<ProxiedPlayer> = Collections.newSetFromMap(WeakHashMap())
    private val readyUsers: MutableSet<ProxiedPlayer> = Collections.newSetFromMap(WeakHashMap())

    init {
        try {
            Class.forName(config.database.driverClass)
        } catch (e: Exception) {}

        dsWrapper = DataSourceConnectionSource(hikari, hikari.jdbcUrl)
        val daoConfig = DatabaseTableConfig.fromClass(dsWrapper, User::class.java).also {
            config.database.tableName.takeUnless { it.isEmpty() }?.run {
                it.tableName = this
            }
        }
        dao = DaoManager.createDao(dsWrapper, daoConfig)
        TableUtils.createTableIfNotExists(dsWrapper, User::class.java)
    }

    override fun isRegistered(username: String): Boolean = findUserSafe(username) != null

    override fun isRegistered(player: ProxiedPlayer): Boolean = isRegistered(player.name)

    override fun registerUser(player: ProxiedPlayer, password: String) {
        findUserSafe(player.name)?.run { throw IllegalStateException("Player ${player.name} is already registered!") }
        val currentTime = currentUnixTimestamp

        dao.create(User(
                player.name, BCrypt.hashpw(password, genSalt()),
                currentTime,
                player.address.toIPString(),
                false,
                null,
                null
        ))

        if(player.isReady) {
            pluginManager.callEvent(PlayerRegisterEvent(player))
        }

        if(config.registration.loginAfterRegister) {
            loginUser(player)
        }
    }

    override fun registerUser(username: String, password: String) {
        // First check if registrable user is online, use right method if that's in case
        username.asPlayer()?.let { player -> registerUser(player, password); return }

        findUserSafe(username)?.run { throw IllegalStateException("Player $username is already registered!") }
        val currentTime = currentUnixTimestamp

        // Register user
        dao.create(User(
                username, BCrypt.hashpw(password, genSalt()),
                currentTime,
                "", // See javadoc for User#getRegisteredIPAddress()
                false,
                null,
                null
        ))
    }

    override fun unregisterUser(player: ProxiedPlayer) {
        dao.delete(findUser(player.name))

        // If player is marked ready, send out unregister event
        if(player.isReady) {
            pluginManager.callEvent(PlayerUnregisterEvent(player))
        }
    }

    override fun unregisterUser(username: String) {
        username.asPlayer()?.let { player -> unregisterUser(player); return }

        dao.delete(findUser(username))
    }

    override fun isEgilibleForSessionLogin(player: ProxiedPlayer): Boolean {
        return if(config.authentication.sessionTimeout == 0L)
            false
        else
            findUser(player.name).lastLogin ?: 0 > TimeUnit.MINUTES.toSeconds(config.authentication.sessionTimeout)
    }

    override fun isLoggedIn(player: ProxiedPlayer): Boolean = findUser(player.name).loggedIn

    override fun isForcefullyLoggedIn(player: ProxiedPlayer): Boolean = forcefullyLoggedIn.contains(player)

    override fun loginUser(player: ProxiedPlayer, force: Boolean) {
        if(isLoggedIn(player))
            return

        findUser(player.name).apply {
            loggedIn = true
            lastIPAddress = player.address.toIPString()
            lastLogin = currentUnixTimestamp

            // Update registered IP address if empty
            if(registeredIPAddress.isEmpty()) {
                registeredIPAddress = lastIPAddress!!
            }

            dao.update(this)
        }
        if(force) forcefullyLoggedIn.add(player)

        if(player.isReady) {
            pluginManager.callEvent(PlayerLoginEvent(player, force))
        }
    }

    override fun logoutUser(player: ProxiedPlayer, clearSession: Boolean, keepReady: Boolean) {
        findUser(player.name).apply {
            loggedIn = false
            if(clearSession) {
                lastLogin = null
                lastIPAddress = null
            }
            dao.update(this)
        }

        if(!keepReady) {
            readyUsers.removeIf { it == player }
        }

        if(player.isReady) {
            pluginManager.callEvent(PlayerLogoutEvent(player))
        }
    }

    override fun checkPassword(player: ProxiedPlayer, password: String): Boolean = findUser(player.name).let {
        BCrypt.checkpw(password, it.password)
    }

    override fun changePassword(player: ProxiedPlayer, newPassword: String) {
        dao.update(findUser(player.name).apply { password = BCrypt.hashpw(newPassword, genSalt()) })
    }

    override fun isUserReady(player: ProxiedPlayer): Boolean = readyUsers.contains(player)

    override fun markUserReady(player: ProxiedPlayer) { readyUsers.add(player) }

    override fun getRegistrations(ipAddress: String): Long = dao.queryBuilder().where()
            .eq(User.REGISTERED_IP_ADDRESS_FIELD, ipAddress)
            .countOf()

    // Shuts user manager down
    fun shutdown() = hikari.close()

    private fun findUser(username: String): User = dao.queryForId(username) ?: throw IllegalStateException("Player $username is not registered")
    private fun findUserSafe(username: String): User? = dao.queryForId(username)
    private fun genSalt(): String = BCrypt.gensalt(config.registration.bcryptRounds, random)
}