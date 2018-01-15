/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_LOGOUT
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.logout
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class LogoutCommand: Command("logout", COMMAND_LOGOUT) {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.sendMessage(*TextComponent.fromLegacyText("this command is only for playrs")) // TODO
            return
        }

        if(player.isLoggedIn) {
            player.logout()
            sender.sendMessage(*TextComponent.fromLegacyText("logged out")) // TODO
        } else {
            sender.sendMessage(*TextComponent.fromLegacyText("you must be logged in")) // TODO
        }
    }
}