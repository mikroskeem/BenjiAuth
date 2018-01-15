/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.database

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager
import com.j256.ormlite.jdbc.DataSourceConnectionSource
import com.j256.ormlite.table.TableUtils
import com.zaxxer.hikari.HikariDataSource
import eu.mikroskeem.benjiauth.LoginManager
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.currentUnixTimestamp
import eu.mikroskeem.benjiauth.database.models.User
import net.md_5.bungee.api.connection.ProxiedPlayer
import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom
import java.util.WeakHashMap

/**
 * @author Mark Vainomaa
 */
class UserManager: LoginManager {
    private val hikari = HikariDataSource(config.database.asHikariConfig)
    private val dsWrapper: DataSourceConnectionSource
    private val dao: Dao<User, String>
    private val random = SecureRandom()
    private val authenticatedUsers = WeakHashMap<ProxiedPlayer, Long>()

    init {
        try {
            Class.forName(config.database.driverClass)
        } catch (e: Exception) {}

        dsWrapper = DataSourceConnectionSource(hikari, hikari.jdbcUrl)
        dao = DaoManager.createDao(dsWrapper, User::class.java)
        TableUtils.createTableIfNotExists(dsWrapper, User::class.java)
    }

    // Tries to find user from database
    fun findUser(username: String): User? = dao.queryForId(username)

    // Logs user in
    fun loginUser(player: ProxiedPlayer, password: String): Boolean {
        val user = findUser(player.name)!!
        return if(BCrypt.checkpw(password, user.password)) {
            setLoggedIn(player)
            true
        } else {
            false
        }
    }

    // Logs user out
    fun logoutUser(player: ProxiedPlayer) { authenticatedUsers.remove(player) }

    // Registers users
    fun registerUser(player: ProxiedPlayer, password: String): User {
        val salt = genSalt()
        val hashedPassword = BCrypt.hashpw(password, salt)
        return User(player.name, hashedPassword, currentUnixTimestamp).also {
            dao.create(it)

            if(config.authentication.loginAfterRegister)
                setLoggedIn(player)
        }
    }

    // Unregisters user
    fun unregisterUser(username: String): Unit {
        dao.deleteById(username)
        authenticatedUsers.keys.find { it.name == username }?.let {
            authenticatedUsers.remove(it)
        }
    }

    fun isLoggedIn(player: ProxiedPlayer): Long? = authenticatedUsers[player]
    fun setLoggedIn(player: ProxiedPlayer): Long = currentUnixTimestamp.apply { authenticatedUsers.put(player, this) }

    // Shuts user manager down
    fun shutdown() {
        hikari.close()
    }

    private fun genSalt(): String = BCrypt.gensalt(config.authentication.bcryptRounds, random)
}