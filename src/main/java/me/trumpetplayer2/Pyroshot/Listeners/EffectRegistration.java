package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.RegisterEffectsEvents;
import me.trumpetplayer2.Pyroshot.Effects.*;

public class EffectRegistration implements Listener {
    public void EventRegistration() {}
    
    @EventHandler
    public void effectsEvent(RegisterEffectsEvents e) {
        registerWinEffects(e);
        registerDeathEffects(e);
    }
    
    void registerWinEffects(RegisterEffectsEvents e){
        e.registerWinEffect(new Nothing());
        e.registerWinEffect(new Smite());
    }
    
    void registerDeathEffects(RegisterEffectsEvents e) {
        e.registerDeathEffect(new Smite());
    }
}
