package com.pwdim.murder.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static String consoleError(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("messsages");
        String msg = section.getString("console-error", "&cVocê precisa ser um jogador!");

        return ColorUtil.color(msg);
    }

    public static String noPermMSG(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("messsages");
        String msg = section.getString("no-perm", "&cVocê precisa ser um jogador!");

        return ColorUtil.color(msg);
    }

    public static Integer timeLeft(){

        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("plugin");

        return section.getInt("timeLeft", 30);
    }

    public static Location getLobby(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("plugin");
        String world = section.getString("lobby-world");

        return Bukkit.getWorld(world).getSpawnLocation();
    }

    public static Integer getMinPlayers(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("plugin");

        return section.getInt("min-players", 2);
    }

    public static Integer getMaxPLayers(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("plugin");

        return section.getInt("max-players", 16);
    }

    public static void sendTitle(Player p, String titulo, String subtitulo, int fadeIn, int stay, int fadeOut){
        CraftPlayer craftPlayer = (CraftPlayer) p;
        PlayerConnection connection = craftPlayer.getHandle().playerConnection;

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, null, fadeIn, stay, fadeOut);
        connection.sendPacket(title);

        if (titulo != null){
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ColorUtil.color(titulo) + "\"}");
            PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetTitle);
        }
        if (subtitulo != null) {
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ColorUtil.color(subtitulo) + "\"}");
            PacketPlayOutTitle packetSub = new PacketPlayOutTitle(
                    PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetSub);
        }
    }
}
