package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events;

import java.util.ArrayList;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.trumpetplayer2.Pyroshot.Effects.Effect;

public class RegisterEffectsEvents extends Event{
    private static final HandlerList handlers = new HandlerList();
    private ArrayList<Effect> winEffects = new ArrayList<Effect>();
    private ArrayList<Effect> deathEffects = new ArrayList<Effect>();
    
    public RegisterEffectsEvents() {
        
    }
    
    @Override
    public HandlerList getHandlers() {
    return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void registerWinEffect(Effect e) {
        winEffects.add(e);
    }
    
    public void registerDeathEffect(Effect e) {
        deathEffects.add(e);
    }
    
    public ArrayList<Effect> getWinEffects(){
        return winEffects;
    }
    
    public ArrayList<Effect> getDeathEffects(){
        return deathEffects;
    }
}
