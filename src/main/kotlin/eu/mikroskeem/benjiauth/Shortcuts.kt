/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth

import eu.mikroskeem.benjiauth.config.Benji
import eu.mikroskeem.benjiauth.config.BenjiMessages
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.PluginManager

/**
 * @author Mark Vainomaa
 */
val proxy: ProxyServer get() = ProxyServer.getInstance()
val pluginManager: PluginManager get() = proxy.pluginManager
val plugin: BenjiAuthPlugin get() = pluginManager.getPlugin("BenjiAuth") as BenjiAuthPlugin
val userManager: LoginManager get() = plugin.api.loginManager

val config: Benji get() = plugin.config
val messages: BenjiMessages get() = plugin.messages

val ProxiedPlayer.isRegistered: Boolean get() = userManager.isRegistered(this)
val ProxiedPlayer.isLoggedIn: Boolean get() = userManager.isLoggedIn(this)
val ProxiedPlayer.isEgilibleForSession: Boolean get() = userManager.isEgilibleForSessionLogin(this)
val ProxiedPlayer.isForcefullyLoggedIn: Boolean get() = userManager.isForcefullyLoggedIn(this)

fun ProxiedPlayer.checkPassword(password: String) = userManager.checkPassword(this, password)
fun ProxiedPlayer.login(password: String): Boolean = if(checkPassword(password)) { userManager.loginUser(this); true } else false
fun ProxiedPlayer.loginWithoutPassword(forceful: Boolean = false) = userManager.loginUser(this, forceful)
fun ProxiedPlayer.register(password: String) = userManager.registerUser(this, password)
fun ProxiedPlayer.changePassword(newPassword: String) = userManager.changePassword(this, newPassword)
fun ProxiedPlayer.logout() = userManager.logoutUser(this)