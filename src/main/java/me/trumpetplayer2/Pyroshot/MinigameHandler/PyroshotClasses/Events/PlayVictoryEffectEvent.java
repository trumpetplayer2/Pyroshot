package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayVictoryEffectEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    private Player Who;

    public PlayVictoryEffectEvent(Player p) {
        Who = p;
    }
    
    public Player getWho() {
        return Who;
        }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
