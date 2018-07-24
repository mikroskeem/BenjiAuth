/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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