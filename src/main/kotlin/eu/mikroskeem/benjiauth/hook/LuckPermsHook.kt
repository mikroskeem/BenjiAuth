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

package eu.mikroskeem.benjiauth.hook

import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.pluginManager
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.context.ContextCalculator
import net.luckperms.api.context.ContextConsumer
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Mark Vainomaa
 */
private const val AUTHENTICATED_CONTEXT_NAME = "authenticated"

private var luckPermsApi: LuckPerms? = null

object LuckPermsHook {
    var isHooked = false
        private set

    class BenjiAuthContextCalculator internal constructor(): ContextCalculator<ProxiedPlayer> {
        override fun calculate(target: ProxiedPlayer, consumer: ContextConsumer) {
            consumer.accept(AUTHENTICATED_CONTEXT_NAME, "${target.isLoggedIn}")
        }
    }

    fun hook() {
        pluginManager.getPlugin("LuckPerms") ?: return
        plugin.pluginLogger.info("LuckPerms found, hooking...")
        luckPermsApi = LuckPermsProvider.get()

        plugin.pluginLogger.info("Registering 'authenticated' context provider in LuckPerms...")
        luckPermsApi!!.contextManager.registerCalculator(BenjiAuthContextCalculator())

        plugin.pluginLogger.info("Hooked into LuckPerms!")
        isHooked = true
    }
}