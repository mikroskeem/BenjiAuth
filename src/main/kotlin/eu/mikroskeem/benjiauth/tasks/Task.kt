/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.tasks

import java.util.concurrent.TimeUnit

/**
 * @author Mark Vainomaa
 */
interface Task: Runnable {
    val delay: Long
    val period: Long
    val timeUnit: TimeUnit

    override fun run()
}