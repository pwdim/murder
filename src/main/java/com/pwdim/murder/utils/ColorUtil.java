package com.pwdim.murder.utils;

import org.bukkit.ChatColor;


public class ColorUtil {


    public static String color(String s){

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String color(String s, Object... args){
        String msg = String.format(s, args);

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
