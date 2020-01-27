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

package eu.mikroskeem.benjiauth.email

import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.config.email.content.CommonContentOptions
import eu.mikroskeem.benjiauth.email.providers.MailgunEmailService
import eu.mikroskeem.benjiauth.emailAddress
import eu.mikroskeem.benjiauth.verifiedEmailAddress
import eu.mikroskeem.benjiauth.plugin
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

/**
 * @author Mark Vainomaa
 */
class EmailManagerImpl: EmailManager {
    private val provider: EmailService

    init {
        provider = when (config.email.provider) {
            "mailgun" -> MailgunEmailService().apply { initialize() }
            else -> throw IllegalStateException("Unknown provider: ${config.email.provider}")
        }

        // Validate e-mail content settings
        require(config.email.content.verificationEmail.isValid) { "Verification e-mail configuration is not valid. Please check subject and e-mail body path" }
        require(config.email.content.recoveryEmail.isValid) { "Recovery e-mail configuration is not valid. Please check subject and e-mail body path" }
    }

    override fun sendVerificationEmail(player: ProxiedPlayer, verificationCode: String): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync {
            sendEmail(player, player.emailAddress, config.email.content.verificationEmail, verificationCode)

            return@supplyAsync null
        }
    }

    override fun sendPasswordRecoveryEmail(player: ProxiedPlayer, verificationCode: String): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync {
            sendEmail(player, player.verifiedEmailAddress, config.email.content.recoveryEmail, verificationCode)

            return@supplyAsync null
        }
    }

    private fun sendEmail(player: ProxiedPlayer, emailAddress: String?, content: CommonContentOptions, verificationCode: String) {
        emailAddress ?: throw IllegalStateException("Player's e-mail address is not present or not verified")
        val subject = content.subject
                .replace("{player_name}", player.name)

        val emailContent = readToString(plugin.pluginFolder.resolve("email").resolve(content.source))
                .replace("{player_name}", player.name)
                .replace("{player_uuid}", player.uniqueId.toString())
                .replace("{verification_code}", verificationCode)

        provider.sendEmail(listOf(emailAddress), subject, EmailService.EmailBody(content.sourceTypeValue, emailContent))
    }

    private fun readToString(path: Path): String {
        return Files.newBufferedReader(path).use {
            it.readLines().joinToString(separator = "\n")
        }
    }
}