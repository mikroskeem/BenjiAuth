/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import net.md_5.bungee.api.Callback
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.lang.reflect.Method
import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

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


fun findServer(name: String): ServerInfo? = proxy.getServerInfo(name)

private val waterfallConnect: Method? by lazy {
    try {
        ProxiedPlayer::class.java.getMethod("connect",
                ServerInfo::class.java, Callback::class.java,
                Boolean::class.javaPrimitiveType,
                Int::class.javaPrimitiveType)
    } catch (e: NoSuchMethodException) {
        null
    }
}

private fun ProxiedPlayer.waterfallConnect(target: ServerInfo, retry: Boolean, timeout: Int, callback: Callback<Boolean>) {
    waterfallConnect!!.invoke(this, target, callback, retry, timeout)
}

fun ProxiedPlayer.movePlayer(target: ServerInfo, retry: Boolean = false,
                             timeout: Long = TimeUnit.SECONDS.toMillis(5),
                             callback: (Boolean, Throwable?) -> Unit) {
    waterfallConnect?.run { waterfallConnect(target, retry, timeout.toInt(), Callback { s, t -> callback(s, t) }); return }

    // Note: BungeeCord does not expose timeout option. Though this won't hurt right now, as BenjiAuth does not
    // make use of timeout option *yet*.
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