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

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_CPW
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.changePassword
import eu.mikroskeem.benjiauth.checkPassword
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.validatePassword
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class ChangePasswordCommand: Command("changepassword", COMMAND_CPW, "cpw") {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.message(messages.error.inGameUseOnly)
            return
        }

        // Tell if player isn't registered
        if (!player.isRegistered) {
            player.message(messages.register.mustRegister)
            return
        }

        // <old> <new>
        if(args.size == 2) {
            // Tell if player isn't logged in
            if (!player.isLoggedIn) {
                player.message(messages.login.mustLogin)
                return
            }

            val oldPassword = args[0]
            val newPassword = args[1]

            // Check password length and username usage
            if (!player.validatePassword(newPassword))
                return

            // Check if password matches
            if (!player.checkPassword(oldPassword)) {
                player.message(messages.password.wrongOldPassword)
            } else {
                // Change password
                player.changePassword(newPassword)
                player.message(messages.password.changed)
            }
        } else {
            // Send help
            player.message(messages.command.changePassword)
        }
    }
}