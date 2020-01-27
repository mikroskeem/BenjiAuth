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

package eu.mikroskeem.benjiauth.hook

import com.github.games647.fastlogin.bungee.FastLoginBungee
import com.github.games647.fastlogin.core.hooks.AuthPlugin
import eu.mikroskeem.benjiauth.isRegistered
import eu.mikroskeem.benjiauth.loginWithoutPassword
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.pluginManager
import eu.mikroskeem.benjiauth.register
import eu.mikroskeem.benjiauth.userManager
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Mark Vainomaa
 */
object FastLoginHook {
    var isHooked = false
        private set

    // Hooks BenjiAuth to FastLogin
    fun hook() {
        val fastLogin = pluginManager.getPlugin("FastLogin") ?: return
        plugin.pluginLogger.info("FastLogin found, hooking...")
        (fastLogin as FastLoginBungee).core.authPluginHook = ActualFastLoginHook
        plugin.pluginLogger.info("Hooked into FastLogin!")
        isHooked = true
    }

    object ActualFastLoginHook: AuthPlugin<ProxiedPlayer> {
        override fun isRegistered(player: String): Boolean {
            return userManager.isRegistered(player)
        }

        override fun forceLogin(player: ProxiedPlayer): Boolean {
            return if (player.isRegistered) {
                player.loginWithoutPassword(forceful = true)
                true
            } else {
                false
            }
        }

        override fun forceRegister(player: ProxiedPlayer, password: String): Boolean {
            return if (!player.isRegistered) {
                player.register(password)
                true
            } else {
                false
            }
        }
    }
}