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