package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import net.md_5.bungee.api.ChatColor;

public class Weapons{
    
    public static ItemStack bow = bow();
    private static ItemStack bow() {
	ItemStack i = new ItemStack(Material.BOW);
	i.setItemMeta(ConfigHandler.bowMeta);
	return(i);
    }
    public static ItemStack endSword = endSword();
    private static ItemStack endSword() {
	ItemStack i = new ItemStack(Material.STONE_SWORD);
	ItemMeta m = i.getItemMeta();
	m.setDisplayName(ChatColor.DARK_AQUA + "Ender Raider");
	ArrayList<String> s = new ArrayList<String>();
	s.add(ChatColor.AQUA + "Standardized Ender Raid Sword");
	s.add(ChatColor.AQUA + "Complete with the engraved sign");
	s.add(ChatColor.AQUA + "Misues may be terms of termination");
	m.setLore(s);
	i.setItemMeta(m);
	return i;
    }
}
