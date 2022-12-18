package de.crafttogether.bungeedeathmessage;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.crafttogether.bungeedeathmessage.listener.PlayerDeathListener;
import de.crafttogether.bungeedeathmessage.listener.PlayerAdvancementListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public final class BungeeDeathMessage extends JavaPlugin implements PluginMessageListener {
    public static BungeeDeathMessage plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        new PlayerDeathListener();
        new PlayerAdvancementListener();
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord"))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if (!subChannel.equals("BungeeDeathMessage"))
            return;

        short len = in.readShort();
        byte[] msgbytes = new byte[len];
        in.readFully(msgbytes);

        DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
        String gson = null;

        try {
            gson = msgin.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Component component = null;
        if (gson != null)
            component = GsonComponentSerializer.gson().deserialize(gson);

        if (component != null) {
            for (Player onlinePlayer : getServer().getOnlinePlayers())
                onlinePlayer.sendMessage(component);
        }
    }

    public void send(Component component) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        String gson = GsonComponentSerializer.gson().serialize(component);

        out.writeUTF("Forward");
        out.writeUTF("ALL");
        out.writeUTF("BungeeDeathMessage");

        ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
        DataOutputStream msgout = new DataOutputStream(msgbytes);
        try {
            msgout.writeUTF(gson);
        } catch (IOException e){
            e.printStackTrace();
        }

        out.writeShort(msgbytes.toByteArray().length);
        out.write(msgbytes.toByteArray());

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}
