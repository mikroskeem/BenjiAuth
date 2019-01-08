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

    fun schedule() = synchronized(this) {
        scheduledTask?.run { throw IllegalStateException("Task is already scheduled!") }

        scheduledTask = plugin.startTask(this)
    }

    fun cancel() = synchronized(this) {
        scheduledTask?.cancel() ?: throw IllegalStateException("Task is not scheduled!")
    }

    abstract override fun run()
}