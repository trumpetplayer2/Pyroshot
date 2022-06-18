package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotMinigame;

public class MinigameTickEvent extends Event{
    
    private static final HandlerList handlers = new HandlerList();
    private PyroshotMain plugin ;

    public MinigameTickEvent(PyroshotMain g) {
	plugin = g;
    }
    
    @Override
    public HandlerList getHandlers() {
	return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public PyroshotMinigame getGame() {
	return plugin.game;
    }
    
}
