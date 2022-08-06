package me.trumpetplayer2.Pyroshot.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Weapons;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class PlayerTeleportListener implements Listener{
    PyroshotMain plugin;
    public PlayerTeleportListener(PyroshotMain main) {
	plugin = main;
    }
    
    @EventHandler
    public void PlayerTeleport(PlayerTeleportEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {return;}
	Player  p = e.getPlayer();
	//Player teleported with an enderpearl, check players kit
	if(!plugin.PlayerMap.get(p).getKit().equals(Kit.ENDER)) {return;}
	e.setCancelled(true);
	//Make sure special is not on cooldown
	if(!plugin.PlayerMap.get(p).special) {return;}
	//Check if location is safe
	Location to = isSafeTeleport(e.getTo());
	//If loc is air, its in void, if loc is in water, then its instant elimination
	if((ConfigHandler.waterLoss && to.getBlock().getBlockData().getMaterial().equals(Material.WATER)) || to.getBlock().getBlockData().getMaterial().equals(Material.AIR)) {
	    return;
	}
	//Move to up a block so they dont spawn in a block
	to = to.add(0,1,0);
	//Players kit was Ender
	plugin.PlayerMap.get(p).special = false;
	plugin.PlayerMap.get(p).specialCooldown = Kit.baseCooldown(Kit.ENDER);
	p.teleport(to);
	p.getInventory().setItem(0, Weapons.endSword);
	//Remove all raid pearls
	while(p.getInventory().contains(Material.ENDER_PEARL)) {
	    p.getInventory().remove(Material.ENDER_PEARL);
	}
	Location  back = e.getFrom();
	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> teleportBack(p, back), 20 * 30);
    }
    
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
    if(!plugin.game.isActive) {return;}
    //If player moves, check if game is active and logs the player
    Player p = e.getPlayer();
    if(!(p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL)) {return;}
    //Determine if the kit is Sniper, if so, check if the player changed Position or just changed look direction
    if(plugin.PlayerMap.get(p).getKit().equals(Kit.SNIPER)) {
        Location from = e.getFrom().clone();
        Location to = e.getTo().clone();
        from.setPitch(0f);
        from.setYaw(0f);
        to.setPitch(0f);
        to.setYaw(0f);
        //Player moved, update their special
        if(!from.equals(to)) {
        plugin.PlayerMap.get(p).specialCooldown = Kit.baseCooldown(Kit.SNIPER);
        }
    }
    //Check if player is frozen
    if(plugin.PlayerMap.get(p).freeze) {
        //Check if player changed Loc or just looked around, if they changed location in a coordinal direction aside from vertical, prevent from moving.
        Location from = e.getFrom().clone();
        Location to = e.getTo().clone();
        from.setPitch(0f);
        from.setYaw(0f);
        //Vertical is prevented to stop server flight issues
        from.setY(0);
        to.setPitch(0f);
        to.setYaw(0f);
        to.setY(0);
        if(!from.equals(to)) {
        //Player is frozen, no moving
        e.setCancelled(true);
        }
    }
    }
    
    public void teleportBack(Player p, Location back) {
	if(!plugin.game.isActive) {return;}
	p.teleport(back);
	p.getInventory().setItem(0, Weapons.bow);
    }
    
    public Location isSafeTeleport(Location loc) {
	Location newLoc = loc;
	//Go down until you hit a block
	while(loc.getBlock().getBlockData().getMaterial().equals(Material.AIR) && loc.getBlockY() > -64) {
	    newLoc = newLoc.subtract(0, 1, 0);
	}
	return loc;
    }
    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!(e.getEntity() instanceof Player)) {return;}
	Player p = (Player) e.getEntity();
	if(!(plugin.PlayerMap.get(p).getKit().equals(Kit.POWER) || plugin.PlayerMap.get(p).getKit().equals(Kit.MOSS))) {return;}
	double damage = e.getDamage();
	if(plugin.PlayerMap.get(p).getKit().equals(Kit.POWER)) {
	damage = damage/2;
	if((p.getHealth() - damage) > 0) {
	    e.setCancelled(true);
	    p.setHealth(p.getHealth() - damage);
	}
	}
	
    }
    
    @EventHandler
    public void ProjectileHit(ProjectileHitEvent e) {
	if(!(plugin.game.isActive)) {return;}
	if(!(e.getEntity() instanceof Snowball)) {return;}
	if(!(e.getEntity().getShooter() instanceof Player)) {return;}
	Player p = (Player) e.getEntity().getShooter();
	//Get where snowball hit
	Location loc = e.getEntity().getLocation().clone();
	int radius = 3;
	ArrayList<Block> blocks = new ArrayList<Block>();
	
	for (int x = (loc.getBlockX()-radius); x <= (loc.getBlockX()+radius); x++) {
	    for (int y = (loc.getBlockY()-radius); y <= (loc.getBlockY()+radius); y++) {
		for (int z = (loc.getBlockZ()-radius); z <= (loc.getBlockZ()+radius); z++) {
		    Location l = new Location(loc.getWorld(), x, y, z);
		    if (l.distance(loc) <= radius) {
			blocks.add(l.getBlock());
		    }
		}
	    }
	}
	for(Block b : blocks) {
	    b.setType(Material.WATER);
	    BlockData bData = b.getBlockData();
	    Levelled tempt = (Levelled) bData;
	    tempt.setLevel(8);
	    b.setBlockData(tempt);
	}
	plugin.PlayerMap.get(p).special = false;
	plugin.PlayerMap.get(p).useSpecial = false;
	plugin.PlayerMap.get(p).specialCooldown = Kit.baseCooldown(Kit.WATER);
    }
    
    @EventHandler
    public void ProjectileThrown(ProjectileLaunchEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!(e.getEntity() instanceof EnderPearl)) {return;}
	if(!(e.getEntity().getShooter() instanceof Player)) {return;}
	Player p = (Player) e.getEntity().getShooter();
	plugin.PlayerMap.get(p).specialCooldown = 15;
    }
}
