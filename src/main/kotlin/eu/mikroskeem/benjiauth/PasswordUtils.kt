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

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.PasswordResult.OKAY
import eu.mikroskeem.benjiauth.PasswordResult.TOO_LONG
import eu.mikroskeem.benjiauth.PasswordResult.TOO_SHORT
import eu.mikroskeem.benjiauth.PasswordResult.USERNAME
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Mark Vainomaa
 */
enum class PasswordResult {
    OKAY,
    USERNAME,
    TOO_SHORT,
    TOO_LONG,
}

private fun checkPassword(username: String, password: String): PasswordResult {
    val pwConfig = config.registration.password

    if(pwConfig.minimumLength > password.length) {
        return TOO_SHORT
    } else if(pwConfig.maximumLength < password.length) {
        return TOO_LONG
    }

    if(config.registration.password.disallowUsingUsername && password == username) {
        return USERNAME
    }

    return OKAY
}

fun CommandSender.validatePassword(username: String, password: String): Boolean {
    val pwConfig = config.registration.password
    return when(checkPassword(username, password)) {
        TOO_SHORT -> {
            message(messages.password.tooShort.replace("{min}", "${pwConfig.minimumLength}"))
            false
        }
        TOO_LONG -> {
            message(messages.password.tooLong.replace("{max}", "${pwConfig.maximumLength}"))
            false
        }
        USERNAME -> {
            message(messages.password.usernameCannotBeUsed)
            false
        }
        OKAY -> true
    }
}

fun ProxiedPlayer.validatePassword(password: String): Boolean = this.validatePassword(name, password)