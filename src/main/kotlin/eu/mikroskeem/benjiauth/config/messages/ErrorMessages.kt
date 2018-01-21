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
}