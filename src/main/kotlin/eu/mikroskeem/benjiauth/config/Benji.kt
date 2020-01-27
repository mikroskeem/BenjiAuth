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

package eu.mikroskeem.benjiauth.config

import eu.mikroskeem.benjiauth.config.authentication.AuthenticationSection
import eu.mikroskeem.benjiauth.config.database.DatabaseSection
import eu.mikroskeem.benjiauth.config.email.EmailSection
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

    @Setting(value = "email", comment = "E-mailing related configuration")
    var email = EmailSection()
        private set

    @Setting(value = "country", comment = "Country whitelist configuration")
    var country = CountryWhitelistSection()
        private set

    @Setting(value = "servers", comment = "Authentication server configuration")
    var servers = ServersSection()
        private set
}