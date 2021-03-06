/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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
import com.j256.ormlite.table.TableUtils
import com.zaxxer.hikari.HikariDataSource
import eu.mikroskeem.benjiauth.LoginManager
import eu.mikroskeem.benjiauth.asPlayer
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.createDatabaseConfig
import eu.mikroskeem.benjiauth.currentUnixTimestamp
import eu.mikroskeem.benjiauth.database.migrations.Migration
import eu.mikroskeem.benjiauth.database.migrations.Users_0to1
import eu.mikroskeem.benjiauth.database.migrations.Users_1to2
import eu.mikroskeem.benjiauth.database.models.DatabaseMetadata
import eu.mikroskeem.benjiauth.database.models.User
import eu.mikroskeem.benjiauth.events.PlayerLoginEvent
import eu.mikroskeem.benjiauth.events.PlayerLogoutEvent
import eu.mikroskeem.benjiauth.events.PlayerRegisterEvent
import eu.mikroskeem.benjiauth.events.PlayerUnregisterEvent
import eu.mikroskeem.benjiauth.ipAddress
import eu.mikroskeem.benjiauth.isReady
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.pluginManager
import eu.mikroskeem.benjiauth.toIPString
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.security.SecureRandom
import java.util.Collections
import java.util.LinkedList
import java.util.Locale
import java.util.WeakHashMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.math.max

/**
 * @author Mark Vainomaa
 */
class UserManager: LoginManager {
    private val hikari = HikariDataSource(config.database.asHikariConfig)
    private val dsWrapper: DataSourceConnectionSource
    private val metaDao: Dao<DatabaseMetadata, String>
    private val usersDao: Dao<User, String>
    private val random = SecureRandom()
    private val forcefullyLoggedIn: MutableSet<ProxiedPlayer> = Collections.newSetFromMap(WeakHashMap())
    private val readyUsers: MutableSet<ProxiedPlayer> = Collections.newSetFromMap(WeakHashMap())
    private val forcefullyLoggedInLock = ReentrantReadWriteLock()
    private val readyUsersLock = ReentrantReadWriteLock()

    init {
        try {
            Class.forName(config.database.driverClass)
        } catch (e: Exception) {}

        dsWrapper = DataSourceConnectionSource(hikari, hikari.jdbcUrl)

        // Set up metadata table
        val metaDaoConfig = createDatabaseConfig<DatabaseMetadata>(dsWrapper, tableName = config.database.metaTableName)
        metaDao = DaoManager.createDao(dsWrapper, metaDaoConfig)
        val metaTableExists = metaDao.isTableExists
        if (metaTableExists) {
            // Get current database version
            val versionData = metaDao.queryForId(DATABASE_VERSION)
            currentDatabaseVersion = versionData?.value?.toIntOrNull() ?: 0
        } else {
            currentDatabaseVersion = latestDatabaseVersion // Assume database version to be 0 if metadata table was not present
            TableUtils.createTableIfNotExists(dsWrapper, metaDaoConfig)
            metaDao.create(DatabaseMetadata(DATABASE_VERSION, "int", "$currentDatabaseVersion"))
        }

        // Set up users table
        val usersDaoConfig = createDatabaseConfig<User>(dsWrapper, tableName = config.database.tableName)
        usersDao = DaoManager.createDao(dsWrapper, usersDaoConfig)
        if (!usersDao.isTableExists) {
            TableUtils.createTableIfNotExists(dsWrapper, usersDaoConfig)
            currentDatabaseVersion = latestDatabaseVersion // Users table was not present either, so we're on latest version
        } else if (!metaTableExists) {
            // Best effort
            plugin.pluginLogger.info("Plugin database metadata table was missing, using best effort to do migration...")
            allowMigrationFailures = true
        }

        // Run migrations
        plugin.pluginLogger.info("Current database version is $currentDatabaseVersion, latest is $latestDatabaseVersion")
        if (currentDatabaseVersion < latestDatabaseVersion) {
            plugin.pluginLogger.info("Running database migrations...")
            runMigrations(metaDao, usersDao)
            plugin.pluginLogger.info("BenjiAuth database successfully updated to version $latestDatabaseVersion")
        } else if (currentDatabaseVersion > latestDatabaseVersion) {
            plugin.pluginLogger.warning("Database version is newer than this plugin supports! You might run into issues")
        }
    }

    override fun isRegistered(username: String): Boolean = findUserSafe(username) != null

    override fun isRegistered(player: ProxiedPlayer): Boolean = isRegistered(player.name)

    // TODO: ugly :(
    fun usernameCaseCorrect(player: ProxiedPlayer): Boolean {
        val user = findUser(player.name)
        return user.originalUsername == user.username
    }

    fun usernameCaseCorrect(playerName: String): Boolean {
        val user = findUserSafe(playerName) ?: return true // Not registered
        return user.originalUsername == playerName
    }

