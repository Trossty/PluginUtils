package me.trossty.pluginutils;

import net.md_5.bungee.api.ChatColor;

public class Colorize {

    public static String format(String text){
        return ChatColor.translateAlternateColorCodes('&',text);
    }

    public static String strip(String text){
        return ChatColor.stripColor(text);
    }

}
