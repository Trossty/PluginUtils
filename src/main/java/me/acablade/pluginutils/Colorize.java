package me.acablade.pluginutils;

import org.bukkit.ChatColor;

public class Colorize {

    public static String format(String text){
        return ChatColor.translateAlternateColorCodes('&',text);
    }

    public static String strip(String text){
        return ChatColor.stripColor(text);
    }

}
