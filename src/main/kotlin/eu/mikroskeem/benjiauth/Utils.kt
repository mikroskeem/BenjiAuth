/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.net.InetSocketAddress

/**
 * @author Mark Vainomaa
 */
val isWaterfall: Boolean by lazy {
    try { Class.forName("io.github.waterfallmc.waterfall.QueryResult"); true } catch(e: ClassNotFoundException) { false }
}

fun String.processMessage(player: ProxiedPlayer? = null): Array<out BaseComponent> {
    val message = this.takeIf { it.isNotEmpty() }
            ?.replace("{player}", player?.name ?: "")
            ?.replace("{prefix}", messages.prefix)
            ?.color()
            ?: return emptyArray()

    return TextComponent.fromLegacyText(message)
}


fun findServer(name: String): ServerInfo? = proxy.getServerInfo(name)

fun ProxiedPlayer.movePlayer(target: ServerInfo, retry: Boolean = false,
                             timeout: Long = config.servers.connectionTimeout,
                             callback: (Boolean, Throwable?) -> Unit) {
    if(isWaterfall) {
        this.connect(target, callback, retry, timeout.toInt())
        return
    }

    // Note: BungeeCord does not expose timeout option. So custom value does not work
    this.connect(target) connect@ { success, throwable ->
        if(!success && retry) {
            this.connect(target, callback)
            return@connect
        }
        callback(success, throwable)
    }
}

fun InetSocketAddress.toIPString(): String = address.hostAddress

val ProxiedPlayer.ipAddress: String get() = address.toIPString()

fun InetSocketAddress.isAllowedToJoin(): Boolean {
    if(config.country.allowedCountries.isEmpty())
        return true

    if(config.country.allowedIps.contains(toIPString()))
        return true

    val country = geoIpApi.getCountryByIP(address)
    if(country == null && config.country.shouldAllowUnknownIPs) {
        return true
    }

    return if(config.country.countryWhitelistWorksAsABlacklist) {
        !config.country.allowedCountries.contains(country)
    } else {
        config.country.allowedCountries.contains(country)
    }
}