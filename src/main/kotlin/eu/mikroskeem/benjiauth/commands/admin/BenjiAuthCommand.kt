/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.admin

import eu.mikroskeem.benjiauth.ADMIN_ACTION_RELOAD
import eu.mikroskeem.benjiauth.ADMIN_ACTION_UNREGISTER
import eu.mikroskeem.benjiauth.COMMAND_BENJIAUTH
import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.proxy
import eu.mikroskeem.benjiauth.userManager
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import java.util.Locale

/**
 * @author Mark Vainomaa
 */
class BenjiAuthCommand: Command("benjiauth", COMMAND_BENJIAUTH), TabExecutor {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        if(args.isNotEmpty()) {
            when(args[0]) {
                "reload" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_RELOAD)) {
                        sender.authMessage(messages.admin.noPermission)
                        return
                    }

                    // Reload configuration
                    plugin.reloadConfig()
                    sender.authMessage(messages.admin.reloadSuccess)
                }
                "unregister" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_UNREGISTER)) {
                        sender.authMessage(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.authMessage(messages.command.unregister)
                        return
                    }

                    // Check if username is registered
                    if(!userManager.isRegistered(username)) {
                        sender.authMessage(messages.admin.noSuchRegisteredUser.replace("{player}", username))
                        return
                    }

                    // Unregister user
                    userManager.unregisterUser(username)
                    sender.authMessage(messages.admin.unregisteredSuccessfully)
                }
                else -> {
                    // Send help message
                    sender.authMessage(messages.error.unknownSubcommand.replace("{subcommand}", args[0]))
                }
            }
        } else {
            sender.authMessage(messages.command.benjiauthAdmin)
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): Iterable<String> {
        val availableCompletions = when {
            args.size == 2 && args[0].equals("unregister", ignoreCase = true) -> proxy.players.map { it.name }
            args.size <= 1 -> listOf("reload", "unregister")
            else -> return emptyList()
        }

        val arg = args.last()
        return availableCompletions.filter { it.length >= arg.length && it.toLowerCase(Locale.ENGLISH).startsWith(arg) }.sorted()
    }
}