/*
 * This file is part of project BenjiAuth, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
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

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.commands.admin.BenjiAuthCommand
import eu.mikroskeem.benjiauth.commands.player.ChangePasswordCommand
import eu.mikroskeem.benjiauth.commands.player.EmailCommand
import eu.mikroskeem.benjiauth.commands.player.LoginCommand
import eu.mikroskeem.benjiauth.commands.player.LogoutCommand
import eu.mikroskeem.benjiauth.commands.player.RegisterCommand
import eu.mikroskeem.benjiauth.config.Benji
import eu.mikroskeem.benjiauth.config.BenjiMessages
import eu.mikroskeem.benjiauth.config.ConfigurationLoader
import eu.mikroskeem.benjiauth.database.UserManager
import eu.mikroskeem.benjiauth.email.EmailManager
import eu.mikroskeem.benjiauth.email.EmailManagerImpl
import eu.mikroskeem.benjiauth.email.NoopEmailManagerImpl
import eu.mikroskeem.benjiauth.hook.FastLoginHook
import eu.mikroskeem.benjiauth.hook.LuckPermsHook
import eu.mikroskeem.benjiauth.listeners.ChatListener
import eu.mikroskeem.benjiauth.listeners.PlayerLoginListener
import eu.mikroskeem.benjiauth.listeners.PlayerLoginStatusChangeListener
import eu.mikroskeem.benjiauth.listeners.ServerSwitchListener
import eu.mikroskeem.geoip.GeoIPAPI
import net.md_5.bungee.api.plugin.Plugin
import org.bstats.bungeecord.MetricsLite
import java.lang.reflect.Proxy
import java.net.InetAddress
import java.nio.file.Path
import java.nio.file.Paths
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author Mark Vainomaa
 */
class BenjiAuth: Plugin(), BenjiAuthPlugin, BenjiAuthAPI {
    private val pluginDataFolder: Path by lazy { Paths.get(dataFolder.absolutePath) }
    private lateinit var configLoader: ConfigurationLoader<Benji>
    private lateinit var messagesLoader: ConfigurationLoader<BenjiMessages>
    private lateinit var userManager: UserManager
    private lateinit var geoIPApi: GeoIPAPI
    private lateinit var emailManager: EmailManager

    override fun onEnable() {
        configLoader = initConfig("config.cfg")
        messagesLoader = initConfig("messages.cfg")
        userManager = try { UserManager() } catch (e: Exception) {
            pluginLogger.log(Level.SEVERE, "Failed to initialize connection to database!", e)
            pluginLogger.severe("Disabling plugin")
            return
        }
        geoIPApi = try { requireNotNull(GeoIPAPI.INSTANCE) } catch (e: Exception) {
            pluginLogger.log(Level.WARNING, "Failed to initialize MaxMind GeoLite database access!", e)
            pluginLogger.warning("Falling back to no-op implementation for GeoIP lookups")
            Proxy.newProxyInstance(this.javaClass.classLoader, arrayOf(GeoIPAPI::class.java)) { _, m, _ ->
                if (m.name == "getCountryByIP") {
                    return@newProxyInstance config.country.allowedCountries.firstOrNull()
                }
                return@newProxyInstance null
            } as GeoIPAPI
        }
        emailManager = try { if(config.email.enabled) EmailManagerImpl() else NoopEmailManagerImpl()
        } catch (e: Exception) {
            pluginLogger.log(Level.WARNING, "Failed to initialize e-mail service!", e)
            pluginLogger.warning("Falling back to no-op implementation and disabling e-mail usage")
            config.email.enabled = false
            NoopEmailManagerImpl()
        }

        registerListener<ChatListener>()
        registerListener<PlayerLoginListener>()
        registerListener<PlayerLoginStatusChangeListener>()
        registerListener<ServerSwitchListener>()

        registerCommand<BenjiAuthCommand>()
        registerCommand<ChangePasswordCommand>()
        registerCommand<LoginCommand>()
        registerCommand<LogoutCommand>()
        registerCommand<RegisterCommand>()
        registerCommand<EmailCommand>()

        // Statistics!
        MetricsLite(this)

        // Hook into FastLogin to skip authentication for online mode players
        FastLoginHook.hook()

        // Hook into LuckPerms to manage authenticated context value
        LuckPermsHook.hook()
    }

    override fun onDisable() { if (::userManager.isInitialized) userManager.shutdown() }

    override fun reloadConfig() {
        configLoader.load()
        configLoader.save()
        messagesLoader.load()
        messagesLoader.save()
    }

    override fun getPluginLogger(): Logger = this.logger
    override fun getPluginFolder(): Path = pluginDataFolder
    override fun getConfig(): Benji = configLoader.configuration
    override fun getMessages(): BenjiMessages = messagesLoader.configuration
    override fun getApi(): BenjiAuthAPI = this
    override fun getLoginManager(): LoginManager = userManager
    override fun getGeoIPAPI(): GeoIPAPI = geoIPApi
    override fun getEmailManager(): EmailManager = emailManager
}
