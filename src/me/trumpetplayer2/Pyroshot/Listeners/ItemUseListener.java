package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;

public class ItemUseListener implements Listener{
    PyroshotMain plugin;
    public ItemUseListener(PyroshotMain main) {
	plugin = main;
    }
    
    @EventHandler
    public void PotionSplash(PotionSplashEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!(e.getEntity().getShooter() instanceof Player)){return;}
	Player p = (Player) e.getEntity().getShooter();
	ItemStack i = e.getPotion().getItem();
	if(i.getType().equals(Material.POTION)) {
	    //Item is potion, remove empty bottle
	    if(p.getInventory().contains(Material.GLASS_BOTTLE)) {
		p.getInventory().remove(Material.GLASS_BOTTLE);
	    }
	}
	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> returnItem(p, i), 20 * 30);
    }
    
    @EventHandler
    public void PlayerItemConsume(PlayerItemConsumeEvent e) {
	if(!plugin.game.isActive) {return;}
	Player p = e.getPlayer();
	ItemStack i = e.getItem();
	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> returnItem(p, i), 20 * 30);
    }
    
    @EventHandler
    public void onPearlShot(ProjectileLaunchEvent e) {
	if (!(e.getEntity() instanceof EnderPearl)) {return;}
	if(!(e.getEntity() instanceof Player)) {return;}
	Player p = (Player) e.getEntity();
	ItemStack i = ConfigHandler.raidPearl;
	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> checkPearlLanded(p, i), 20*30);
    }
    
    public void checkPearlLanded(Player p, ItemStack i) {
	if(!plugin.PlayerMap.get(p).special) {return;}
	returnItem(p, i);
    }
    
    public void returnItem(Player p, ItemStack i) {
	//Item is potion, remove empty bottle
        if(!plugin.game.isActive) {return;}
	if(p.getInventory().contains(Material.GLASS_BOTTLE)) {
	    p.getInventory().remove(Material.GLASS_BOTTLE);
	}
	p.getInventory().addItem(i);
    }
    
}
