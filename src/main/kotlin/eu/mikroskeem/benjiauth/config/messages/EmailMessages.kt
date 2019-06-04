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

package eu.mikroskeem.benjiauth.config.messages

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class EmailMessages {
    @Setting(value = "not-enabled", comment = "Sent when player invokes /email while e-mail system is disabled")
    var notEnabled = "{prefix}&cE-mail system is not enabled"
        private set

    @Setting(value = "invalid-address", comment = "Sent when player inserts an invalid e-mail address")
    var invalidAddress = "{prefix}&cInvalid e-mail address"
        private set

    @Setting(value = "invalid-password-reset-code", comment = "Sent when player uses invalid password reset code")
    var invalidPasswordResetCode = "{prefix}&cInvalid password reset code!"
        private set

    @Setting(value = "invalid-verification-code", comment = "Sent when player uses invalid e-mail verification code")
    var invalidVerificationCode = "{prefix}&cInvalid verification code!"
        private set

    @Setting(value = "expired-password-reset-code", comment = "Sent when player tries to reset their password while validation code has been expired")
    var expiredPasswordResetCode = "{prefix}&cPassword reset code expired, you need to try to recovery your password again!"
        private set

    @Setting(value = "expired-verification-code", comment = "Sent when player tries to validate their e-mail while validation code has been expired")
    var expiredVerificationCode = "{prefix}&cVerification code expired, you need to add your e-mail address again!"
        private set

    @Setting(value = "something-went-wrong", comment = "Sent when an internal error happens while sending an e-mail")
    var somethingWentWrong = "{prefix}&cSomething went wrong while attempting to send an e-mail, please contact with server administrators"
        private set

    @Setting(value = "address-verified", comment = "Sent after player successfully verifies their e-mail address")
    var verified = "{prefix}E-mail address verified!"
        private set

    @Setting(value = "address-not-verified", comment = "Sent when player's e-mail address is not verified")
    var notVerified = "{prefix}&cYour e-mail address is not verified!"
        private set

    @Setting(value = "email-add-cooldown", comment = "Sent when player tries to add e-mail address too frequently")
    var emailAddCooldown = "{prefix}&cYou need to wait before trying to add an e-mail address again"
        private set

    @Setting(value = "password-reset-cooldown", comment = "Sent when player tries to recover their password too frequently")
    var passwordRecoveryCooldown = "{prefix}&cYou need to wait before trying to recover your password again"
        private set

    @Setting(value = "address-not-set", comment = "Sent when player attempts to recover a password or unset it while e-mail address is not set")
    var notSet = "{prefix}&cE-mail address is not set!"
        private set

    @Setting(value = "address-unset", comment = "Set when player removes their e-mail address successfully")
    var unset = "{prefix}E-mail address unset"
        private set

    @Setting(value = "address-already-set", comment = "Sent when player tries to add their e-mail address twice")
    var alreadySet = "{prefix}E-mail address is already set!"
        private set

    @Setting(value = "address-already-verified", comment = "Sent when player tries to verify their e-mail address twice")
    var alreadyVerified = "{prefix}E-mail address is already verified!"
        private set

    @Setting(value = "unset-email-address-before-setting-new", comment = "Sent when player tries to add new e-mail address without removing previous one")
    var unsetBeforeSettingNew = "{prefix}Unset current email with &c/email remove &7before using this command again"
        private set

    @Setting(value = "sending-email", comment = "Sent when plugin is sending out an e-mail")
    var sendingEmail = "{prefix}Sending e-mail..."
        private set

    @Setting(value = "email-sent", comment = "Sent when e-mail is sent")
    var emailSent = "{prefix}E-mail sent. Wait until it arrives and follow its directions."
        private set

    @Setting(value = "password-recovered", comment = "Sent when player recovers their password successfully")
    var passwordRecovered = "{prefix}Password recovered and changed successfully!"
        private set
}