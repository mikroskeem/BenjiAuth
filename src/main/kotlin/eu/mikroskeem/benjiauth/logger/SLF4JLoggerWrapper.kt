/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.logger

import org.slf4j.Logger

/**
 * @author Mark Vainomaa
 */
class SLF4JLoggerWrapper(private val logger: Logger): PluginLogger {
    override fun info(message: String) = logger.info(message)

    override fun info(message: String, throwable: Throwable?) = logger.info(message, throwable)

    override fun warn(message: String) = logger.warn(message)

    override fun warn(message: String, throwable: Throwable?) = logger.warn(message, throwable)

    override fun error(message: String) = logger.error(message)

    override fun error(message: String, throwable: Throwable?) = logger.error(message, throwable)
}