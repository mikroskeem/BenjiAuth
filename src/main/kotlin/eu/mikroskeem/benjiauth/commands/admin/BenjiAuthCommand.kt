/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.admin

import eu.mikroskeem.benjiauth.ADMIN_ACTION_FORCELOGIN
import eu.mikroskeem.benjiauth.ADMIN_ACTION_LOGOUT
import eu.mikroskeem.benjiauth.ADMIN_ACTION_REGISTER
import eu.mikroskeem.benjiauth.ADMIN_ACTION_RELOAD
import eu.mikroskeem.benjiauth.ADMIN_ACTION_UNREGISTER
import eu.mikroskeem.benjiauth.COMMAND_BENJIAUTH
import eu.mikroskeem.benjiauth.asPlayer
import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.loginWithoutPassword
import eu.mikroskeem.benjiauth.logout
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.proxy
import eu.mikroskeem.benjiauth.userManager
import eu.mikroskeem.benjiauth.validatePassword
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
                "login" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_FORCELOGIN)) {
                        sender.authMessage(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.authMessage(messages.command.adminUnregister)
                        return
                    }

                    // Check if player is online
                    val player = username.asPlayer() ?: run {
                        sender.authMessage(messages.error.noSuchPlayer.replace("{player}", username))
                        return
                    }

                    // Check if player is registered (TODO: plugin architecture does not allow logging in without having a password in database)
                    if(!player.isRegistered) {
                        sender.authMessage(messages.error.userNotRegistered.replace("{player}", username))
                        return
                    }

                    // Check if player is already logged in
                    if(player.isLoggedIn) {
                        sender.authMessage(messages.error.userAlreadyLoggedIn.replace("{player}", username))
                        return
                    }

                    // Force login
                    player.loginWithoutPassword(forceful = true)
                    sender.authMessage(messages.admin.loggedInSuccessfully)
                }
                "logout" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_LOGOUT)) {
                        sender.authMessage(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.authMessage(messages.command.adminUnregister)
                        return
                    }

                    // Check if player is online
                    val player = username.asPlayer() ?: run {
                        sender.authMessage(messages.error.noSuchPlayer.replace("{player}", username))
                        return
                    }

                    // Check if player is logged in
                    if(!player.isLoggedIn) {
                        sender.authMessage(messages.error.userNotLoggedIn.replace("{player}", username))
                        return
                    }

                    // Log out player
                    player.logout()
                    sender.authMessage(messages.admin.loggedOutSuccessfully)
                }
                "register" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_REGISTER)) {
                        sender.authMessage(messages.admin.noPermission)
                        return
                    }

                    // Check if arguments are present
                    val username = args.getOrNull(1) ?: run {
                        sender.authMessage(messages.command.adminRegister)
                        return
                    }

                    val password = args.getOrNull(2) ?: run {
                        sender.authMessage(messages.command.adminRegister)
                        return
                    }

                    // Check if username is registered
                    if(userManager.isRegistered(username)) {
                        sender.authMessage(messages.admin.userAlreadyRegistered.replace("{player}", username))
                        return
                    }

                    // Check if password is valid
                    if(!sender.validatePassword(username, password))
                        return

                    // Register user
                    userManager.registerUser(username, password)
                    sender.authMessage(messages.admin.registeredSuccessfully)
                }
                "unregister" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_UNREGISTER)) {
                        sender.authMessage(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.authMessage(messages.command.adminUnregister)
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
            args.size == 2 && args[0].equals("register", ignoreCase = true) -> proxy.players.map { it.name }
            args.size == 2 && args[0].equals("login", ignoreCase = true) -> proxy.players.map { it.name }
            args.size == 2 && args[0].equals("logout", ignoreCase = true) -> proxy.players.map { it.name }
            args.size <= 1 -> listOf("login", "logout", "reload", "unregister", "register")
            else -> return emptyList()
        }

        val arg = args.last()
        return availableCompletions.filter { it.length >= arg.length && it.toLowerCase(Locale.ENGLISH).startsWith(arg) }.sorted()
    }
}