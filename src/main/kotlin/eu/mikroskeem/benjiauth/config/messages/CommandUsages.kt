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
    @Setting
    var login = "{prefix}Kasutamine: &3/login &8<&3parool&8>"
        private set

    @Setting
    var register = "{prefix}Kasutamine: &3/register &8<&3parool&8> <&3parool uuesti&8>"
        private set

    @Setting
    var changePassword = "{prefix}Kasutamine: &3/changepassword &8<&3vana parool&8> <&3uus parool&8>"
        private set

    @Setting
    var logout = "{prefix}Kasutamine: &3/logout"
        private set
}
