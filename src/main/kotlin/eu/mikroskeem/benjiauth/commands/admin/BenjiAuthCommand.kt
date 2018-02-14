/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.admin

import eu.mikroskeem.benjiauth.COMMAND_BENJIAUTH
import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.proxy
import eu.mikroskeem.benjiauth.userManager
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor

/**
 * @author Mark Vainomaa
 */
class BenjiAuthCommand: Command("benjiauth", COMMAND_BENJIAUTH), TabExecutor {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if(args.isNotEmpty()) {
            when(args[0]) {
                "reload" -> {
                    plugin.reloadConfig()
                    sender.authMessage(messages.admin.reloadSuccess)
                }
                "unregister" -> {
                    val username = args.getOrNull(1) ?: run {
                        sender.authMessage(messages.command.unregister)
                        return
                    }

                    if(!userManager.isRegistered(username)) {
                        sender.authMessage(messages.admin.noSuchRegisteredUser.replace("{player}", username))
                        return
                    }

                    userManager.unregisterUser(username)
                    sender.authMessage(messages.admin.unregisteredSuccessfully)
                }
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): Iterable<String> {
        return when {
            args.size == 2 && args[0].equals("unregister", ignoreCase = true) -> proxy.players.map { it.name }
            args.size <= 1 -> listOf("reload", "unregister")
            else -> emptyList()
        }
    }
}