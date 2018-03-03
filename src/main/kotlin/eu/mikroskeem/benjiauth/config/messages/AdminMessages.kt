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
    var noSuchRegisteredUser = "{prefix}&cEi leidnud mängijat nimega &3{player} &candmebaasist!"
        private set

    @Setting(value = "user-already-registered", comment = "Sent when admin attempted to register already registered player")
    var userAlreadyRegistered = "{prefix}&cMängija &3{player} &con juba registreeritud!"
        private set

    @Setting(value = "unregistered-successfully", comment = "Sent when admin unregisters player successfully")
    var unregisteredSuccessfully = "{prefix}Kasutaja eemaldatud andmebaasist!"
        private set

    @Setting(value = "registered-successfully", comment = "Sent when admin registers player successfully")
    var registeredSuccessfully = "{prefix}Kasutaja registreeritud edukalt!"
        private set

    @Setting(value = "no-permission", comment = "Sent when player has no permission for given action")
    var noPermission = "{prefix}Sul pole selleks piisavalt õiguseid!"
        private set
}