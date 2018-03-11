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
import eu.mikroskeem.benjiauth.sendTitle
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.concurrent.TimeUnit

/**
 * @author Mark Vainomaa
 */
class LoginMessageTask(private val player: ProxiedPlayer): Task() {
    override val delay: Long = 0
    override val period: Long = 1
    override val timeUnit: TimeUnit = TimeUnit.SECONDS

    private val interval = config.authentication.loginNoticeInterval
    private var intervalCountdown = 0L
    private var timeout = config.authentication.authTimeout

    override fun run() {
        if(!player.isConnected || player.isLoggedIn) {
            cancel()
            return
        }

        if(timeout <= 0) {
            player.kickWithMessage(messages.login.loginTimeout)
            return
        }

        if(intervalCountdown <= 0) {
            player.message(messages.login.pleaseLogin)
            player.sendTitle(messages.login.pleaseLoginTitle.copy(fadeIn = 0, stay = (interval * 20) + 20, fadeOut = 0))
            intervalCountdown = interval
        }

        timeout--
        intervalCountdown--
    }
}