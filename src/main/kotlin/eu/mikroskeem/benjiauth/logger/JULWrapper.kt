/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.logger

import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Mark Vainomaa
 */
class JULWrapper(private val logger: Logger): PluginLogger {
    override fun info(message: String) = logger.info(message)

    override fun info(message: String, throwable: Throwable?) = logger.log(Level.INFO, message, throwable)

    override fun warn(message: String) = logger.info(message)

    override fun warn(message: String, throwable: Throwable?) = logger.log(Level.WARNING, message, throwable)

    override fun error(message: String) = logger.severe(message)

    override fun error(message: String, throwable: Throwable?) = logger.log(Level.SEVERE, message, throwable)
}