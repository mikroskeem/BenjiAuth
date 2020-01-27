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

package eu.mikroskeem.benjiauth.config.email

import eu.mikroskeem.benjiauth.config.email.providers.MailgunProviderSection
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class EmailSection {
    @Setting(value = "enabled", comment = "Whether e-mailing services for password recovery and confirmation should be used or not")
    var enabled = false
        // note: no private set, must be mutable in case
        // of invalid email provider configuration

    @Setting(value = "verification-code-length", comment = "Verification code length used in verification e-mail")
    var verificationCodeLength = 8
        get() {
            return max(field, 8) // To avoid dumb shit
        }
        private set

    @Setting(value = "verification-timeout", comment = "After how long will verification e-mail code expire? Value is in seconds and default is 24 hours")
    var verificationTimeout = TimeUnit.HOURS.toSeconds(24)
        private set

    @Setting(value = "password-reset-code-timeout", comment = "After how long will password reset code expire? Value is in seconds and default is 24 hours")
    var passwordResetCodeTimeout = TimeUnit.HOURS.toSeconds(24)
        private set

    @Setting(value = "verification-email-cooldown", comment = "How long should user wait before requesting a new e-mail verificaton? Due to current plugin design, cooldowns reset after proxy reboot")
    var verificationEmailCooldown = TimeUnit.MINUTES.toSeconds(15)
        private set

    @Setting(value = "password-reset-email-cooldown", comment = "How long should user wait before requesting a new password reset e-mail? Due to current plugin design, cooldowns reset after proxy reboot")
    var passwordResetEmailCooldown = TimeUnit.MINUTES.toSeconds(15)
        private set

    @Setting(value = "content", comment = "E-mail content setup")
    var content = ContentSection()
        private set

    @Setting(value = "provider", comment = "E-mail service provider used in this plugin\n" +
            "Currently supported providers:\n" +
            "- mailgun")
    var provider = "mailgun"
        private set

    @Setting(value = "mailgun-provider", comment = "Mailgun E-mail service provider")
    var mailgun = MailgunProviderSection()
        private set
}