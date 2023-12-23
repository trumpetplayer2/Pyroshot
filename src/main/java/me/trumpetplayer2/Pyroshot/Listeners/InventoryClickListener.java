package me.trumpetplayer2.Pyroshot.Listeners;

import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Commands.PyroshotCommand;
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
	PlayerStats s = plugin.getPlayerStats(p);
	ItemStack i = e.getCurrentItem();
	//No modifying inventories mid game
	if(plugin.game.isActive) {e.setCancelled(true); return;}
	//Check if the title is valid, and if the item is valid
	if(!(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Kits") 
		|| e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Map Vote") 
		|| e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Elimination Messages")
		|| e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Win Effect")
		|| e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Death Effect"))) {
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
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3f); 
	    mapVote(p, e.getSlot(), i);
	}
	if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Elimination Messages")){
	    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3f); 
	    eliminationClick(p, e.getSlot(), i);
	}
	if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Win Effect")) {
	    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3f); 
	    winEffectClick(p, e.getSlot(), i);
	}
	if(e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Death Effect")) {
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3f); 
        deathEffectClick(p, e.getSlot(), i);
    }
    }
    
    public void kitClick(PlayerStats s, ItemStack i, Player p) {
	//Check if user has permission
	if(!p.hasPermission("Pyroshot.Minigame.SelectKit")) {return;}
	if(PyroshotMain.getInstance().game.isActive) {p.closeInventory(); return;}
	//Determine kit by removing the " kit" at the end
	String n = i.getItemMeta().getDisplayName();
	n = ChatColor.stripColor(n);
	n = n.substring(0, n.length()-4);
	//Determine the kit name WITH color
	String kitName = i.getItemMeta().getDisplayName().substring(0, i.getItemMeta().getDisplayName().length()-4);
	//If player has permission, save kit change, otherwise give invalid permission for kit
	if(s.getKit().hasPermission(p)) {
	    String translatedKit = MessageFormat.format(plugin.getLocalizedText(p, "kitselect"), kitName);
        p.sendMessage(translatedKit);
        //Set the players kit
	    s.setKit(Kit.kitFromString(n));
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 3f); 
	    plugin.addPlayer(p, s);
	}else {
	    String translatedKit = MessageFormat.format(plugin.getLocalizedText(p, "invalidkitpermission"), kitName);
        p.sendMessage(translatedKit);
        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 3f); 
	}
    }
    
    public void mapVote(Player p, int Slot, ItemStack item) {
        if(PyroshotMain.getInstance().game.isActive) {p.closeInventory(); return;}
        //Add the vote to count
        plugin.game.addVote(p, Slot);
        String mapVote = MessageFormat.format(plugin.getLocalizedText(p, "mapvote"), item.getItemMeta().getDisplayName());
        //mapVote.replace("(Map)", item.getItemMeta().getDisplayName());
        p.sendMessage(ChatColor.DARK_AQUA + mapVote);
    }

    public void eliminationClick(Player p, int Slot, ItemStack item) {
        if(PyroshotMain.getInstance().game.isActive) {p.closeInventory(); return;}
        PlayerStats s = plugin.getPlayerStats(p);
        s.setDeathMessage(ConfigHandler.eliminationMsgs.get(Slot).getMsg());
        plugin.addPlayer(p, s);
        String effect = MessageFormat.format(plugin.getLocalizedText(p, "selecteliminationmsg"), item.getItemMeta().getDisplayName());
        p.sendMessage(ChatColor.DARK_AQUA + effect);
    }
    
    public void deathEffectClick(Player p, int Slot, ItemStack item) {
        if(PyroshotMain.getInstance().game.isActive) {p.closeInventory(); return;}
        if(!p.hasPermission("pyroshot.customization.effect.death." + ChatColor.stripColor(plugin.getWinEffect().get(Slot).getName()))) {PyroshotCommand.invalidPermission(p); return;}
        PlayerStats s = plugin.getPlayerStats(p);
        s.setDeathEffect(plugin.getDeathEffect().get(Slot));;
        plugin.addPlayer(p, s);
        String effect = MessageFormat.format(plugin.getLocalizedText(p, "selectdeatheffect"), item.getItemMeta().getDisplayName());
        p.sendMessage(ChatColor.DARK_AQUA + effect);
    }
    
    public void winEffectClick(Player p, int Slot, ItemStack item) {
        if(PyroshotMain.getInstance().game.isActive) {p.closeInventory(); return;}
        if(!p.hasPermission("pyroshot.customization.effect.win." + ChatColor.stripColor(plugin.getWinEffect().get(Slot).getName()))) {PyroshotCommand.invalidPermission(p); return;}
        PlayerStats s = plugin.getPlayerStats(p);
        s.setWinEffect(plugin.getWinEffect().get(Slot));;
        plugin.addPlayer(p, s);
        String effect = MessageFormat.format(plugin.getLocalizedText(p, "selectwineffect"), item.getItemMeta().getDisplayName());
        p.sendMessage(ChatColor.DARK_AQUA + effect);
    }
}
