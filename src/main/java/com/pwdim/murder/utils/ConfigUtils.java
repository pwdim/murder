package com.pwdim.murder.utils;


import com.pwdim.murder.Murder;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigUtils {
    private static Murder plugin;


    public ConfigUtils(Murder plugin) {
        this.plugin = plugin;
    }

    public static String consoleError(){
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("messages");
        String msg = section.getString("console-error", "&cVocê precisa ser um jogador!");

        return ColorUtil.color(msg);
    }

    public static String noPermMSG(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("messages");
        String msg = section.getString("no-perm", "&cSem permissão!");

        return ColorUtil.color(msg);
    }

    public static String alreadyLobby(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("messages");
        String msg = section.getString("already-lobby", "&cVocẽ já esta no Lobby!");

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

    public static String getGameMap(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("plugin");


        return section.getString("game-map", "academy");
    }

    public static Location getMapSpawn(){
        ConfigurationSection section = Bukkit.getPluginManager().getPlugin("Murder").getConfig().getConfigurationSection("plugin");
        List<Double> world = section.getDoubleList("map-spawn");
        double x = world.get(0);
        double y = world.get(1);
        double z = world.get(2);
        double yawD = world.get(4);
        float yaw = (float) yawD;
        double pitchD = world.get(3);
        float pitch = (float) pitchD;


        return new Location(Bukkit.getWorld(getGameMap()), x, y, z, yaw, pitch);
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

    public boolean inGame(Player p){
        if (plugin == null || plugin.getArenaManager() == null){
            return false;
        }
        return plugin.getArenaManager().searchPlayer(p);
    }
}
