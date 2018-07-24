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
