/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config

import eu.mikroskeem.benjiauth.config.messages.AdminMessages
import eu.mikroskeem.benjiauth.config.messages.CommandUsages
import eu.mikroskeem.benjiauth.config.messages.ErrorMessages
import eu.mikroskeem.benjiauth.config.messages.LoginMessages
import eu.mikroskeem.benjiauth.config.messages.PasswordMessages
import eu.mikroskeem.benjiauth.config.messages.RegisterMessages
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class BenjiMessages {
    @Setting(value = "prefix", comment = "Prefix prepended to all messages. Can be left empty")
    var prefix = "&8[&cBenjiAuth&8] &7"
        private set

    @Setting(value = "login", comment = "Login related messages")
    var login = LoginMessages()
        private set

    @Setting(value = "register", comment = "Register related messages")
    var register = RegisterMessages()
        private set

    @Setting(value = "password", comment = "Password related messages")
    var password = PasswordMessages()
        private set

    @Setting(value = "error", comment = "Error messages")
    var error = ErrorMessages()
        private set

    @Setting(value = "commands", comment = "Command help messages")
    var command = CommandUsages()
        private set

    @Setting(value = "admin", comment = "Admin messages")
    var admin = AdminMessages()
        private set
}