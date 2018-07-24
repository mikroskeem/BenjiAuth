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
    var reloadSuccess = "{prefix}Plugin configuration reloaded!"
        private set

    @Setting(value = "no-such-registered-user", comment = "Sent when admin attempted to unregister unknown player")
    var noSuchRegisteredUser = "{prefix}&cCouldn't find a player with name &c&l{player} &cfrom the database!"
        private set

    @Setting(value = "user-already-registered", comment = "Sent when admin attempted to register already registered player")
    var userAlreadyRegistered = "{prefix}&cPlayer &c&l{player} &cis already registered!"
        private set

    @Setting(value = "unregistered-successfully", comment = "Sent when admin unregisters player successfully")
    var unregisteredSuccessfully = "{prefix}Player unregistered from the database successfully!"
        private set

    @Setting(value = "registered-successfully", comment = "Sent when admin registers player successfully")
    var registeredSuccessfully = "{prefix}Player registered successfully!"
        private set

    @Setting(value = "logged-in-successfully", comment = "Sent when admin force logins player successfully")
    var loggedInSuccessfully = "{prefix}Player logged in successfully!"
        private set

    @Setting(value = "logged-out-successfully", comment = "Sent when admin logouts player successfully")
    var loggedOutSuccessfully = "{prefix}Player logged out successfully!"
        private set

    @Setting(value = "no-permission", comment = "Sent when player has no permission for given action")
    var noPermission = "{prefix}You don't have enough permissions for that!"
        private set
}