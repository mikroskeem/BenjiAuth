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
class AdminMessages {
    @Setting(value = "reload-success", comment = "Sent when plugin reload succeeds")
    var reloadSuccess = "{prefix}Plugina seadistus taaslaetud!"
        private set

    @Setting(value = "no-such-registered-user", comment = "Sent when admin attempted to unregister unknown player")
    var noSuchRegisteredUser = "{prefix}&cEi leidnud mängijat nimega &f{player} &candmebaasist!"
        private set

    @Setting(value = "user-already-registered", comment = "Sent when admin attempted to register already registered player")
    var userAlreadyRegistered = "{prefix}&cMängija &f{player} &con juba registreeritud!"
        private set

    @Setting(value = "unregistered-successfully", comment = "Sent when admin unregisters player successfully")
    var unregisteredSuccessfully = "{prefix}Kasutaja eemaldatud andmebaasist!"
        private set

    @Setting(value = "registered-successfully", comment = "Sent when admin registers player successfully")
    var registeredSuccessfully = "{prefix}Kasutaja registreeritud edukalt!"
        private set

    @Setting(value = "logged-in-successfully", comment = "Sent when admin force logins player successfully")
    var loggedInSuccessfully = "{prefix}Kasutaja edukalt sisse logitud!"
        private set

    @Setting(value = "logged-out-successfully", comment = "Sent when admin logouts player successfully")
    var loggedOutSuccessfully = "{prefix}Kasutaja edukalt välja logitud!"
        private set

    @Setting(value = "no-permission", comment = "Sent when player has no permission for given action")
    var noPermission = "{prefix}Sul pole selleks piisavalt õiguseid!"
        private set
}