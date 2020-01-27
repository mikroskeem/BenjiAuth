/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.benjiauth.config

import eu.mikroskeem.benjiauth.config.messages.AdminMessages
import eu.mikroskeem.benjiauth.config.messages.CommandUsages
import eu.mikroskeem.benjiauth.config.messages.EmailMessages
import eu.mikroskeem.benjiauth.config.messages.ErrorMessages
import eu.mikroskeem.benjiauth.config.messages.LoginMessages
import eu.mikroskeem.benjiauth.config.messages.PasswordMessages
import eu.mikroskeem.benjiauth.config.messages.RegisterMessages
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class BenjiMessages {
    @Setting(value = "prefix", comment = "Prefix prepended to all messages. Can be left empty")
    var prefix = "&8[&cBenjiAuth&8] &7"
        private set

    @Setting(value = "login", comment = "Login related messages")
    var login = LoginMessages()
        private set

    @Setting(value = "register", comment = "Register related messages")
    var register = RegisterMessages()
        private set

    @Setting(value = "password", comment = "Password related messages")
    var password = PasswordMessages()
        private set

    @Setting(value = "email", comment = "E-mail related messages")
    var email = EmailMessages()
        private set

    @Setting(value = "error", comment = "Error messages")
    var error = ErrorMessages()
        private set

    @Setting(value = "commands", comment = "Command help messages")
    var command = CommandUsages()
        private set

    @Setting(value = "admin", comment = "Admin messages")
    var admin = AdminMessages()
        private set
}