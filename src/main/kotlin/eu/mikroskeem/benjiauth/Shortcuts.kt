/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.config.Benji
import eu.mikroskeem.benjiauth.config.BenjiMessages
import eu.mikroskeem.benjiauth.database.UserManager
import eu.mikroskeem.benjiauth.database.models.User
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.PluginManager

/**
 * @author Mark Vainomaa
 */
val proxy: ProxyServer get() = ProxyServer.getInstance()
val pluginManager: PluginManager get() = proxy.pluginManager
val plugin: BenjiAuth get() = pluginManager.getPlugin("BenjiAuth") as BenjiAuth
val userManager: UserManager get() = plugin.userManager

val config: Benji get() = plugin.configLoader.configuration
val messages: BenjiMessages get() = plugin.messagesLoader.configuration

val ProxiedPlayer.isRegistered: Boolean get() = userManager.findUser(name) != null
val ProxiedPlayer.loggedInSince: Long? get() = userManager.isLoggedIn(this)
val ProxiedPlayer.isLoggedIn: Boolean get() = loggedInSince != null

fun ProxiedPlayer.login(password: String): Boolean = userManager.loginUser(this, password)
fun ProxiedPlayer.register(password: String): User = userManager.registerUser(this, password)
fun ProxiedPlayer.logout() = userManager.logoutUser(this)