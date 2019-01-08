/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_REGISTER
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.ipAddress
import eu.mikroskeem.benjiauth.ipHasTooManyRegistrations
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.register
import eu.mikroskeem.benjiauth.userManager
import eu.mikroskeem.benjiauth.validatePassword
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class RegisterCommand: Command("register", COMMAND_REGISTER) {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.message(messages.error.inGameUseOnly)
            return
        }

        if(player.isRegistered) {
            // Tell if player is already registered
            player.message(messages.register.alreadyRegistered)
        } else if(player.ipHasTooManyRegistrations()) {
            // Tell that player has too many registrations for this IP address
            player.message(messages.error.tooManyRegistrationsPerIP)
        } else {
            if(config.registration.newRegistrationsDisabled) {
                player.message(messages.register.newRegistrationsDisabled)
                return
            }

            if(args.size == 2) {
                val password = args[0]
                val confirmPassword = args[1]

                // Check if passwords don't match
                if(password != confirmPassword) {
                    player.message(messages.password.dontMatch)
                } else {
                    // Check password length and username usage
                    // Note: if user sets configuration values to something unreasonable, then
                    //       I *will* tell user that PEBKAC
                    if(!player.validatePassword(password))
                        return

                    // Register player
                    player.register(password)

                    // If player should be logged in after registering
                    if(config.registration.loginAfterRegister) {
                        player.message(messages.register.registered)
                    } else {
                        player.message(messages.register.registeredAndMustLogin)
                    }
                }
            } else {
                // Send help
                player.message(messages.command.register)
            }
        }
    }
}