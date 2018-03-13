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
    var login = "{prefix}Kasutamine: &3/login &8<&3parool&8>"
        private set

    @Setting(value = "help-register", comment = "/register command help")
    var register = "{prefix}Kasutamine: &3/register &8<&3parool&8> <&3parool uuesti&8>"
        private set

    @Setting(value = "help-changepassword", comment = "/changepassword command help")
    var changePassword = "{prefix}Kasutamine: &3/changepassword &8<&3vana parool&8> <&3uus parool&8>"
        private set

    @Setting(value = "help-logout", comment = "/logout command help")
    var logout = "{prefix}Kasutamine: &3/logout"
        private set

    @Setting(value = "admin-help", comment = "/benjiauth help")
    var benjiauthAdmin = """
        {prefix}BenjiAuth
        {prefix}Käsud:
        {prefix}- &3/benjiauth register &8<&3kasutajanimi&8> <&3parool&8>&7 - registreerib mängija
        {prefix}- &3/benjiauth reload &7- taaslaeb plugina seadistuse
        {prefix}- &3/benjiauth login &8<&3kasutajanimi&8> &7- sunnib mängijat sisse logima ilma paroolita
        {prefix}- &3/benjiauth logout &8<&3kasutajanimi&8> &7- logib mängija välja
        {prefix}- &3/benjiauth register &8<&3kasutajanimi&8> <&3parool&8> &7- registreerib mängija andmebaasi valitud parooliga
        {prefix}- &3/benjiauth unregister &8<&3kasutajanimi&8> &7- eemaldab mängija andmebaasist ja logib välja, kui mängija on serveris
    """.trimIndent()

    @Setting(value = "admin-help-unregister", comment = "/benjiauth unregister help")
    var adminUnregister = "{prefix}Kasutamine: &3/benjiauth unregister &8<&3kasutajanimi&8>"
        private set

    @Setting(value = "admin-help-forceregister", comment = "/benjiauth register help")
    var adminRegister = "{prefix}Kasutamine: &3/benjiauth register &8<&3kasutajanimi&8> <&3parool&8>"
        private set

    @Setting(value = "admin-help-logout", comment = "/benjiauth unregister help")
    var adminLogin = "{prefix}Kasutamine: &3/benjiauth login &8<&3kasutajanimi&8>"
        private set

    @Setting(value = "admin-help-logout", comment = "/benjiauth unregister help")
    var adminLogout = "{prefix}Kasutamine: &3/benjiauth logout &8<&3kasutajanimi&8>"
        private set
}
