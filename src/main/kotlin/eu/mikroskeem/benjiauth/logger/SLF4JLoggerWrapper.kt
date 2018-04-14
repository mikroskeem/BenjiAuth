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
class SLF4JLoggerWrapper(private val logger: Logger): PluginLogger, Logger by logger