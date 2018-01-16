/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.config.ConfigurationLoader
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.time.Instant
import kotlin.reflect.KClass

/**
 * @author Mark Vainomaa
 */
inline fun <reified T: Any> BenjiAuth.initConfig(fileName: String): ConfigurationLoader<T> = ConfigurationLoader(pluginDataFolder.resolve(fileName), T::class.java)

fun <T: Listener> Plugin.registerListener(listener: KClass<T>) = pluginManager.registerListener(this, listener.java.getConstructor().newInstance())

fun <T: Command> Plugin.registerCommand(command: KClass<T>) = pluginManager.registerCommand(this, command.java.getConstructor().newInstance())

fun String.color() = ChatColor.translateAlternateColorCodes('&', this)

val currentUnixTimestamp: Long get() = Instant.now().epochSecond

fun findServer(name: String): ServerInfo? = proxy.serversCopy[name]

fun ProxiedPlayer.movePlayer(target: ServerInfo) {
    this.connect(target)
}

fun InetSocketAddress.toIPString(): String = String(this.address.address)