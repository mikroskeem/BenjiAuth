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

package eu.mikroskeem.benjiauth.config.location

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class CountryWhitelistSection {
    @Setting(value = "allowed-countries", comment = "What countries are allowed to login to server? Set empty to disable")
    var allowedCountries = listOf("EE", "FI")
        private set

    @Setting(value = "allowed-ips", comment = "What IPs are always allowed to login to server regardless of country? " +
            "Set empty to disable")
    var allowedIps = listOf("127.0.0.1")
        private set

    @Setting(value = "country-whitelist-works-as-a-blacklist", comment = "Whether country whitelist behaviour should be reverted, " +
            "e.g 'EE' in allowed-countries means that given country is blocked")
    var countryWhitelistWorksAsABlacklist = false
        private set

    @Setting(value = "should-allow-unknown-ips", comment = "Whether to allow IP addresses to connect which " +
            "couldn't be looked up from GeoIP database")
    var shouldAllowUnknownIPs = false
        private set
}