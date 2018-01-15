/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_LOGIN
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.login
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
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
            sender.sendMessage(*TextComponent.fromLegacyText("this command is only for playrs")) // TODO
            return
        }
        attempts.put(player, 0)

        if(player.isLoggedIn) {
            player.sendMessage(*TextComponent.fromLegacyText("you are already logged in!")) // TODO
        } else if(!player.isRegistered) {
            player.sendMessage(*TextComponent.fromLegacyText("you must register first")) // TODO
        } else if(args.isNotEmpty()) {
            val password = args[0]
            if(player.login(password)) {
                player.sendMessage(*TextComponent.fromLegacyText("logged in")) // TODO
            } else {
                player.sendMessage(*TextComponent.fromLegacyText("wrong password")) // TODO
                if(attempts[player]!! >= config.authentication.maxLoginRetries) {
                    player.disconnect(*TextComponent.fromLegacyText("wrong password")) // TODO
                }
                attempts.put(player, attempts[player]!! + 1)
            }
        } else {
            player.sendMessage(*TextComponent.fromLegacyText("usage: /login <password>")) // TODO
        }
    }
}