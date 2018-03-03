/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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
            authMessage(messages.password.tooShort.replace("{min}", "${pwConfig.minimumLength}"))
            false
        }
        TOO_LONG -> {
            authMessage(messages.password.tooLong.replace("{max}", "${pwConfig.maximumLength}"))
            false
        }
        USERNAME -> {
            authMessage(messages.password.usernameCannotBeUsed)
            false
        }
        OKAY -> true
    }
}

fun ProxiedPlayer.validatePassword(password: String): Boolean = this.validatePassword(name, password)