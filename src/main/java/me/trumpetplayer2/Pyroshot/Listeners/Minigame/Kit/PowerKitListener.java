package me.trumpetplayer2.Pyroshot.Listeners.Minigame.Kit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class PowerKitListener implements Listener {
    
    //Power
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if(!PyroshotMain.getInstance().game.isActive) {return;}
        if(!(e.getEntity() instanceof Player)) {return;}
        Player p = (Player) e.getEntity();
        if(!(PyroshotMain.getInstance().getPlayerStats(p).getKit().equals(Kit.POWER))) {return;}
        double damage = e.getDamage();
        if(PyroshotMain.getInstance().getPlayerStats(p).getKit().equals(Kit.POWER)) {
            damage = damage/2;
            if((p.getHealth() - damage) > 0) {
                e.setCancelled(true);
                p.setHealth(p.getHealth() - damage);
            }
        }
    }
}
