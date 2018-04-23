/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.hook

import eu.mikroskeem.benjiauth.isLoggedIn
import eu.mikroskeem.benjiauth.plugin
import eu.mikroskeem.benjiauth.pluginManager
import me.lucko.luckperms.LuckPerms
import me.lucko.luckperms.api.LuckPermsApi
import me.lucko.luckperms.api.context.ContextCalculator
import me.lucko.luckperms.api.context.MutableContextSet
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * @author Mark Vainomaa
 */
private const val AUTHENTICATED_CONTEXT_NAME = "authenticated"

private var luckPermsApi: LuckPermsApi? = null

fun hookLuckPerms() {
    pluginManager.getPlugin("LuckPerms") ?: return
    plugin.pluginLogger.info("LuckPerms found, hooking...")
    luckPermsApi = LuckPerms.getApiSafe().orElseGet {
        plugin.pluginLogger.warn("Failed to hook into LuckPerms: Couldn't get API interface instance")
        return@orElseGet null
    } ?: return

    plugin.pluginLogger.info("Registering 'authenticated' context provider in LuckPerms")
    luckPermsApi?.registerContextCalculator(BenjiAuthContextCalculator())

    plugin.pluginLogger.info("Hooked into LuckPerms!")
}

class BenjiAuthContextCalculator: ContextCalculator<ProxiedPlayer> {
    override fun giveApplicableContext(subject: ProxiedPlayer, accumulator: MutableContextSet): MutableContextSet {
        if(accumulator.containsKey(AUTHENTICATED_CONTEXT_NAME)) {
            accumulator.removeAll(AUTHENTICATED_CONTEXT_NAME)
        }

        accumulator.add(AUTHENTICATED_CONTEXT_NAME, "${subject.isLoggedIn}")

        return accumulator
    }
}