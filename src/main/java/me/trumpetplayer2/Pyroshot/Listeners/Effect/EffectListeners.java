package me.trumpetplayer2.Pyroshot.Listeners.Effect;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.MinigameEndEvent;

public class EffectListeners implements Listener {
    @EventHandler
    public void winEffect(MinigameEndEvent e) {
        if(e.getWinners() == null) {return;}
        for(String uuid : e.getWinners().players) {
            Player p = Bukkit.getPlayer(UUID.fromString(uuid));
            if(p == null) {continue;}
            PyroshotMain.getInstance().getPlayerStats(p);
        }
    }
}
