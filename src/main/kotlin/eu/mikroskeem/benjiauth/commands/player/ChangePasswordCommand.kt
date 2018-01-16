/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_CPW
import eu.mikroskeem.benjiauth.changePassword
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
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
            sender.sendMessage(*TextComponent.fromLegacyText("this command is only for playrs")) // TODO
            return
        }

        if(!player.isRegistered) {
            sender.sendMessage(*TextComponent.fromLegacyText("you must be registered first")) // TODO
        } else if(!player.isLoggedIn) {
            sender.sendMessage(*TextComponent.fromLegacyText("you must be logged in")) // TODO
        } else if(args.size == 2) {
            val oldPassword = args[0]
            val newPassword = args[1]

            // TODO: password check

            player.changePassword(newPassword)
            player.sendMessage(*TextComponent.fromLegacyText("password changed!")) // TODO
        } else {
            sender.sendMessage(*TextComponent.fromLegacyText("usage: /changepassword <old> <new>")) // TODO
        }
    }
}