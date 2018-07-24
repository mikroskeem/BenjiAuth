/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.config.messages

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class CommandUsages {
    @Setting(value = "help-login", comment = "/login command help")
    var login = "{prefix}Usage: &c&l/login &8<&cpassword&8>"
        private set

    @Setting(value = "help-register", comment = "/register command help")
    var register = "{prefix}Usage: &c&l/register &8<&cpassword&8> <&cpassword again&8>"
        private set

    @Setting(value = "help-changepassword", comment = "/changepassword command help")
    var changePassword = "{prefix}Usage: &c&l/changepassword &8<&cold password&8> <&cnew password&8>"
        private set

    @Setting(value = "help-logout", comment = "/logout command help")
    var logout = "{prefix}Usage: &c&l/logout"
        private set

    @Setting(value = "admin-help", comment = "/benjiauth help")
    var benjiauthAdmin = listOf(
        "{prefix}BenjiAuth",
        "{prefix}Commands:",
        "{prefix}- &c&l/benjiauth register &8<&cusername&8> <&cpassword&8>&7 - registers a player",
        "{prefix}- &c&l/benjiauth reload &7- reloads the configurations",
        "{prefix}- &c&l/benjiauth login &8<&cusername&8> &7- forces player to log in without a password",
        "{prefix}- &c&l/benjiauth logout &8<&cusername&8> &7- logs player out forcefully",
        "{prefix}- &c&l/benjiauth register &8<&cusername&8> <&cpassword&8> &7- registers player into database with given password",
        "{prefix}- &c&l/benjiauth unregister &8<&cusername&8> &7- removes player from the database, and logs player out if one is logged in"
    )

    @Setting(value = "admin-help-unregister", comment = "/benjiauth unregister help")
    var adminUnregister = "{prefix}Usage: &c&l/benjiauth unregister &8<&cusername&8>"
        private set

    @Setting(value = "admin-help-forceregister", comment = "/benjiauth register help")
    var adminRegister = "{prefix}Usage: &c&l/benjiauth register &8<&cusername&8> <&cpassword&8>"
        private set

    @Setting(value = "admin-help-logout", comment = "/benjiauth unregister help")
    var adminLogin = "{prefix}Usage: &c&l/benjiauth login &8<&cusername&8>"
        private set

    @Setting(value = "admin-help-logout", comment = "/benjiauth unregister help")
    var adminLogout = "{prefix}Usage: &c&l/benjiauth logout &8<&cusername&8>"
        private set
}
