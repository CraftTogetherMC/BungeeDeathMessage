package de.crafttogether.bungeedeathmessage.listener;

import de.crafttogether.bungeedeathmessage.BungeeDeathMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private static BungeeDeathMessage plugin = BungeeDeathMessage.plugin;

    public PlayerDeathListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Component component = event.deathMessage();

        if (component == null)
            return;

        Component hover = Component.text(event.getPlayer().getName())
                .append(Component.newline())
                .append(Component.text("Server: " + plugin.getConfig().getString("serverName")));
        component = component.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, hover));

        plugin.send(component);
    }
}
