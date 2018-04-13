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
    var login = "{prefix}Kasutamine: &f/login &8<&fparool&8>"
        private set

    @Setting(value = "help-register", comment = "/register command help")
    var register = "{prefix}Kasutamine: &f/register &8<&fparool&8> <&fparool uuesti&8>"
        private set

    @Setting(value = "help-changepassword", comment = "/changepassword command help")
    var changePassword = "{prefix}Kasutamine: &f/changepassword &8<&fvana parool&8> <&fuus parool&8>"
        private set

    @Setting(value = "help-logout", comment = "/logout command help")
    var logout = "{prefix}Kasutamine: &f/logout"
        private set

    @Setting(value = "admin-help", comment = "/benjiauth help")
    var benjiauthAdmin = listOf(
        "{prefix}BenjiAuth",
        "{prefix}Käsud:",
        "{prefix}- &f/benjiauth register &8<&fkasutajanimi&8> <&fparool&8>&7 - registreerib mängija",
        "{prefix}- &f/benjiauth reload &7- taaslaeb plugina seadistuse",
        "{prefix}- &f/benjiauth login &8<&fkasutajanimi&8> &7- sunnib mängijat sisse logima ilma paroolita",
        "{prefix}- &f/benjiauth logout &8<&fkasutajanimi&8> &7- logib mängija välja",
        "{prefix}- &f/benjiauth register &8<&fkasutajanimi&8> <&fparool&8> &7- registreerib mängija andmebaasi valitud parooliga",
        "{prefix}- &f/benjiauth unregister &8<&fkasutajanimi&8> &7- eemaldab mängija andmebaasist ja logib välja, kui mängija on serveris"
    )

    @Setting(value = "admin-help-unregister", comment = "/benjiauth unregister help")
    var adminUnregister = "{prefix}Kasutamine: &f/benjiauth unregister &8<&fkasutajanimi&8>"
        private set

    @Setting(value = "admin-help-forceregister", comment = "/benjiauth register help")
    var adminRegister = "{prefix}Kasutamine: &f/benjiauth register &8<&fkasutajanimi&8> <&fparool&8>"
        private set

    @Setting(value = "admin-help-logout", comment = "/benjiauth unregister help")
    var adminLogin = "{prefix}Kasutamine: &f/benjiauth login &8<&fkasutajanimi&8>"
        private set

    @Setting(value = "admin-help-logout", comment = "/benjiauth unregister help")
    var adminLogout = "{prefix}Kasutamine: &f/benjiauth logout &8<&fkasutajanimi&8>"
        private set
}
