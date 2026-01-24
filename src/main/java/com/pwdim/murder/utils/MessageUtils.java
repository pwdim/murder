package com.pwdim.murder.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

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
}
