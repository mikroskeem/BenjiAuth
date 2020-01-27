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

import eu.mikroskeem.benjiauth.COMMAND_LOGIN
import eu.mikroskeem.benjiauth.kickWithMessage
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.login
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.sendTitle
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author Mark Vainomaa
 */
class LoginCommand: Command("login", COMMAND_LOGIN, "l") {
    // Player login attempts
    private val attempts = WeakHashMap<ProxiedPlayer, AtomicInteger>()

    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.message(messages.error.inGameUseOnly)
            return
        }

        // Tell if player is not registered
        if (!player.isRegistered) {
            player.message(messages.register.mustRegister)
            return
        }

        // Tell if player is already logged in
        if (player.isLoggedIn) {
            player.message(messages.login.alreadyLoggedIn)
            return
        }

        // Initialize counter
        attempts.computeIfAbsent(player) { AtomicInteger(0) }

        // If password is supplied
        if (args.size == 1) {
            // Tell if player is already logged in
            if (player.isLoggedIn) {
                player.message(messages.login.alreadyLoggedIn)
                return
            }

            val password = args[0]
            if (player.login(password)) {
                // Logged in!
                player.message(messages.login.loggedIn)
            } else {
                // Password is wrong
                attempts[player]!!.incrementAndGet()

                // Check login attempts count
                if (attempts[player]!!.get() >= config.authentication.maxLoginRetries) {
                    // Kick if there are too many login attempts
                    player.kickWithMessage(messages.password.wrong)
                    return
                } else {
                    player.message(messages.password.wrong)
                    player.sendTitle(messages.password.wrongTitle)
                }
            }
        } else {
            // Send help message
            player.message(messages.command.login)
        }
    }
}