/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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

    @Setting(value = "should-allow-unknown-classes", comment = "Whether to allow IP addresses to connect which " +
            "couldn't be looked up from GeoIP database")
    var shouldAllowUnknownIPs = false
        private set
}