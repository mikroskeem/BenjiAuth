package eu.mikroskeem.benjiauth.listeners

import eu.mikroskeem.benjiauth.events.PlayerLogoutEvent
import eu.mikroskeem.benjiauth.events.PlayerUnregisterEvent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.scheduler.ScheduledTask
import net.md_5.bungee.event.EventHandler
import java.util.WeakHashMap

/**
 * @author Mark Vainomaa
 */
class PlayerLoginStatusChangeListener: Listener {
    @EventHandler
    fun on(event: PlayerLogoutEvent) {

    }

    @EventHandler
    fun on(event: PlayerUnregisterEvent) {

    }
}