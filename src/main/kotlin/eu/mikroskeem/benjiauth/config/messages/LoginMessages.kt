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

package eu.mikroskeem.benjiauth.config.messages

import eu.mikroskeem.benjiauth.Title
import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

/**
 * @author Mark Vainomaa
 */
@ConfigSerializable
class LoginMessages {
    @Setting(value = "please-login", comment = "This message gets sent to player repeatedly until one logs in")
    var pleaseLogin = "{prefix}Log in using command &c&l/login &8<&cpassword&8>"
        private set

    @Setting(value = "please-login-title", comment = "This message gets sent to player repeatedly until one gets" +
            "registered. Set both title and subtitle empty to avoid sending it. Timings will be ignored")
    var pleaseLoginTitle = Title("", "")
        private set

    @Setting(value = "must-login", comment = "This message gets sent when player is doing something while not " +
            "being logged in")
    var mustLogin = "{prefix}&cYou must log in first!"
        private set

    @Setting(value = "must-login-before-chatting", comment = "This message gets sent when player is chatting while not " +
            "being logged in")
    var mustLoginBeforeChatting = "{prefix}&cYou must log in before chatting!"
        private set

    @Setting(value = "must-login-before-using-commands", comment = "This message gets sent when player is using " +
            " commands while not being logged in")
    var mustLoginBeforeUsingCommands = "{prefix}&cYou must log in before using commands!"
        private set

    @Setting(value = "must-login-before-switching-server", comment = "This message gets sent when player is trying " +
            " to switch servers while not being logged in")
    var mustLoginBeforeSwitchingServers = "{prefix}&cYou must log in before switching servers!"
        private set

    @Setting(value = "login-timeout", comment = "This message gets sent when player doesn't log in fast enough " +
            "(iow hits login timeout)")
    var loginTimeout = "{prefix}&cYou didn't log in fast enough!"
        private set

    @Setting(value = "logged-in", comment = "This message gets sent when player logs in successfully")
    var loggedIn = "{prefix}You are logged in!"
        private set

    @Setting(value = "already-logged-in", comment = "This message gets sent when player is already logged in")
    var alreadyLoggedIn = "{prefix}&cYou are already logged in!"
        private set

    @Setting(value = "logged-out", comment = "This message gets sent when player is logged out successfully")
    var loggedOut = "{prefix}You are logged out!"
        private set
}