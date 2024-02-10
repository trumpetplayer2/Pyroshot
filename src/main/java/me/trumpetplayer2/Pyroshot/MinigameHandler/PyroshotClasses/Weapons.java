package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import net.md_5.bungee.api.ChatColor;

public class Weapons{
    
    public static ItemStack bow(Player p) {
	ItemStack i = new ItemStack(Material.BOW);
	ItemMeta m = i.getItemMeta();
	
	NamespacedKey key = new NamespacedKey(PyroshotMain.getInstance(), "Pyroshot");
	m.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
	m.setUnbreakable(true);
	
	m.setDisplayName(ChatColor.DARK_RED + Localize(p, "bowdisplayname"));
	
	ArrayList<String> lore = new ArrayList<String>();
	lore.add(ChatColor.RED + Localize(p, "bowloreone"));
	lore.add(ChatColor.RED + Localize(p, "bowloretwo"));
	lore.add(ChatColor.RED + Localize(p, "bowlorethree"));
	
	m.setLore(lore);
	
	m.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
	m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	
	
	i.setItemMeta(m);
	
	return(i);
    }
    
    public static ItemStack endSword(Player p) {
	ItemStack i = new ItemStack(Material.STONE_SWORD);
	ItemMeta m = i.getItemMeta();
	
	m.setUnbreakable(true);
	m.setDisplayName(ChatColor.DARK_AQUA + Localize(p, "endkitswordname"));
	ArrayList<String> s = new ArrayList<String>();
	s.add(ChatColor.AQUA + Localize(p, "endkitswordloreone"));
	s.add(ChatColor.AQUA + Localize(p, "endkitswordloretwo"));
	s.add(ChatColor.AQUA + Localize(p, "endkitswordlorethree"));
	m.setLore(s);
	i.setItemMeta(m);
	return i;
    }
    
    public static ItemStack machineGun(Player p) {
        ItemStack MachineGun = new ItemStack(Material.CROSSBOW);
        ItemMeta m = MachineGun.getItemMeta();

        NamespacedKey key = new NamespacedKey(PyroshotMain.getInstance(), "Pyroshot");
        m.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
        
        m.setUnbreakable(true);
        
        m.setDisplayName(ChatColor.DARK_AQUA + Localize(p, "barragekitcrossbowname"));
        m.addEnchant(Enchantment.QUICK_CHARGE, 5, true);
        m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ArrayList<String> s = new ArrayList<String>();
        s.add(ChatColor.AQUA + Localize(p, "barragekitcrossbowloreone"));
        s.add(ChatColor.AQUA + Localize(p, "barragekitcrossbowloretwo"));
        s.add(ChatColor.AQUA + Localize(p, "barragekitcrossbowlorethree"));
        m.setLore(s);
        
        MachineGun.setItemMeta(m);
        return MachineGun;
    }
    
    public static ItemStack raidPearl(Player p) {
      ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
      ItemMeta m = pearl.getItemMeta();
      
      //Set up the name and lore of the pearl
      m.setDisplayName(ChatColor.DARK_AQUA + Localize(p, "endkitpearlname"));
      List<String> lore = new ArrayList<String>();
      lore.add(ChatColor.AQUA + Localize(p, "endkitpearlloreone"));
      lore.add(ChatColor.AQUA + Localize(p, "endkitpearlloretwo"));
      lore.add(ChatColor.AQUA + Localize(p, "endkitpearllorethree"));
      m.setLore(lore);
      //Set the items meta to the meta and return the itemstack
      pearl.setItemMeta(m);
      return pearl;
    }
    
    static String Localize(Player p, String Key) {
        return PyroshotMain.getInstance().getLocalizedText(p, Key);
    }
}
