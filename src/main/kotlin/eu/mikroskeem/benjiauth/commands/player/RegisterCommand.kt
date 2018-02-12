/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_REGISTER
import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.register
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class RegisterCommand: Command("register", COMMAND_REGISTER) {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.authMessage(messages.error.inGameUseOnly)
            return
        }

        if(player.isRegistered) {
            // Tell if player is already registered
            player.authMessage(messages.register.alreadyRegistered)
        } else if(player.isLoggedIn) {
            // Tell if player is already logged in
            player.authMessage(messages.login.alreadyLoggedIn)
        } else if(args.size == 2) {
            val password = args[0]
            val confirmPassword = args[1]

            // Check if passwords don't match
            if(password != confirmPassword) {
                player.authMessage(messages.password.dontMatch)
            } else {
                // Check password length and username usage
                // Note: if user sets configuration values to something unreasonable, then
                //       I *will* tell user that PEBKAC
                config.registration.password.run {
                    if(minimumLength > password.length) {
                        player.authMessage(messages.password.tooShort.replace("{min}", "$minimumLength"))
                        return
                    } else if(maximumLength < password.length) {
                        player.authMessage(messages.password.tooLong.replace("{max}", "$maximumLength"))
                        return
                    }

                    if(config.registration.password.disallowUsingUsername && password == player.name) {
                        player.authMessage(messages.password.usernameCannotBeUsed)
                        return
                    }
                }

                // Register player
                player.register(password)

                // If player should be logged in after registering
                if(config.registration.loginAfterRegister) {
                    player.authMessage(messages.register.registered)
                } else {
                    player.authMessage(messages.register.registeredAndMustLogin)
                }
            }
        } else {
            // Send help
            player.authMessage(messages.command.register)
        }
    }
}