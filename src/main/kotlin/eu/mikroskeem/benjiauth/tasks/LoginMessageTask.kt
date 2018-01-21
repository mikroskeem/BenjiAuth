/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.tasks

import eu.mikroskeem.benjiauth.authMessage
import eu.mikroskeem.benjiauth.config
import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.messages
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.concurrent.TimeUnit

/**
 * @author Mark Vainomaa
 */
class LoginMessageTask(private val player: ProxiedPlayer): Task {
    override val delay: Long = 0
    override val period: Long get() = config.authentication.loginNoticeInterval
    override val timeUnit: TimeUnit = TimeUnit.SECONDS

    override fun run() {
        if(!player.isLoggedIn) {
            player.authMessage(messages.login.pleaseLogin)
        }
    }
}