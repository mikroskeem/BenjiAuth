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