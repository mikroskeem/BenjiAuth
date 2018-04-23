/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.commands.admin.BenjiAuthCommand
import eu.mikroskeem.benjiauth.commands.player.ChangePasswordCommand
import eu.mikroskeem.benjiauth.commands.player.LoginCommand
import eu.mikroskeem.benjiauth.commands.player.LogoutCommand
import eu.mikroskeem.benjiauth.commands.player.RegisterCommand
import eu.mikroskeem.benjiauth.config.Benji
import eu.mikroskeem.benjiauth.config.BenjiMessages
import eu.mikroskeem.benjiauth.config.ConfigurationLoader
import eu.mikroskeem.benjiauth.database.GeoIPDatabase
import eu.mikroskeem.benjiauth.database.UserManager
import eu.mikroskeem.benjiauth.hook.hookFastLogin
import eu.mikroskeem.benjiauth.hook.hookLuckPerms
import eu.mikroskeem.benjiauth.listeners.ChatListener
import eu.mikroskeem.benjiauth.listeners.PlayerLoginListener
import eu.mikroskeem.benjiauth.listeners.PlayerLoginStatusChangeListener
import eu.mikroskeem.benjiauth.logger.JULWrapper
import eu.mikroskeem.benjiauth.logger.PluginLogger
import eu.mikroskeem.benjiauth.logger.SLF4JLoggerWrapper
import net.md_5.bungee.api.plugin.Plugin
import org.slf4j.Logger
import java.net.InetAddress
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Mark Vainomaa
 */
class BenjiAuth: Plugin(), BenjiAuthPlugin, BenjiAuthAPI {
    private val pluginDataFolder: Path by lazy { Paths.get(dataFolder.absolutePath) }
    private lateinit var configLoader: ConfigurationLoader<Benji>
    private lateinit var messagesLoader: ConfigurationLoader<BenjiMessages>
    private lateinit var userManager: UserManager
    private lateinit var geoIPApi: GeoIPAPI

    override fun onEnable() {
        configLoader = initConfig("config.cfg")
        messagesLoader = initConfig("messages.cfg")
        userManager = UserManager()
        geoIPApi = try { GeoIPDatabase() } catch (e: Exception) {
            pluginLogger.warn("Failed to initialize MaxMind GeoLite database!", e)
            pluginLogger.warn("Falling back to no-op implementation for GeoIP lookups")
            object: GeoIPAPI {
                override fun getCountryByIP(ipAddress: InetAddress): String? = config.country.allowedCountries.firstOrNull()
                override fun getCountryByIP(ipAddress: String): String? = config.country.allowedCountries.firstOrNull()
            }
        }

        registerListener<ChatListener>()
        registerListener<PlayerLoginListener>()
        registerListener<PlayerLoginStatusChangeListener>()

        registerCommand<BenjiAuthCommand>()
        registerCommand<ChangePasswordCommand>()
        registerCommand<LoginCommand>()
        registerCommand<LogoutCommand>()
        registerCommand<RegisterCommand>()

        // Hook into FastLogin to skip authentication for online mode players
        hookFastLogin()

        // Hook into LuckPerms to manage authenticated context value
        hookLuckPerms()
    }

    override fun onDisable() = userManager.shutdown()

    override fun reloadConfig() {
        configLoader.load()
        configLoader.save()
        messagesLoader.load()
        messagesLoader.save()
    }

    private val lazyLogger by lazy {
        try {
            SLF4JLoggerWrapper(Plugin::class.java.getMethod("getSLF4JLogger").invoke(this) as Logger)
        } catch (e: NoSuchMethodException) {
            JULWrapper(logger)
        }
    }

    override fun getPluginLogger(): PluginLogger = lazyLogger
    override fun getPluginFolder(): Path = pluginDataFolder
    override fun getConfig(): Benji = configLoader.configuration
    override fun getMessages(): BenjiMessages = messagesLoader.configuration
    override fun getApi(): BenjiAuthAPI = this
    override fun getLoginManager(): LoginManager = userManager
    override fun getGeoIPAPI(): GeoIPAPI = geoIPApi
}