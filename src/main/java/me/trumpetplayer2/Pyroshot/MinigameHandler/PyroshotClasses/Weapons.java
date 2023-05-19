package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
    public static final ItemStack Machinegun = machineGun();
    public static ItemStack machineGun() {
        ItemStack MachineGun = new ItemStack(Material.CROSSBOW);
        ItemMeta MachMeta = MachineGun.getItemMeta();
        MachMeta.setDisplayName(ChatColor.DARK_AQUA + "Machine Gun");
        MachMeta.addEnchant(Enchantment.QUICK_CHARGE, 5, true);
        ArrayList<String> s = new ArrayList<String>();
        s.add(ChatColor.AQUA + "Standard Issue Machine Gun");
        s.add(ChatColor.AQUA + "Sponsored by Zhop Corp");
        s.add(ChatColor.AQUA + "Don't miss soldier");
        MachMeta.setLore(s);
        MachineGun.setItemMeta(MachMeta);
        return MachineGun;
    }
}
