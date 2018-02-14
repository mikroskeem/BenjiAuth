/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.tasks

import eu.mikroskeem.benjiauth.currentUnixTimestamp
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.startTask
import net.md_5.bungee.api.scheduler.ScheduledTask
import java.util.concurrent.TimeUnit

/**
 * @author Mark Vainomaa
 */
abstract class Task: Runnable {
    abstract val delay: Long
    abstract val period: Long
    abstract val timeUnit: TimeUnit

    private var scheduledTask: ScheduledTask? = null

    protected val taskStart by lazy { currentUnixTimestamp }

    fun schedule() = synchronized(this) {
        scheduledTask?.run { throw IllegalStateException("Task is already scheduled!") }

        scheduledTask = plugin.startTask(this)
    }

    fun cancel() = synchronized(this) {
        scheduledTask?.cancel() ?: throw IllegalStateException("Task is not scheduled!")
    }

    abstract override fun run()
}