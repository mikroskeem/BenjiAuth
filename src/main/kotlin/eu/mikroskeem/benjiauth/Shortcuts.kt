/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.config.Benji
import eu.mikroskeem.benjiauth.config.BenjiMessages
import eu.mikroskeem.benjiauth.config.ConfigurationLoader
import eu.mikroskeem.benjiauth.tasks.Task
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.plugin.PluginManager
import net.md_5.bungee.api.scheduler.ScheduledTask
import java.time.Instant
import kotlin.reflect.KClass

/**
 * @author Mark Vainomaa
 */
val proxy: ProxyServer get() = ProxyServer.getInstance()
val pluginManager: PluginManager get() = proxy.pluginManager
val plugin: BenjiAuthPlugin get() = pluginManager.getPlugin("BenjiAuth") as BenjiAuthPlugin
val userManager: LoginManager get() = plugin.api.loginManager
val geoIpApi: GeoIPAPI get() = plugin.api.geoIPAPI

val config: Benji get() = plugin.config
val messages: BenjiMessages get() = plugin.messages

val currentUnixTimestamp: Long get() = Instant.now().epochSecond

inline fun <reified T: Any> BenjiAuthPlugin.initConfig(fileName: String): ConfigurationLoader<T> = ConfigurationLoader(pluginFolder.resolve(fileName), T::class.java)
fun <T: Listener> BenjiAuthPlugin.registerListener(listener: KClass<T>) = pluginManager.registerListener(this as Plugin, listener.java.getConstructor().newInstance())
fun <T: Command> BenjiAuthPlugin.registerCommand(command: KClass<T>) = pluginManager.registerCommand(this as Plugin, command.java.getConstructor().newInstance())
fun <T: Runnable> BenjiAuthPlugin.runAsync(task: KClass<T>): ScheduledTask = task.java.getConstructor().newInstance().let { proxy.scheduler.runAsync(this as Plugin, it) }
fun <T: Task> BenjiAuthPlugin.startTask(task: T): ScheduledTask = task.let { proxy.scheduler.schedule(this as Plugin, it, it.delay, it.period, it.timeUnit) }
fun <T: Task> BenjiAuthPlugin.startTask(task: KClass<T>, vararg args: Any): ScheduledTask = task.java.getConstructor(*args.map(Any::javaClass).toTypedArray()).newInstance(args).let {
    proxy.scheduler.schedule(this as Plugin, it, it.delay, it.period, it.timeUnit) }

fun String.color() = ChatColor.translateAlternateColorCodes('&', this)

fun Task.schedule() = plugin.startTask(this)
fun <T: Task> KClass<T>.schedule(vararg args: Any) = plugin.startTask(this, args)

fun CommandSender.authMessage(message: String): Unit { sendMessage(*message.processMessage(this as? ProxiedPlayer).takeIf { it.isNotEmpty() } ?: return) }

val ProxiedPlayer.isRegistered: Boolean get() = userManager.isRegistered(this)
val ProxiedPlayer.isLoggedIn: Boolean get() = isRegistered && userManager.isLoggedIn(this)
val ProxiedPlayer.isEgilibleForSession: Boolean get() = userManager.isEgilibleForSessionLogin(this)
val ProxiedPlayer.isForcefullyLoggedIn: Boolean get() = userManager.isForcefullyLoggedIn(this)

fun ProxiedPlayer.checkPassword(password: String) = userManager.checkPassword(this, password)
fun ProxiedPlayer.login(password: String): Boolean = if(checkPassword(password)) { userManager.loginUser(this); true } else false
fun ProxiedPlayer.loginWithoutPassword(forceful: Boolean = false) = userManager.loginUser(this, forceful)
fun ProxiedPlayer.register(password: String) = userManager.registerUser(this, password)
fun ProxiedPlayer.changePassword(newPassword: String) = userManager.changePassword(this, newPassword)
fun ProxiedPlayer.logout() = userManager.logoutUser(this, true)