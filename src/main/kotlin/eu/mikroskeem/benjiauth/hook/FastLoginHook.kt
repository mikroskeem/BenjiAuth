/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.hook

import com.github.games647.fastlogin.bungee.FastLoginBungee
import com.github.games647.fastlogin.core.hooks.AuthPlugin
import eu.mikroskeem.benjiauth.pluginManager
import eu.mikroskeem.benjiauth.userManager
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Mark Vainomaa
 */
class FastLoginHook: AuthPlugin<ProxiedPlayer> {
    override fun isRegistered(player: String): Boolean {
        return userManager.findUser(player) != null
    }

    override fun forceLogin(player: ProxiedPlayer): Boolean {
        userManager.setLoggedIn(player)
        return true
    }

    override fun forceRegister(player: ProxiedPlayer, password: String): Boolean {
        userManager.registerUser(player, password)
        return true
    }
}

// Hooks BenjiAuth to FastLogin
fun hookFastLogin() {
    val fastLogin = pluginManager.getPlugin("FastLogin") ?: return
    (fastLogin as FastLoginBungee).core.authPluginHook = FastLoginHook()
}