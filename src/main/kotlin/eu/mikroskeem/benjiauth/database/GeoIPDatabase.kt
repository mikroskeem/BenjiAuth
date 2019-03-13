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

package eu.mikroskeem.benjiauth.database

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.exception.AddressNotFoundException
import eu.mikroskeem.benjiauth.GeoIPAPI
import eu.mikroskeem.benjiauth.plugin
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import java.io.FileOutputStream
import java.net.ConnectException
import java.net.InetAddress
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Path
import java.util.Optional
import java.util.Optional.empty
import java.util.Optional.of
import java.util.concurrent.TimeUnit.HOURS
import java.util.zip.GZIPInputStream

/**
 * GeoLite country database wrapper and cacher
 *
 * @author Mark Vainomaa
 */
private val DATABASE_URL = "https://geolite.maxmind.com/download/geoip/database/GeoLite2-Country.tar.gz"

class GeoIPDatabase(databaseDirectory: Path = plugin.pluginFolder): GeoIPAPI {
    private val databaseFile: Path = databaseDirectory.resolve("geoip.db")
    private val database: DatabaseReader
    private val cache: LoadingCache<InetAddress, Optional<String>>

    init {
        Files.createDirectories(databaseFile.parent)
        if(Files.notExists(databaseFile)) {
            val urlStream = try { URL(DATABASE_URL).openStream() } catch (e: ConnectException) {
                throw RuntimeException("Failed to connect to $DATABASE_URL", e)
            }
            val unpackStream = TarArchiveInputStream(GZIPInputStream(urlStream))

            var tarEntry: TarArchiveEntry? = unpackStream.nextTarEntry
            while(tarEntry != null) {
                if(tarEntry.isFile && tarEntry.name.endsWith("GeoLite2-Country.mmdb"))
                    break

                tarEntry = unpackStream.nextTarEntry
            }

            if(tarEntry == null)
                throw IllegalStateException("Failed to find GeoLite2-Country.mmdb from tarball!")

            Channels.newChannel(unpackStream).use { download ->
                FileOutputStream(databaseFile.toFile()).use { file ->
                    file.channel.transferFrom(download, 0, Long.MAX_VALUE)
                }
            }
        }

        database = DatabaseReader.Builder(databaseFile.toFile()).build()
        val loader = object: CacheLoader<InetAddress, Optional<String>>() {
            override fun load(key: InetAddress): Optional<String> {
                return try {
                    of(database.country(key).country.isoCode)
                } catch (e: AddressNotFoundException) {
                    empty()
                }
            }
        }
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(12, HOURS)
                .maximumSize(16384)
                .build(loader)
    }


    override fun getCountryByIP(ipAddress: InetAddress): String? = cache.get(ipAddress).map { it }.orElse(null)
    override fun getCountryByIP(ipAddress: String): String? = getCountryByIP(InetAddress.getByName(ipAddress))
}