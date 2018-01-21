/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_LOGIN
import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.login
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.processMessage
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import java.util.WeakHashMap

/**
 * @author Mark Vainomaa
 */
class LoginCommand: Command("login", COMMAND_LOGIN, "l") {
    private val attempts = WeakHashMap<ProxiedPlayer, Int>()

    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.authMessage(messages.error.inGameUseOnly)
            return
        }
        attempts[player] = 0

        if(!player.isRegistered) {
            player.authMessage(messages.register.mustRegister)
            return
        }

        if(args.size == 1) {
            if(player.isLoggedIn) {
                player.authMessage(messages.login.alreadyLoggedIn)
            }

            val password = args[0]
            if(player.login(password)) {
                player.authMessage(messages.login.loggedIn)
            } else {
                player.authMessage(messages.password.wrong)
                if(attempts[player]!! >= config.authentication.maxLoginRetries) {
                    player.disconnect(*messages.password.wrong.processMessage(player))
                }
                attempts.put(player, attempts[player]!! + 1)
            }
        } else {
            player.authMessage(messages.command.login)
        }
    }
}