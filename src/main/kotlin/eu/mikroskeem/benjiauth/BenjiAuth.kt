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
import eu.mikroskeem.benjiauth.database.UserManager
import eu.mikroskeem.benjiauth.hook.hookFastLogin
import eu.mikroskeem.benjiauth.listeners.ChatListener
import eu.mikroskeem.benjiauth.listeners.PlayerLoginListener
import net.md_5.bungee.api.plugin.Plugin
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Mark Vainomaa
 */
class BenjiAuth: Plugin() {
    val pluginDataFolder: Path by lazy { Paths.get(dataFolder.absolutePath) }
    internal lateinit var configLoader: ConfigurationLoader<Benji>
    internal lateinit var messagesLoader: ConfigurationLoader<BenjiMessages>
    internal lateinit var userManager: UserManager

    override fun onEnable() {
        configLoader = initConfig("config.cfg")
        messagesLoader = initConfig("messages.cfg")
        userManager = UserManager()

        registerListener(PlayerLoginListener::class)
        registerListener(ChatListener::class)

        registerCommand(BenjiAuthCommand::class)
        registerCommand(LoginCommand::class)
        registerCommand(LogoutCommand::class)
        registerCommand(ChangePasswordCommand::class)
        registerCommand(RegisterCommand::class)

        hookFastLogin()
    }

    override fun onDisable() {
        userManager.shutdown()
    }
}