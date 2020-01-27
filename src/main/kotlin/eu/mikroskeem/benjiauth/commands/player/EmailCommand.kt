/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.benjiauth.commands.player

import eu.mikroskeem.benjiauth.COMMAND_EMAIL
import eu.mikroskeem.benjiauth.LoginManager
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.emailAddress
import eu.mikroskeem.benjiauth.generateRandomString
import eu.mikroskeem.benjiauth.isEmailVerified
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.isValidEmailAddress
import eu.mikroskeem.benjiauth.kickWithMessage
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.userManager
import eu.mikroskeem.benjiauth.validatePassword
import net.jodah.expiringmap.ExpiringMap
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.logging.Level

/**
 * @author Mark Vainomaa
 */
class EmailCommand: Command("email", COMMAND_EMAIL), TabExecutor {
    private val emailVerifyCooldowns = ExpiringMap.builder().variableExpiration().build<UUID, Boolean>()
    private val passwordResetCooldowns = ExpiringMap.builder().variableExpiration().build<UUID, Boolean>()

    override fun execute(sender: CommandSender, args: Array<out String>) {
        val player = sender as? ProxiedPlayer ?: run {
            sender.message(messages.error.inGameUseOnly)
            return
        }

        // Tell if player isn't registered
        if (!player.isRegistered) {
            player.message(messages.register.mustRegister)
            return
        }

        if (!config.email.enabled) {
            player.message(messages.email.notEnabled)
            return
        }

        when (args.getOrNull(0)) {
            "add" -> {
                if (!player.isLoggedIn) {
                    player.message(messages.login.mustLogin)
                    return
                }

                if (args.size < 2) {
                    player.message(messages.command.email)
                    return
                }

                if (player.emailAddress != null) {
                    if (player.isEmailVerified) {
                        player.message(messages.email.alreadySet)
                    } else {
                        player.message(messages.email.unsetBeforeSettingNew)
                    }
                    return
                } else if (emailVerifyCooldowns.containsKey(player.uniqueId)) {
                    player.message(messages.email.emailAddCooldown)
                    return
                }

                val emailAddress = args[1].takeIf { it.isValidEmailAddress() } ?: run {
                    player.message(messages.email.invalidAddress)
                    return
                }

                if (userManager.getEmailUsages(emailAddress) > 0) {
                    player.message(messages.email.alreadyInUse)
                    return
                }

                player.message(messages.email.sendingEmail)
                val verificationCode = generateRandomString(config.email.verificationCodeLength)
                userManager.setEmail(player, emailAddress, verificationCode) // This must be here, as email sending code requires address to be present
                plugin.api.emailManager.sendVerificationEmail(player, verificationCode).handleAsync { _, e ->
                    if (e != null) {
                        player.message(messages.email.somethingWentWrong)
                        plugin.pluginLogger.log(Level.SEVERE, "Failed to send verification email to ''$emailAddress''", e)
                        userManager.setEmail(player, null, null)
                        return@handleAsync null
                    }

                    emailVerifyCooldowns.put(player.uniqueId, true, config.email.verificationEmailCooldown, TimeUnit.SECONDS)
                    player.message(messages.email.emailSent)
                }
            }
            "remove" -> {
                if(!player.isLoggedIn) {
                    player.message(messages.login.mustLogin)
                    return
                }

                if (player.emailAddress != null) {
                    userManager.setEmail(player, null, null)
                    player.message(messages.email.unset)
                } else {
                    player.message(messages.email.notSet)
                }
            }
            "recover" -> {
                val emailAddress = player.emailAddress ?: run {
                    player.message(messages.email.notSet)
                    return
                }

                if (!player.isEmailVerified) {
                    player.message(messages.email.notVerified)
                    return
                }

                var resetCode = userManager.getPasswordResetCode(player)
                when {
                    args.size == 1 -> {
                        if (resetCode != null && passwordResetCooldowns.containsKey(player.uniqueId)) {
                            player.message(messages.email.passwordRecoveryCooldown)
                            return
                        }

                        player.message(messages.email.sendingEmail)
                        resetCode = generateRandomString(config.email.verificationCodeLength)
                        plugin.api.emailManager.sendPasswordRecoveryEmail(player, resetCode).handleAsync { _, e ->
                            if (e != null) {
                                player.message(messages.email.somethingWentWrong)
                                plugin.pluginLogger.log(Level.SEVERE, "Failed to send password recovery email to ''$emailAddress''", e)
                                return@handleAsync null
                            }

                            passwordResetCooldowns.put(player.uniqueId, true, config.email.passwordResetEmailCooldown, TimeUnit.SECONDS)
                            userManager.setPasswordResetCode(player, resetCode)
                            if (player.isLoggedIn) {
                                player.message(messages.email.emailSent)
                            } else {
                                player.kickWithMessage(messages.email.emailSent)
                            }
                        }
                    }
                    args.size == 3 -> {
                        val suppliedResetCode = args[1]
                        val newPassword = args[2]

                        if(!player.validatePassword(newPassword))
                            return

                        when (userManager.verifyPasswordReset(player, suppliedResetCode, newPassword)) {
                            LoginManager.PasswordResetVerifyResult.SUCCESS -> player.message(messages.email.passwordRecovered)
                            LoginManager.PasswordResetVerifyResult.EXPIRED -> player.message(messages.email.expiredPasswordResetCode)
                            LoginManager.PasswordResetVerifyResult.FAILED -> player.message(messages.email.invalidPasswordResetCode)
                        }
                    }
                    else -> player.message(messages.command.email)
                }
            }
            "verify" -> {
                if(!player.isLoggedIn) {
                    player.message(messages.login.mustLogin)
                    return
                }

                if (args.size < 2) {
                    player.message(messages.command.email)
                    return
                }

                if (player.emailAddress == null) {
                    player.message(messages.email.notSet)
                    return
                }

                if (player.isEmailVerified) {
                    player.message(messages.email.alreadyVerified)
                    return
                }

                val verificationCode = args[1]
                when (userManager.verifyEmail(player, verificationCode)) {
                    LoginManager.EmailVerifyResult.SUCCESS -> player.message(messages.email.verified)
                    LoginManager.EmailVerifyResult.EXPIRED -> player.message(messages.email.expiredVerificationCode)
                    LoginManager.EmailVerifyResult.ALREADY_VERIFIED -> player.message(messages.email.alreadyVerified)
                    LoginManager.EmailVerifyResult.FAILED -> player.message(messages.email.invalidVerificationCode)
                }
            }
            null -> {
                player.message(messages.command.email)
            }
            else -> {
                player.message(messages.command.email)
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, args: Array<out String>): Iterable<String> {
        if(sender !is ProxiedPlayer || !config.email.enabled)
            return emptyList()

        val availableCompletions = when {
            args.size <= 1 && sender.isRegistered && !sender.isLoggedIn && sender.isEmailVerified -> listOf("recover")
            args.size <= 1 && !sender.isEmailVerified -> listOf("add", "remove", "verify")
            args.size <= 1 -> listOf("add", "remove", "recover")
            else -> return emptyList()
        }

        val arg = args.last()
        return availableCompletions.filter { it.length >= arg.length && it.toLowerCase(Locale.ENGLISH).startsWith(arg) }.sorted()
    }
}