/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_CPW
import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.changePassword
import eu.mikroskeem.benjiauth.checkPassword
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.messages
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class ChangePasswordCommand: Command("changepassword", COMMAND_CPW, "cpw") {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.authMessage(messages.error.inGameUseOnly)
            return
        }

        // Tell if player isn't registered
        if(!player.isRegistered) {
            player.authMessage(messages.register.mustRegister)
            return
        }

        // <old> <new>
        if(args.size == 2) {
            // Tell if player isn't logged in
            if(!player.isLoggedIn) {
                player.authMessage(messages.login.mustLogin)
                return
            }

            val oldPassword = args[0]
            val newPassword = args[1]

            // Check password length and username usage
            config.registration.password.run {
                if(minimumLength > newPassword.length) {
                    player.authMessage(messages.password.tooShort.replace("{min}", "$minimumLength"))
                    return
                } else if(maximumLength < newPassword.length) {
                    player.authMessage(messages.password.tooLong.replace("{max}", "$maximumLength"))
                    return
                }

                if(config.registration.password.disallowUsingUsername && newPassword == player.name) {
                    player.authMessage(messages.password.usernameCannotBeUsed)
                    return
                }
            }

            // Check if password matches
            if(!player.checkPassword(oldPassword)) {
                player.authMessage(messages.password.wrongOldPassword)
            } else {
                // Change password
                player.changePassword(newPassword)
                player.authMessage(messages.password.changed)
            }
        } else {
            // Send help
            player.authMessage(messages.command.changePassword)
        }
    }
}