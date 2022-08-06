package me.trumpetplayer2.Pyroshot.Debug;

import org.bukkit.Bukkit;

import me.trumpetplayer2.Pyroshot.ConfigHandler;

public class Debug {
    //Sends a provided message to the console
    public static void TellConsole(String s) {
	if(ConfigHandler.debug) {
	    Bukkit.getServer().getConsoleSender().sendMessage(ConfigHandler.pluginAnnouncement + s);
	}
    }
    public static double getNMSVersion(){
        String v = Bukkit.getServer().getClass().getPackage().getName();
        v = v.substring(v.lastIndexOf('.') + 1);
        String nmsShort = v.substring(0, v.length() - 3);
        switch(nmsShort){
        case "v_1_7":
            return 1.7;
        case "v1_8":
            return 1.8;
        case "v1_9":
            return 1.9;
        case "v1_10":
            return 1.10;
        case "v1_11":
            return 1.11;
        case "v1_12":
            return 1.12;
        case "v1_13":
            return 1.13;
        case "v1_14":
            return 1.14;
        case "v1_15":
            return 1.15;
        case "v1_16":
            return 1.16;
        case "v1_17":
            return 1.17;
        case "v1_18":
            return 1.18;
        case "v1_19":
            return 1.19;
        default:
            TellConsole("Verion " + nmsShort + " is an unknown version of spigot. Assuming the version is valid");
            nmsShort.replace('_', '.');
            nmsShort = nmsShort.substring(1);
            try {
            return Double.parseDouble(nmsShort);
            }catch(NumberFormatException ex) {
                TellConsole("Invalid version, defaulting to plugin version");
                return 1.19;
            }
        }
    }
}
