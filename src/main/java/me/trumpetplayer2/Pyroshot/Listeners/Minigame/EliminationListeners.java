package me.trumpetplayer2.Pyroshot.Listeners.Minigame;

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
import org.jetbrains.annotations.NotNull;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Effects.EffectType;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.PlayerEliminatedEvent;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.TriggerUltimateEvent;
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
	    if(p.getKiller() != null) {
	        killer = "was pushed into water by " + plugin.game.map.getPlayerTeam(p.getKiller()).teamColor + p.getKiller().getName();
	        incrementKill(p.getKiller());
	    }
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
	    if(!(plugin.getPlayerStats(p).useSpecial) && plugin.getPlayerStats(p).getKit().equals(Kit.MOSS) && !(e.getCause().equals(DamageCause.FIRE_TICK))) {
	        
	        TriggerUltimateEvent ev = new TriggerUltimateEvent(p, PyroshotMain.getInstance().getPlayerStats(p));
            Bukkit.getPluginManager().callEvent(ev);
	        
            if(!ev.isCancelled()) {
	        //Cancel the damage, update the users special, restore users health and give absorption
	        e.setCancelled(true);
	        plugin.getPlayerStats(p).useSpecial = true;
	        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	        p.setAbsorptionAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	        //Cause Moss to sprout around player
	        SpecialUse.replaceNearBlocks(p, Kit.MOSS.KitSymbol(p).getType());
	        plugin.getPlayerStats(p).freeze = true;
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unfreeze(p), 20*30);
	        return;
            }
	    }

	    //Player has just died
	    e.setCancelled(true);
	    p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	    if(p.getKiller() != null) {
	        if(plugin.game.map.getPlayerTeam(p.getKiller()).teamColor != null) {
	            killer = plugin.game.map.getPlayerTeam(p.getKiller()).teamColor + "";
	        }
	        killer += p.getKiller().getName();
	        incrementKill(p.getKiller());
	    }else {
	        killer = "damage";
	    }
	    eliminate(p, EliminationType.DEATH);
	    return;
	}
    }
    
    @EventHandler
    public void onEntityKill(EntityDamageByEntityEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!(e.getEntity() instanceof Player)) {return;}
	if(!(e.getDamager() instanceof Player)) {return;}
	Player p = (Player) e.getEntity();
	if(!(plugin.getPlayerStats(p).useSpecial) && plugin.getPlayerStats(p).getKit().equals(Kit.MOSS)) {
	    if(p.getHealth() - e.getFinalDamage() <= 0) {
	        
	        TriggerUltimateEvent ev = new TriggerUltimateEvent(p, PyroshotMain.getInstance().getPlayerStats(p));
            Bukkit.getPluginManager().callEvent(ev);
            
            if(!ev.isCancelled()) {
                e.setCancelled(true);
                plugin.getPlayerStats(p).useSpecial = true;
                p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                p.setAbsorptionAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                SpecialUse.replaceNearBlocks(p, Kit.MOSS.KitSymbol(p).getType());
                plugin.getPlayerStats(p).freeze = true;
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unfreeze(p), 20*30);
                return;
            }
	    }
	}
    }
    
    public void unfreeze(Player p) {
	plugin.getPlayerStats(p).freeze = false;
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
	if(p.getKiller() != null) {
        killer = "combat logged whilst fighting " + plugin.game.map.getPlayerTeam(p.getKiller()).teamColor + p.getKiller().getName();
        incrementKill(p.getKiller());
    }
	String eliminationMsg = p.getName() + " has been eliminated by " + killer + "!";
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
		if(!(plugin.getPlayerStats(p).useSpecial) && plugin.getPlayerStats(p).getKit().equals(Kit.MOSS) && (deathBy.equals(EliminationType.DEATH) || deathBy.equals(EliminationType.WATER))) {
		    
		    TriggerUltimateEvent ev = new TriggerUltimateEvent(p, PyroshotMain.getInstance().getPlayerStats(p));
		    Bukkit.getPluginManager().callEvent(ev);
		    
		    if(!ev.isCancelled()) {
	    	p.teleport(p.getLocation().add(0, 2, 0));
	    	//First Death of player with moss. Anchor them and give absorption
	    	//Cancel Elimination Event
	    	e.setCancelled(true);
	    	p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	    	p.setAbsorptionAmount(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	    	SpecialUse.replaceNearBlocks(p, Kit.MOSS.KitSymbol(p).getType());
	    	plugin.getPlayerStats(p).freeze = true;
	    	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> unfreeze(p), 20*30);
	    	//Mark that the player has used their special
	    	plugin.getPlayerStats(p).useSpecial = true;
	        return;
		    }
		}
		String eliminationMsg = parsePlayer(p, ChatColor.RED + "(player) was eliminated by (killer)");
        if(plugin.getPlayerStats(p).getDeathMessage() != null) {
            eliminationMsg = parsePlayer(p, plugin.getPlayerStats(p).getDeathMessage());
        }
        Bukkit.broadcastMessage(eliminationMsg);
        if(plugin.getPlayerStats(p) != null && plugin.getPlayerStats(p).getDeathEffect() != null) {
            //Play elimination effect
            plugin.getPlayerStats(p).getDeathEffect().oneShotEffect(p, EffectType.DIE);
        }else {
            p.getLocation().getWorld().strikeLightning(p.getLocation());
        }
		Location spectator = new Location(plugin.game.map.getWorld(), ConfigHandler.getSpectators(plugin.game.map.getWorldName()).get(0), ConfigHandler.getSpectators(plugin.game.map.getWorldName()).get(1), ConfigHandler.getSpectators(plugin.game.map.getWorldName()).get(2));
		p.teleport(spectator);
		plugin.game.map.eliminatePlayer(p);
		p.setGameMode(GameMode.SPECTATOR);
	    plugin.game.map.updateBoard();
	    PyroshotMain.getInstance().getPlayerStats(p).incrementDeaths(1);
    }
    
    private String parsePlayer(Player p, String s) {
        String playerName = "";
        if(plugin.game.map.getPlayerTeam(p) != null) {
            playerName += plugin.game.map.getPlayerTeam(p).teamColor;
        }
        playerName += p.getName();
        String temp = s.replace("(player)",  playerName + ChatColor.RED);
        temp = temp.replace("(killer)", ChatColor.DARK_AQUA + killer + ChatColor.RED);
        temp = ChatColor.translateAlternateColorCodes('&', temp);
        return temp;
    }
    
    private void incrementKill(@NotNull Player p) {
        plugin.getPlayerStats(p).incrementKills(1);
    }
}
