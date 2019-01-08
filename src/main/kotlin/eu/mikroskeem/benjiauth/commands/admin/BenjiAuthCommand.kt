/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2019 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.benjiauth.commands.admin

import eu.mikroskeem.benjiauth.ADMIN_ACTION_FORCELOGIN
import eu.mikroskeem.benjiauth.ADMIN_ACTION_LOGOUT
import eu.mikroskeem.benjiauth.ADMIN_ACTION_REGISTER
import eu.mikroskeem.benjiauth.ADMIN_ACTION_RELOAD
import eu.mikroskeem.benjiauth.ADMIN_ACTION_UNREGISTER
import eu.mikroskeem.benjiauth.COMMAND_BENJIAUTH
import eu.mikroskeem.benjiauth.asPlayer
import eu.mikroskeem.benjiauth.message
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
                        sender.message(messages.admin.noPermission)
                        return
                    }

                    // Reload configuration
                    plugin.reloadConfig()
                    sender.message(messages.admin.reloadSuccess)
                }
                "login" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_FORCELOGIN)) {
                        sender.message(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.message(messages.command.adminLogin)
                        return
                    }

                    // Check if player is online
                    val player = username.asPlayer() ?: run {
                        sender.message(messages.error.noSuchPlayer.replace("{player}", username))
                        return
                    }

                    // Check if player is registered (TODO: plugin architecture does not allow logging in without having a password in database)
                    if(!player.isRegistered) {
                        sender.message(messages.error.userNotRegistered.replace("{player}", username))
                        return
                    }

                    // Check if player is already logged in
                    if(player.isLoggedIn) {
                        sender.message(messages.error.userAlreadyLoggedIn.replace("{player}", username))
                        return
                    }

                    // Force login
                    player.loginWithoutPassword(forceful = true)
                    sender.message(messages.admin.loggedInSuccessfully)
                }
                "logout" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_LOGOUT)) {
                        sender.message(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.message(messages.command.adminLogout)
                        return
                    }

                    // Check if player is online
                    val player = username.asPlayer() ?: run {
                        sender.message(messages.error.noSuchPlayer.replace("{player}", username))
                        return
                    }

                    // Check if player is logged in
                    if(!player.isLoggedIn) {
                        sender.message(messages.error.userNotLoggedIn.replace("{player}", username))
                        return
                    }

                    // Log out player
                    player.logout()
                    sender.message(messages.admin.loggedOutSuccessfully)
                }
                "register" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_REGISTER)) {
                        sender.message(messages.admin.noPermission)
                        return
                    }

                    // Check if arguments are present
                    val username = args.getOrNull(1) ?: run {
                        sender.message(messages.command.adminRegister)
                        return
                    }

                    val password = args.getOrNull(2) ?: run {
                        sender.message(messages.command.adminRegister)
                        return
                    }

                    // Check if username is registered
                    if(userManager.isRegistered(username)) {
                        sender.message(messages.admin.userAlreadyRegistered.replace("{player}", username))
                        return
                    }

                    // Check if password is valid
                    if(!sender.validatePassword(username, password))
                        return

                    // Register user
                    userManager.registerUser(username, password)
                    sender.message(messages.admin.registeredSuccessfully)
                }
                "unregister" -> {
                    // Check if player has permission
                    if(!sender.hasPermission(ADMIN_ACTION_UNREGISTER)) {
                        sender.message(messages.admin.noPermission)
                        return
                    }

                    // Check if username argument is present
                    val username = args.getOrNull(1) ?: run {
                        sender.message(messages.command.adminUnregister)
                        return
                    }

                    // Check if username is registered
                    if(!userManager.isRegistered(username)) {
                        sender.message(messages.admin.noSuchRegisteredUser.replace("{player}", username))
                        return
                    }

                    // Unregister user
                    userManager.unregisterUser(username)
                    sender.message(messages.admin.unregisteredSuccessfully)
                }
                else -> {
                    // Send help message
                    sender.message(messages.error.unknownSubcommand.replace("{subcommand}", args[0]))
                }
            }
        } else {
            sender.message(messages.command.benjiauthAdmin.joinToString(separator = "\n"))
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