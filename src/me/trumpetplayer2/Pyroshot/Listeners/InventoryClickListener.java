package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;

public class InventoryClickListener implements Listener{
    PyroshotMain plugin;
    public InventoryClickListener(PyroshotMain main) {
	plugin = main;
    }
    
    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
	//Determine who clicked
	Player p = (Player) e.getWhoClicked();
	PlayerStats s = plugin.PlayerMap.get(p);
	ItemStack i = e.getCurrentItem();
	//No modifying inventories mid game
	if(plugin.game.isActive) {e.setCancelled(true); return;}
	//Check if the title is valid, and if the item is valid
	if(!(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Kits") 
		|| e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Map Vote"))) {
		return;
	}
	if(e.getCurrentItem() == null) return;
	if(e.getCurrentItem().getItemMeta() == null) return;
	if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
	//Cancel the event, no stealing
	e.setCancelled(true);
	//Run appropriate method based on the name of the gui
	if((e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Kits"))){
	    kitClick(s, i, p);
	}
	if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Map Vote")){
	    mapVote(p, e.getSlot(), i);
	}
	
    }
    
    public void kitClick(PlayerStats s, ItemStack i, Player p) {
	//Check if user has permission
	if(!p.hasPermission("Pyroshot.Minigame.SelectKit")) {return;}
	//Determine kit by removing the " kit" at the end
	String n = i.getItemMeta().getDisplayName();
	n = ChatColor.stripColor(n);
	n = n.substring(0, n.length()-4);
	//Set the players kit
	s.setKit(Kit.kitFromString(n));
	//Determine the kit name WITH color
	String kitName = i.getItemMeta().getDisplayName().substring(0, i.getItemMeta().getDisplayName().length()-4);
	//If player has permission, save kit change, otherwise give invalid permission for kit
	if(s.getKit().hasPermission(p)) {
	p.sendMessage(ChatColor.GOLD + "Selected " + ChatColor.BOLD + kitName + ChatColor.RESET + "" + ChatColor.GOLD + " Kit!");
	plugin.PlayerMap.put(p, s);
	}else {
	    p.sendMessage(ChatColor.DARK_RED + "Invalid permissions for " + kitName + ChatColor.RESET + "" + ChatColor.DARK_RED + " Kit.");
	}
    }
    
    public void mapVote(Player whoClicked, int Slot, ItemStack item) {
	//Add the vote to count
	plugin.game.addVote(whoClicked, Slot);
	whoClicked.sendMessage(ChatColor.DARK_AQUA + "Voted for " + item.getItemMeta().getDisplayName());
    }
}
