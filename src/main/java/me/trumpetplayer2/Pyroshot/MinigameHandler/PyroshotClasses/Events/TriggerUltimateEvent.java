package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;


public class TriggerUltimateEvent extends Event implements Cancellable{
    
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private Player player;
    private PlayerStats stats;
    
    public TriggerUltimateEvent(Player p, PlayerStats pStats) {
	this.isCancelled = false;
	stats = pStats;
	player = p;
    }
    
    @Override
    public boolean isCancelled() {
	return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
	isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
	return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Kit getKit() {
        return stats.getKit();
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public PlayerStats getPlayerStats() {
        return stats;
    }
}
