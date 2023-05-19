package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.PlayerEliminatedEvent;
import me.trumpetplayer2.Pyroshot.PlayerStates.EliminationType;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import net.md_5.bungee.api.ChatColor;

public class EliminationListeners implements Listener{
    //Link Event back to the to the plugin
    PyroshotMain plugin;
    String killer;
    public EliminationListeners(PyroshotMain main) {
	plugin = main;
    }
    
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e) {
	if(!plugin.game.isActive) {return;}
	Player p = e.getPlayer();
	if(!(p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL)) {return;}
	//If player is in water, eliminate them
	if(ConfigHandler.waterLoss && p.getLocation().getBlock().getType().equals(Material.WATER)) {
	    killer = "falling in water";
	    eliminate(p, EliminationType.WATER);
	}
    }
    
    @EventHandler
    public void onDamageEvent(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) {return;}
        if(!plugin.game.isActive) {
            //If lobby pvp is off, disable damage
            if(!ConfigHandler.LobbyPvp) {
                e.setDamage(0);
            }
            return;
        }
        //If player was struck by elimination lightning, cancel. Dont want players dying by winning
	if(e.getCause().equals(DamageCause.LIGHTNING)) {e.setCancelled(true); return;}
	Player p = (Player) e.getEntity();
	//If player was damaged by void, instantly eliminate them.
	if(e.getCause().equals(DamageCause.VOID)) {
	    killer = "slipping off the edge";
	    eliminate(p, EliminationType.VOID); 
	    return;}
	//If player has moss kit, run their invuln check if they would die. NOTE: Moss is not immune to fire or fall (Though double jump saves fall)
	if(p.getHealth() - e.getFinalDamage() <= 0 && !(e.getCause().equals(DamageCause.FALL))) {
	    if(!(plugin.PlayerMap.get(p).useSpecial) && plugin.PlayerMap.get(p).getKit().equals(Kit.MOSS) && !(e.getCause().equals(DamageCause.FIRE_TICK))) {
	        //Cancel the damage, update the users special, restore users health and give absorption
	        e.setCancelled(true);
	        plugin.PlayerMap.get(p).useSpecial = true;
	        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	        p.setAbsorptionAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	        //Cause Moss to sprout around player
	        PlayerDropItemListener.replaceNearBlocks(p, Kit.MOSS.KitSymbol().getType());
	        plugin.PlayerMap.get(p).freeze = true;
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unfreeze(p), 20*30);
	        return;
	    }else {
	        //Player has just died
	        e.setCancelled(true);
	        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	        killer = "damage";
	        eliminate(p, EliminationType.DEATH);
	        return;
	    }
	}
    }
    
    @EventHandler
    public void onEntityKill(EntityDamageByEntityEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!(e.getEntity() instanceof Player)) {return;}
	if(!(e.getDamager() instanceof Player)) {return;}
	Player p = (Player) e.getEntity();
	if(!(plugin.PlayerMap.get(p).useSpecial) && plugin.PlayerMap.get(p).getKit().equals(Kit.MOSS)) {
	    if(p.getHealth() - e.getFinalDamage() <= 0) {
		e.setCancelled(true);
		plugin.PlayerMap.get(p).useSpecial = true;
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		p.setAbsorptionAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		PlayerDropItemListener.replaceNearBlocks(p, Kit.MOSS.KitSymbol().getType());
		plugin.PlayerMap.get(p).freeze = true;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unfreeze(p), 20*30);
		return;
	    }
	}
    }
    
    public void unfreeze(Player p) {
	plugin.PlayerMap.get(p).freeze = false;
    }
    
    @EventHandler
    private void playerLeave(PlayerQuitEvent e) {
	if(!plugin.game.isActive) {return;}
	Player p = e.getPlayer();
	PlayerEliminatedEvent ev = new PlayerEliminatedEvent(p, EliminationType.LEFT);
	Bukkit.getPluginManager().callEvent(ev);
	p.getLocation().getWorld().strikeLightning(p.getLocation());
	plugin.game.map.removeFromTeam(p);
	p.setGameMode(GameMode.SPECTATOR);
	killer = "ceasing to exist";
	String eliminationMsg = p.getName() + " has been eliminated by " + killer + "!";
    if(plugin.PlayerMap.get(p).eliminationMessage != null) {
        eliminationMsg = parsePlayer(p, plugin.PlayerMap.get(p).eliminationMessage);
    }
	Bukkit.broadcastMessage(eliminationMsg);
	plugin.game.map.updateBoard();
    }
    
    public void eliminate(Player p, EliminationType deathBy) {
		//Prevent gmc or gmsp from being eliminated, as they already are
		if(!(p.getGameMode() == GameMode.ADVENTURE || p.getGameMode() == GameMode.SURVIVAL)) {return;}
		if(!plugin.game.isActive) {return;}
		PlayerEliminatedEvent e = new PlayerEliminatedEvent(p, deathBy);
		Bukkit.getPluginManager().callEvent(e);
		if(e.isCancelled()) {return;}
		if(!(plugin.PlayerMap.get(p).useSpecial) && plugin.PlayerMap.get(p).getKit().equals(Kit.MOSS) && (deathBy.equals(EliminationType.DEATH) || deathBy.equals(EliminationType.WATER))) {
	    	p.teleport(p.getLocation().add(0, 2, 0));
	    	//First Death of player with moss. Anchor them and give absorption
	    	//Cancel Elimination Event
	    	e.setCancelled(true);
	    	p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	    	p.setAbsorptionAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	    	PlayerDropItemListener.replaceNearBlocks(p, Kit.MOSS.KitSymbol().getType());
	    	plugin.PlayerMap.get(p).freeze = true;
	    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unfreeze(p), 20*30);
	    	//Mark that the player has used their special
	    	plugin.PlayerMap.get(p).useSpecial = true;
	        return;
		}
		p.getLocation().getWorld().strikeLightning(p.getLocation());
		Location spectator = new Location(plugin.game.map.getWorld(), ConfigHandler.getSpectators(plugin.game.map.getWorldName()).get(0), ConfigHandler.getSpectators(plugin.game.map.getWorldName()).get(1), ConfigHandler.getSpectators(plugin.game.map.getWorldName()).get(2));
		p.teleport(spectator);
		plugin.game.map.eliminatePlayer(p);
		p.setGameMode(GameMode.SPECTATOR);
		String eliminationMsg = parsePlayer(p, ChatColor.RED + "(player) has been eliminated by (killer)");
	    if(plugin.PlayerMap.get(p).eliminationMessage != null) {
	        eliminationMsg = parsePlayer(p, plugin.PlayerMap.get(p).eliminationMessage);
	    }
	    Bukkit.broadcastMessage(eliminationMsg);
	    plugin.game.map.updateBoard();
	    PyroshotMain.getInstance().PlayerMap.get(p).incrementDeaths(1);
    }
    
    private String parsePlayer(Player p, String s) {
        String temp = s.replace("(player)", ChatColor.DARK_AQUA + p.getName() + ChatColor.RED);
        temp = temp.replace("(killer)", ChatColor.DARK_AQUA + killer + ChatColor.RED);
        return temp;
    }
}
