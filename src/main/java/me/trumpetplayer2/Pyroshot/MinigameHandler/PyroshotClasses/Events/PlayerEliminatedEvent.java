package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.trumpetplayer2.Pyroshot.PlayerStates.EliminationType;

public class PlayerEliminatedEvent extends Event implements Cancellable{
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private EliminationType eliminatedBy;
    private Player Who;
    
    public PlayerEliminatedEvent(Player p, EliminationType way) {
	Who = p;
	eliminatedBy = way;
	isCancelled = false;
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
    public EliminationType getEliminatedBy() {
	return eliminatedBy;
    }
    public void setEliminatedBy(EliminationType eliminatedBy) {
	this.eliminatedBy = eliminatedBy;
    }

    public Player getWho() {
	return Who;
    }

}
