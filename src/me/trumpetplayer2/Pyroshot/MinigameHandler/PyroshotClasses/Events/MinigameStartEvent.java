package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotMinigame;

public class MinigameStartEvent extends Event implements Cancellable{
    
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private PyroshotMinigame game;
    
    public MinigameStartEvent(PyroshotMinigame g) {
	this.isCancelled = false;
	game = g;
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

    public PyroshotMinigame getGame() {
	return game;
    }

}
