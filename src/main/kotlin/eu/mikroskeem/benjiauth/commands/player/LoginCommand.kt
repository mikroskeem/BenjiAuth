/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
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
        if(!player.isRegistered) {
            player.message(messages.register.mustRegister)
            return
        }

        // Tell if player is already logged in
        if(player.isLoggedIn) {
            player.message(messages.login.alreadyLoggedIn)
            return
        }

        // Initialize counter
        attempts.computeIfAbsent(player) { AtomicInteger(0) }

        // If password is supplied
        if(args.size == 1) {
            // Tell if player is already logged in
            if(player.isLoggedIn) {
                player.message(messages.login.alreadyLoggedIn)
                return
            }

            val password = args[0]
            if(player.login(password)) {
                // Logged in!
                player.message(messages.login.loggedIn)
            } else {
                // Password is wrong
                attempts[player]!!.incrementAndGet()

                // Check login attempts count
                if(attempts[player]!!.get() >= config.authentication.maxLoginRetries) {
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