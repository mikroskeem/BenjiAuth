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

import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.DatabaseTableConfig
import net.md_5.bungee.api.ServerConnectRequest
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.event.ServerConnectEvent
import java.net.InetSocketAddress
import java.util.concurrent.ThreadLocalRandom

/**
 * @author Mark Vainomaa
 */
private val alphabet = "abcdefghijklmopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray()

fun String.processMessage(player: ProxiedPlayer? = null, placeholders: Map<String, Any> = emptyMap()): Array<out BaseComponent> {
    if (this.isEmpty())
        return emptyArray()

    var message = this
            .replace("{player}", player?.name ?: "")
            .replace("{prefix}", messages.prefix)
            .color()

    placeholders.forEach { key, value ->
        message = message.replace("{$key}", value.toString())
    }

    return TextComponent.fromLegacyText(message)
}


fun findServer(name: String): ServerInfo? = proxy.getServerInfo(name)

fun ProxiedPlayer.movePlayer(target: ServerInfo, timeout: Long = config.servers.connectionTimeout,
                             callback: (ServerConnectRequest.Result, Throwable?) -> Unit) {
    val request = ServerConnectRequest.builder()
            .target(target)
            .connectTimeout(timeout.toInt())
            .callback(callback)
            .reason(ServerConnectEvent.Reason.PLUGIN)
            .build()
    connect(request)
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

inline fun <reified T: Any> createDatabaseConfig(connectionSource: ConnectionSource, tableName: String? = null): DatabaseTableConfig<T> {
    val config = DatabaseTableConfig.fromClass(connectionSource, T::class.java)
    tableName?.takeUnless { it.isEmpty() }?.run {
        config.tableName = this
    }
    return config
}

fun generateRandomString(length: Int): String {
    return StringBuilder().apply {
        for (i in 0 until length) {
            append(alphabet[ThreadLocalRandom.current().nextInt(alphabet.size)])
        }
    }.toString()
}

fun String.isValidEmailAddress(): Boolean {
    // TODO: definitely implement a proper check
    return count { it == '@' } == 1 && count { it == '.' } >= 1
}