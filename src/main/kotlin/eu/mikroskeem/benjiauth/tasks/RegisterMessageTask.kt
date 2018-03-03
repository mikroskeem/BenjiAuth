/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.tasks

import eu.mikroskeem.benjiauth.kickWithMessage
import eu.mikroskeem.benjiauth.message
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.messages
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
            intervalCountdown = interval
        }

        timeout--
        intervalCountdown--
    }
}