    override fun registerUser(player: ProxiedPlayer, password: String) {
        findUserSafe(player.name)?.run { throw IllegalStateException("Player ${player.name} is already registered!") }
        val currentTime = currentUnixTimestamp

        usersDao.create(User(
                player.name.toLowerCase(Locale.ROOT), player.name, hashPassword(password),
                currentTime,
                player.address.toIPString(),
                false,
                null,
                null,
                null,
                false
        ))

        if (player.isReady) {
            pluginManager.callEvent(PlayerRegisterEvent(player))
        }

        if (config.registration.loginAfterRegister) {
            loginUser(player)
        }
    }

    override fun registerUser(username: String, password: String) {
        // First check if registrable user is online, use right method if that's in case
        username.asPlayer()?.let { player -> registerUser(player, password); return }

        findUserSafe(username)?.run { throw IllegalStateException("Player $username is already registered!") }
        val currentTime = currentUnixTimestamp

        // Register user
        usersDao.create(User(
                username.toLowerCase(Locale.ROOT), username, hashPassword(password),
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
        usersDao.delete(findUser(player.name))

        // If player is marked ready, send out unregister event
        if (player.isReady) {
            pluginManager.callEvent(PlayerUnregisterEvent(player))
        }
    }

    override fun unregisterUser(username: String) {
        username.asPlayer()?.let { player -> unregisterUser(player); return }

        usersDao.delete(findUser(username))
    }

    override fun isEligibleForSessionLogin(player: ProxiedPlayer): Boolean {
        if (config.authentication.sessionTimeout == 0L)
            return false

        val user = findUser(player.name)

        // User logged itself out, not egilible for session login in that case.
        if (user.forceKillSession)
            return false

        val timeout = TimeUnit.MINUTES.toSeconds(config.authentication.sessionTimeout)

        val lastSeen = user.lastSeen?.run { currentUnixTimestamp - this }
        val lastLogin = user.lastLogin?.run { currentUnixTimestamp - this }

        // Definitely not eligible
        if (lastSeen == null || lastLogin == null) {
            return false
        }

        // User is only eligible for session login when time since last seen
        // is less than timeout and if IP addresses match.
        if (lastSeen < timeout && player.ipAddress == user.lastIPAddress) {
            return true
        }

        return false
    }

    override fun isLoggedIn(player: ProxiedPlayer): Boolean = findUser(player.name).loggedIn

    override fun isForcefullyLoggedIn(player: ProxiedPlayer): Boolean = forcefullyLoggedInLock.read { forcefullyLoggedIn.contains(player) }

    override fun loginUser(player: ProxiedPlayer, force: Boolean) {
        if (isLoggedIn(player))
            return

        val passwordResetCodeCleared: Boolean
        findUser(player.name).apply {
            val timestamp = currentUnixTimestamp

            loggedIn = true
            lastIPAddress = player.address.toIPString()
            lastLogin = timestamp
            lastSeen = timestamp

            // Update registered IP address if empty
            if (registeredIPAddress.isEmpty()) {
                registeredIPAddress = lastIPAddress!!
            }

            // Clear password reset code if not forcefully logged in
            if (!force && passwordResetCode != null) {
                passwordResetCode = null
                passwordResetSentTimestamp = null
                passwordResetCodeCleared = true
            } else {
                passwordResetCodeCleared = false
            }

            // Reset kill session flag
            forceKillSession = false

            usersDao.update(this)
        }
        if (force) forcefullyLoggedInLock.write { forcefullyLoggedIn.add(player) }

        if (player.isReady) {
            pluginManager.callEvent(PlayerLoginEvent(player, force, passwordResetCodeCleared))
        }
    }

    override fun logoutUser(player: ProxiedPlayer, clearSession: Boolean, keepReady: Boolean) {
        findUser(player.name).apply {
            if (loggedIn) {
                lastSeen = currentUnixTimestamp
                loggedIn = false
            }
            if (clearSession && !forceKillSession) {
                forceKillSession = clearSession
            }
            usersDao.update(this)
        }

        if (!keepReady) {
            readyUsersLock.write {
                readyUsers.removeIf { it == player }
            }
        }

        if (player.isReady) {
            pluginManager.callEvent(PlayerLogoutEvent(player))
        }
    }

    override fun checkPassword(player: ProxiedPlayer, password: String): Boolean = findUser(player.name).let {
        checkPassword(password, it.password)
    }

    override fun changePassword(player: ProxiedPlayer, newPassword: String) {
        usersDao.update(findUser(player.name).apply { password = hashPassword(newPassword) })
    }

    override fun getEmail(player: ProxiedPlayer): String? = findUser(player.name).email

    override fun setEmail(player: ProxiedPlayer, email: String?, verificationCode: String?) {
        findUser(player.name).apply {
            this.email = email
            this.emailVerification = verificationCode
            this.emailVerificationSentTimestamp = if(verificationCode == null) null else currentUnixTimestamp
            usersDao.update(this)
        }
    }

    override fun verifyEmail(player: ProxiedPlayer, verificationCode: String?): LoginManager.EmailVerifyResult {
        val user = findUser(player.name)

        if (user.email == null)
            return LoginManager.EmailVerifyResult.FAILED

        if (user.emailVerification == null && user.emailVerificationSentTimestamp == null)
            return LoginManager.EmailVerifyResult.ALREADY_VERIFIED

        val expired = (currentUnixTimestamp - (user.emailVerificationSentTimestamp ?: 0)) >= config.email.verificationTimeout
        if (verificationCode != null) {
            // Verification code is present, but token is expired :(
            if (expired)
                return LoginManager.EmailVerifyResult.EXPIRED

            if (verificationCode != user.emailVerification)
                return LoginManager.EmailVerifyResult.FAILED
        }

        usersDao.update(user.apply {
            emailVerification = null
            emailVerificationSentTimestamp = null
        })

        return LoginManager.EmailVerifyResult.SUCCESS
    }

    override fun isEmailVerified(player: ProxiedPlayer): Boolean {
        val user = findUser(player.name)
        return user.email != null && user.emailVerification == null && user.emailVerificationSentTimestamp == null
    }

    override fun getPasswordResetCode(player: ProxiedPlayer): String? = findUser(player.name).passwordResetCode

    override fun setPasswordResetCode(player: ProxiedPlayer, code: String?) {
        findUser(player.name).apply {
            passwordResetCode = code
            passwordResetSentTimestamp = if(code == null) null else currentUnixTimestamp
            usersDao.update(this)
        }
    }

    override fun verifyPasswordReset(player: ProxiedPlayer, code: String?, newPassword: String): LoginManager.PasswordResetVerifyResult {
        val user = findUser(player.name)

        if (user.passwordResetCode == null)
            return LoginManager.PasswordResetVerifyResult.FAILED

        val expired = (currentUnixTimestamp - (user.passwordResetSentTimestamp ?: 0)) >= config.email.passwordResetCodeTimeout
        if (code != null) {
            if (expired)
                return LoginManager.PasswordResetVerifyResult.EXPIRED

            if (code != user.passwordResetCode)
                return LoginManager.PasswordResetVerifyResult.FAILED
        }

        usersDao.update(user.apply {
            passwordResetCode = null
            passwordResetSentTimestamp = null
            password = hashPassword(newPassword)
        })
        return LoginManager.PasswordResetVerifyResult.SUCCESS
    }

    override fun isUserReady(player: ProxiedPlayer): Boolean = readyUsersLock.read { readyUsers.contains(player) }

    override fun markUserReady(player: ProxiedPlayer) { readyUsersLock.write { readyUsers.add(player) } }

    override fun getRegistrations(ipAddress: String): Long = usersDao.queryBuilder().where()
            .eq(User.REGISTERED_IP_ADDRESS_FIELD, ipAddress)
            .countOf()

    override fun getEmailUsages(emailAddress: String): Long = usersDao.queryBuilder().where()
            .eq(User.EMAIL_FIELD, emailAddress)
            .and()
            .isNull(User.EMAIL_VERIFICATION_FIELD)
            .countOf()

    // Shuts user manager down
    fun shutdown() = hikari.close()

    private fun findUser(username: String): User = usersDao.queryForId(username.toLowerCase(Locale.ROOT)) ?: throw IllegalStateException("Player $username is not registered")
    private fun findUserSafe(username: String): User? = usersDao.queryForId(username.toLowerCase(Locale.ROOT))

    private fun hashPassword(password: String): String {
        return BCrypt.with(random).hashToString(config.registration.bcryptRounds, password.toCharArray())
    }

    private fun checkPassword(password: String, correctPassword: String): Boolean {
        val result = BCrypt.verifyer().verify(password.toCharArray(), correctPassword.toCharArray())
        return result.verified
    }

    companion object {
        private val migrations = LinkedList<Migration>()
        private var currentDatabaseVersion: Int = 0
        private var latestDatabaseVersion = 0
        private var allowMigrationFailures = false

        private fun registerMigration(migration: Migration) {
            migrations.add(migration)
            latestDatabaseVersion = max(migration.newVersion, latestDatabaseVersion)
        }

        fun runMigrations(metaDao: Dao<DatabaseMetadata, String>, dao: Dao<*, *>) {
            migrations.forEach { migration ->
                migration.update(currentDatabaseVersion, allowMigrationFailures, dao)
                currentDatabaseVersion = migration.newVersion
            }
            metaDao.update(DatabaseMetadata(DATABASE_VERSION, "int", "$latestDatabaseVersion"))
        }

        init {
            registerMigration(Users_0to1())
            registerMigration(Users_1to2())
        }
    }
}