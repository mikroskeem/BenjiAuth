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
class ErrorMessages {
    @Setting
    var inGameUseOnly = "{prefix}&cAntud command on mõeldud mängijatele!"
        private set

    @Setting
    var invalidUsername = "{prefix}&cVigane kasutajanimi, kasutage midagi muud!"
        private set

    @Setting
    var ipAddressDisallowed = "{prefix}&cTeie IP aadress on blokeeritud siin serveris."
        private set

    @Setting
    var couldntConnectToLobby = "{prefix}&cEi suutnud peaserveriga ühenduda!"
        private set

    @Setting
    var couldntConnectToAuthserver = "{prefix}&cEi suutnud autentimisserveriga ühenduda!"
        private set
}