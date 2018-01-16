/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_REGISTER
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
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
            sender.sendMessage(*TextComponent.fromLegacyText("this command is only for playrs")) // TODO
            return
        }

        if(player.isLoggedIn) {
            sender.sendMessage(*TextComponent.fromLegacyText("you are already logged in")) // TODO
        } else if(player.isRegistered) {
            sender.sendMessage(*TextComponent.fromLegacyText("you are already registered, please log in")) // TODO
        } else if(args.size == 2) {
            val password = args[0]
            val confirmPassword = args[1]
            if(password != confirmPassword) {
                player.sendMessage(*TextComponent.fromLegacyText("passwords don't match!")) // TODO
            } else {
                player.register(password)
                if(config.registration.loginAfterRegister) {
                    player.sendMessage(*TextComponent.fromLegacyText("user registered successfully")) // TODO
                } else {
                    player.sendMessage(*TextComponent.fromLegacyText("user registered successfully, you may log in now")) // TODO
                }
            }
        } else {
            player.sendMessage(*TextComponent.fromLegacyText("usage: /register <password> <confirmPassword>")) // TODO
        }
    }
}