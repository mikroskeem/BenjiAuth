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

package eu.mikroskeem.benjiauth.tasks

import eu.mikroskeem.benjiauth.kickWithMessage
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.messages
import eu.mikroskeem.benjiauth.sendTitle
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.concurrent.TimeUnit

/**
 * @author Mark Vainomaa
 */
class RegisterMessageTask(private val player: ProxiedPlayer): Task() {
    override val delay: Long = 0
    override val period: Long = 1
    override val timeUnit: TimeUnit = TimeUnit.SECONDS

    private val interval = config.registration.registerNoticeInterval
    private var intervalCountdown = 0L
    private var timeout = config.authentication.authTimeout

    override fun run() {
        if(!player.isConnected || player.isLoggedIn) {
            cancel()
            return
        }

        if(timeout <= 0) {
            player.kickWithMessage(messages.register.registerTimeout)
            return
        }

        if(intervalCountdown <= 0) {
            player.message(messages.register.pleaseRegister)
            player.sendTitle(messages.register.pleaseRegisterTitle.copy(fadeIn = 0, stay = (interval * 20) + 20, fadeOut = 0))
            intervalCountdown = interval
        }

        timeout--
        intervalCountdown--
    }
}