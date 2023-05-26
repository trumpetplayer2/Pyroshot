package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.PlayerFireball;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Weapons;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.TriggerUltimateEvent;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class PlayerShootBowListener implements Listener{
    PyroshotMain plugin;
    ConfigHandler config;
    public PlayerShootBowListener(PyroshotMain m) {
	plugin = m;
    }
    @EventHandler
    public void EntityShootBowEvent(EntityShootBowEvent e) {
	//Only Players can shoot fireballs, Make sure everything is in order to shoot
	if(!(e.getEntity() instanceof Player)) {return;}
	if(!(e.getBow().getItemMeta().equals(ConfigHandler.bowMeta) || e.getBow().getItemMeta().getLore().equals(Weapons.Machinegun.getItemMeta().getLore()))) {return;}
	if(!(e.getProjectile() instanceof Arrow)) {return;}
	if(!plugin.game.isActive) {return;}
	//Shoot fireball
	Player p = (Player) e.getEntity();
	if(p.getGameMode().equals(GameMode.SPECTATOR)) {e.setCancelled(true); return;}
	TriggerUltimateEvent ev = new TriggerUltimateEvent(p, PyroshotMain.getInstance().getPlayerStats(p));
	if(plugin.getPlayerStats(p).getKit().equals(Kit.WATER) && plugin.getPlayerStats(p).useSpecial) {
	    Bukkit.getPluginManager().callEvent(ev);
	    e.setCancelled(true);
	    if(ev.isCancelled()) {return;}
	    //Player is water kit and wants to use special
	    PlayerFireball.LaunchSnowball(p, (Arrow) e.getProjectile(), plugin.getPlayerStats(p), e.getForce());
	    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, 1, 1f); 
	    return;
	}
	if(plugin.getPlayerStats(p).getKit().equals(Kit.SHOTGUN) && plugin.getPlayerStats(p).useSpecial) {
	    Bukkit.getPluginManager().callEvent(ev);
	    e.setCancelled(true);
	    if(ev.isCancelled()) {return;}
	    PlayerFireball.ShotgunFireball(p, (Arrow) e.getProjectile(), plugin, e.getForce());
	    return;
	}
	PlayerFireball.LaunchFireball(p, (Arrow) e.getProjectile(), plugin.getPlayerStats(p), e.getForce());
	e.setCancelled(true);
    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f); 
    p.getInventory().setItem(17, new ItemStack(Material.ARROW));
    }
}
