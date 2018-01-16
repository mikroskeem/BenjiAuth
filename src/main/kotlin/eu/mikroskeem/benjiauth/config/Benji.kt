/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config

import eu.mikroskeem.benjiauth.config.authentication.AuthenticationSection
import eu.mikroskeem.benjiauth.config.database.DatabaseSection
import eu.mikroskeem.benjiauth.config.location.CountryWhitelistSection
import eu.mikroskeem.benjiauth.config.registration.RegistrationSection
import eu.mikroskeem.benjiauth.config.servers.ServersSection
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class Benji {
    @Setting(value = "database", comment = "Plugin database configuration")
    var database = DatabaseSection()
        private set

    @Setting(value = "authentication", comment = "Authentication related configuration")
    var authentication = AuthenticationSection()
        private set

    @Setting(value = "registration", comment = "Registration related configuration")
    var registration = RegistrationSection()
        private set

    @Setting(value = "country", comment = "Country whitelist configuration")
    var country = CountryWhitelistSection()
        private set

    @Setting(value = "servers", comment = "Authentication server configuration")
    var servers = ServersSection()
        private set
}