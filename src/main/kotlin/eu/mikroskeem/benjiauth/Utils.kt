/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.config.ConfigurationLoader
import eu.mikroskeem.benjiauth.tasks.Task
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.scheduler.ScheduledTask
import java.net.InetSocketAddress
import java.time.Instant
import kotlin.reflect.KClass

/**
 * @author Mark Vainomaa
 */


fun String.processMessage(player: ProxiedPlayer? = null): Array<out BaseComponent> {
    val message = this.takeIf { it.isNotEmpty() }
            ?.replace("{player}", player?.name ?: "")
            ?.replace("{prefix}", messages.prefix)
            ?.color()
            ?: return emptyArray()

    return TextComponent.fromLegacyText(message)
}


fun findServer(name: String): ServerInfo? = proxy.serversCopy[name]

fun ProxiedPlayer.movePlayer(target: ServerInfo) {
    this.connect(target)
}

fun InetSocketAddress.toIPString(): String = address.hostAddress

fun InetSocketAddress.isAllowedToJoin(): Boolean {
    if(config.country.allowedCountries.isEmpty())
        return true

    if(config.country.allowedIps.contains(toIPString()))
        return true

    val country = geoIpApi.getCountryByIP(address)
    return if(config.country.countryWhitelistWorksAsABlacklist) {
        !config.country.allowedCountries.contains(country)
    } else {
        config.country.allowedCountries.contains(country)
    }
}