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

    @Setting(value = "whitelist-works-as-a-blacklist", comment = "Whether whitelist behaviour should be reverted, " +
            "e.g 'EE' in allowed-countries means that given country is blocked")
    var whitelistWorksAsABlacklist = false
        private set
}