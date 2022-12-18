package de.crafttogether.bungeedeathmessage.listener;

import de.crafttogether.bungeedeathmessage.BungeeDeathMessage;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import java.util.Objects;

public class PlayerAdvancementListener implements Listener {
    private static final BungeeDeathMessage plugin = BungeeDeathMessage.plugin;

    public PlayerAdvancementListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        AdvancementDisplay.Frame frame = Objects.requireNonNull(event.getAdvancement().getDisplay()).frame();

        Component component = switch (frame) {
            case CHALLENGE -> Component.translatable("chat.type.advancement.challenge", Component.text(player.getName()), event.getAdvancement().displayName());
            case GOAL -> Component.translatable("chat.type.advancement.goal", Component.text(player.getName()), event.getAdvancement().displayName());
            case TASK -> Component.translatable("chat.type.advancement.task", Component.text(player.getName()), event.getAdvancement().displayName());
        };

        plugin.send(component);
    }
}
