/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.benjiauth.database

import at.favre.lib.crypto.bcrypt.BCrypt
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.DataSourceConnectionSource
import com.j256.ormlite.table.DatabaseTableConfig
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
import eu.mikroskeem.benjiauth.ipAddress
import eu.mikroskeem.benjiauth.isReady
import eu.mikroskeem.benjiauth.pluginManager
import eu.mikroskeem.benjiauth.toIPString
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.security.SecureRandom
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

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
        TableUtils.createTableIfNotExists(dsWrapper, daoConfig)
    }

    override fun isRegistered(username: String): Boolean = findUserSafe(username) != null

    override fun isRegistered(player: ProxiedPlayer): Boolean = isRegistered(player.name)

    override fun registerUser(player: ProxiedPlayer, password: String) {
        findUserSafe(player.name)?.run { throw IllegalStateException("Player ${player.name} is already registered!") }
        val currentTime = currentUnixTimestamp

        dao.create(User(
                player.name, hashPassword(password),
                currentTime,
                player.address.toIPString(),
                false,
                null,
                null,
                null,
                false
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
                username, hashPassword(password),
                currentTime,
                "", // See javadoc for User#getRegisteredIPAddress()
                false,
                null,
                null,
                null,
                false
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

    override fun isEligibleForSessionLogin(player: ProxiedPlayer): Boolean {
        if(config.authentication.sessionTimeout == 0L)
            return false

        val user = findUser(player.name)

        // User logged itself out, not egilible for session login in that case.
        if(user.forceKillSession)
            return false

        val timeout = TimeUnit.MINUTES.toSeconds(config.authentication.sessionTimeout)

        val lastSeen = user.lastSeen?.run { currentUnixTimestamp - this }
        val lastLogin = user.lastLogin?.run { currentUnixTimestamp - this }

        // Definitely not eligible
        if(lastSeen == null || lastLogin == null) {
            return false
        }

        // User is only eligible for session login when time since last seen
        // is less than timeout and if IP addresses match.
        if(lastSeen < timeout && player.ipAddress == user.lastIPAddress) {
            return true
        }

        return false
    }

    override fun isLoggedIn(player: ProxiedPlayer): Boolean = findUser(player.name).loggedIn

    override fun isForcefullyLoggedIn(player: ProxiedPlayer): Boolean = forcefullyLoggedIn.contains(player)

    override fun loginUser(player: ProxiedPlayer, force: Boolean) {
        if(isLoggedIn(player))
            return

        findUser(player.name).apply {
            val timestamp = currentUnixTimestamp

            loggedIn = true
            lastIPAddress = player.address.toIPString()
            lastLogin = timestamp
            lastSeen = timestamp

            // Update registered IP address if empty
            if(registeredIPAddress.isEmpty()) {
                registeredIPAddress = lastIPAddress!!
            }

            // Reset kill session flag
            forceKillSession = false

            dao.update(this)
        }
        if(force) forcefullyLoggedIn.add(player)

        if(player.isReady) {
            pluginManager.callEvent(PlayerLoginEvent(player, force))
        }
    }

    override fun logoutUser(player: ProxiedPlayer, clearSession: Boolean, keepReady: Boolean) {
        findUser(player.name).apply {
            if(loggedIn) {
                lastSeen = currentUnixTimestamp
                loggedIn = false
            }
            if(clearSession && !forceKillSession) {
                forceKillSession = clearSession
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
        checkPassword(password, it.password)
    }

    override fun changePassword(player: ProxiedPlayer, newPassword: String) {
        dao.update(findUser(player.name).apply { password = hashPassword(newPassword) })
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

    private fun hashPassword(password: String): String {
        return BCrypt.with(random).hashToString(config.registration.bcryptRounds, password.toCharArray())
    }

    private fun checkPassword(password: String, correctPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(password.toCharArray(), correctPassword.toCharArray())
        return result.verified
    }
}