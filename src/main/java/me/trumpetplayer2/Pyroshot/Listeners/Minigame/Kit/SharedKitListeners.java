package me.trumpetplayer2.Pyroshot.Listeners.Minigame.Kit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class SharedKitListeners implements Listener {
    PyroshotMain plugin;
    public SharedKitListeners() {
        plugin = PyroshotMain.getInstance();
    }
    //Sniper and Moss
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
        if(!plugin.game.isActive) {return;}
        //If player moves, check if game is active and logs the player
        Player p = e.getPlayer();
        if(!(p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL)) {return;}
        //Determine if the kit is Sniper, if so, check if the player changed Position or just changed look direction
        if(plugin.getPlayerStats(p).getKit().equals(Kit.SNIPER)) {
            Location from = e.getFrom().clone();
            Location to = e.getTo().clone();
            from.setPitch(0f);
            from.setYaw(0f);
            to.setPitch(0f);
            to.setYaw(0f);
            //Player moved, update their special
            if(!from.equals(to)) {
                if(!(from.distance(to) <= 0.15) && plugin.getPlayerStats(p).specialCooldown <= Kit.baseCooldown(Kit.SNIPER)) {
                    plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(Kit.SNIPER);
                    plugin.getPlayerStats(p).shotsSinceReset = 0;
                }
            }
        }
        //Moss Starts Here
        //Check if player is frozen
        if(plugin.getPlayerStats(p).freeze) {
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
    
  //Witch
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
    Kit k = plugin.getPlayerStats(p).getKit();
    if(!(k.equals(Kit.WITCH))) {return;}
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> returnItem(p, i), 20 * 30);
    }
    
    //Generic
    @EventHandler
    public void PlayerItemConsume(PlayerItemConsumeEvent e) {
    if(!plugin.game.isActive) {return;}
    Player p = e.getPlayer();
    Kit k = plugin.getPlayerStats(p).getKit();
    if(!(k.equals(Kit.WITCH))) {return;}
    ItemStack i = e.getItem();
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> returnItem(p, i), 20 * 30);
    }
    
    //Generic
    public void returnItem(Player p, ItemStack i) {
    //Item is potion, remove empty bottle
        if(!plugin.game.isActive) {return;}
    if(p.getInventory().contains(Material.GLASS_BOTTLE)) {
        p.getInventory().remove(Material.GLASS_BOTTLE);
    }
    p.getInventory().addItem(i);
    }
}
