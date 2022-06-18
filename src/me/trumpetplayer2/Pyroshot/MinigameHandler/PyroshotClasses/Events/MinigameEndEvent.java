package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotMinigame;
import me.trumpetplayer2.Pyroshot.PlayerStates.PyroshotTeam;

public class MinigameEndEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private PyroshotMinigame game;
    private PyroshotTeam Winners;
    
    public MinigameEndEvent(PyroshotMinigame g, PyroshotTeam win) {
	game = g;
	Winners = win;
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

    public PyroshotTeam getWinners() {
	return Winners;
    }

    public void setWinners(PyroshotTeam winners) {
	Winners = winners;
    }
}
