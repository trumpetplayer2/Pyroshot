package me.trumpetplayer2.Pyroshot.Listeners.Minigame.Kit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Weapons;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class EnderKitListeners implements Listener{
    PyroshotMain plugin;
    public EnderKitListeners() {
    plugin = PyroshotMain.getInstance();
    }
    
  //Ender kit
    @EventHandler
    public void PlayerTeleport(PlayerTeleportEvent e) {
    if(!plugin.game.isActive) {return;}
    if(!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {return;}
    Player  p = e.getPlayer();
    //Player teleported with an enderpearl, check players kit
    if(!plugin.getPlayerStats(p).getKit().equals(Kit.ENDER)) {return;}
    e.setCancelled(true);
    //Make sure special is not on cooldown
    if(!plugin.getPlayerStats(p).special) {return;}
    //Check if location is safe
    Location to = isSafeTeleport(e.getTo());
    //If loc is air, its in void, if loc is in water, then its instant elimination
    if((ConfigHandler.waterLoss && to.getBlock().getBlockData().getMaterial().equals(Material.WATER)) || to.getBlock().getBlockData().getMaterial().equals(Material.AIR)) {
        return;
    }
    //Move to up a block so they dont spawn in a block
    to = to.add(0,1,0);
    //Players kit was Ender
    plugin.getPlayerStats(p).special = false;
    plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(Kit.ENDER);
    p.teleport(to);
    p.getInventory().setItem(0, Weapons.endSword(p));
    //Remove all raid pearls
    while(p.getInventory().contains(Material.ENDER_PEARL)) {
        p.getInventory().remove(Material.ENDER_PEARL);
    }
    Location  back = e.getFrom();
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> teleportBack(p, back), 20 * 30);
    }
    
  //Ender Kit
    public void teleportBack(Player p, Location back) {
    if(!plugin.game.isActive) {return;}
    p.teleport(back);
    p.getInventory().setItem(0, Weapons.bow(p));
    }
    
    //Ender Kit
    public Location isSafeTeleport(Location loc) {
    Location newLoc = loc;
    //Go down until you hit a block
    while(loc.getBlock().getBlockData().getMaterial().equals(Material.AIR) && loc.getBlockY() > -64) {
        newLoc = newLoc.subtract(0, 1, 0);
    }
    return loc;
    }
}
