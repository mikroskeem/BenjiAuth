/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_LOGOUT
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.logout
import eu.mikroskeem.benjiauth.messages
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class LogoutCommand: Command("logout", COMMAND_LOGOUT) {
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

        // Don't take any arguments (yes people may find this behavior stupid, but I think it's their fault)
        if(args.isNotEmpty()) {
            player.message(messages.command.logout)
            return
        }

        // Logout player
        if(player.isLoggedIn) {
            player.logout()
            player.message(messages.login.loggedOut)
        } else {
            player.message(messages.login.mustLogin)
        }
    }
}