/*
 * Copyright (c) 2018 Mark Vainomaa
 *
 * This source code is proprietary software and must not be distributed and/or copied without the express permission of Mark Vainomaa
 */

package eu.mikroskeem.benjiauth.commands.admin

import eu.mikroskeem.benjiauth.COMMAND_BENJIAUTH
import eu.mikroskeem.benjiauth.authMessage
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

/**
 * @author Mark Vainomaa
 */
class BenjiAuthCommand: Command("benjiauth", COMMAND_BENJIAUTH) {
    override fun execute(sender: CommandSender, args: Array<out String>) {
        sender.authMessage("Not done yet, annoy @mikroskeem with it")
    }
}