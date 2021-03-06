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

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.config.Benji
import eu.mikroskeem.benjiauth.config.BenjiMessages
import eu.mikroskeem.benjiauth.config.ConfigurationLoader
import eu.mikroskeem.benjiauth.database.UserManager
import eu.mikroskeem.benjiauth.tasks.Task
import eu.mikroskeem.geoip.GeoIPAPI
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.plugin.PluginManager
import net.md_5.bungee.api.scheduler.ScheduledTask
import java.time.Instant

/**
 * @author Mark Vainomaa
 */
val proxy: ProxyServer get() = ProxyServer.getInstance()
val pluginManager: PluginManager get() = proxy.pluginManager
val plugin: BenjiAuthPlugin get() = pluginManager.getPlugin("BenjiAuth") as BenjiAuthPlugin
val userManager: LoginManager get() = plugin.api.loginManager
val geoIpApi: GeoIPAPI get() = plugin.geoIPAPI

val config: Benji get() = plugin.config
val messages: BenjiMessages get() = plugin.messages

val currentUnixTimestamp: Long get() = Instant.now().epochSecond

inline fun <reified T: Any> BenjiAuthPlugin.initConfig(fileName: String): ConfigurationLoader<T> = ConfigurationLoader(pluginFolder.resolve(fileName), T::class.java)
inline fun <reified T: Listener> BenjiAuthPlugin.registerListener() = pluginManager.registerListener(this as Plugin, T::class.java.getConstructor().newInstance())
inline fun <reified T: Command> BenjiAuthPlugin.registerCommand() = pluginManager.registerCommand(this as Plugin, T::class.java.getConstructor().newInstance())
fun <T: Task> BenjiAuthPlugin.startTask(task: T): ScheduledTask = task.let { proxy.scheduler.schedule(this as Plugin, it, it.delay, it.period, it.timeUnit) }

fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)

fun CommandSender.message(message: Iterable<String>, placeholders: Map<String, Any> = emptyMap()): Unit {
    message(message.joinToString(separator = "\n"), placeholders)
}
fun CommandSender.message(message: String, placeholders: Map<String, Any> = emptyMap()): Unit {
    sendMessage(*message.processMessage(this as? ProxiedPlayer, placeholders).takeIf { it.isNotEmpty() } ?: return)
}
fun ProxiedPlayer.kickWithMessage(message: String, placeholders: Map<String, Any> = emptyMap()): Unit {
    disconnect(*message.processMessage(this, placeholders).takeIf { it.isNotEmpty() } ?: return)
}

val ProxiedPlayer.isRegistered: Boolean get() = userManager.isRegistered(this)
// TODO: two methods below are evil
val ProxiedPlayer.isUsernameCaseCorrect: Boolean get() = (userManager as UserManager).usernameCaseCorrect(this)
val String.isUsernameCaseCorrect: Boolean get() = (userManager as UserManager).usernameCaseCorrect(this)
val ProxiedPlayer.isLoggedIn: Boolean get() = isRegistered && userManager.isLoggedIn(this)
val ProxiedPlayer.isEligibleForSession: Boolean get() = userManager.isEligibleForSessionLogin(this)
val ProxiedPlayer.isForcefullyLoggedIn: Boolean get() = userManager.isForcefullyLoggedIn(this)
val ProxiedPlayer.isReady: Boolean get() = userManager.isUserReady(this)
val ProxiedPlayer.registrationsForIP: Long get() = userManager.getRegistrations(this.ipAddress)

val ProxiedPlayer.verifiedEmailAddress: String? get() = userManager.getEmail(this)?.takeIf { userManager.isEmailVerified(this) }
val ProxiedPlayer.emailAddress: String? get() = userManager.getEmail(this)
val ProxiedPlayer.isEmailVerified: Boolean get() = userManager.isEmailVerified(this)

fun ProxiedPlayer.checkPassword(password: String) = userManager.checkPassword(this, password)
fun ProxiedPlayer.login(password: String): Boolean = if(checkPassword(password)) { userManager.loginUser(this); true } else false
fun ProxiedPlayer.loginWithoutPassword(forceful: Boolean = false) = userManager.loginUser(this, forceful)
fun ProxiedPlayer.register(password: String) = userManager.registerUser(this, password)
fun ProxiedPlayer.changePassword(newPassword: String) = userManager.changePassword(this, newPassword)
fun ProxiedPlayer.logout(clearSession: Boolean = true, keepReady: Boolean = true) = userManager.logoutUser(this, clearSession, keepReady)
fun ProxiedPlayer.markReady() = userManager.markUserReady(this)
fun ProxiedPlayer.hasTooManyRegistrations() = registrationsForIP >= config.registration.maxRegstrationsPerIP

fun String.asPlayer(): ProxiedPlayer? = proxy.players.find { it.name == this }

inline fun getAuthServer(failure: () -> Unit): ServerInfo {
    return config.servers.authServer.takeUnless { it.isEmpty() }?.run(::findServer) ?: run {
        failure()
        throw IllegalStateException("Invalid auth server '${config.servers.authServer}'!")
    }
}