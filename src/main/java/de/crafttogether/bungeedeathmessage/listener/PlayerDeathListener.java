package de.crafttogether.bungeedeathmessage.listener;

import de.crafttogether.bungeedeathmessage.BungeeDeathMessage;
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
        plugin.send(event.deathMessage());
    }
}
