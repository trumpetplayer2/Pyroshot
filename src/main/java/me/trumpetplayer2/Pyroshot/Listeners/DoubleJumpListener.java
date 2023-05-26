package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {
    PyroshotMain m;
    public DoubleJumpListener(PyroshotMain p) {
	m = p;
    }
    
    @EventHandler
    public void PlayerToggleFlightEvent(PlayerToggleFlightEvent e) {
	Player p = e.getPlayer();
	//Check if the double jump requirements are met. This includes Gamemode, Double Jump being enabled, if the player is flying, if the player is in the double jump map, and several other checks
	if (!ConfigHandler.enableDoubleJump || !m.game.isActive || p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR || p.isFlying() || !m.getPlayerStats(p).getKit().doubleJump()) {return;} else {
	    //Disable flight, and allowed flight, cancel event, and fire in the direction they are looking.
	    e.setCancelled(true);
 
	    p.setAllowFlight(false);
	    p.setFlying(false);
	    
	    p.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5).setY(1));
	    p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, -5.0f);
	    p.setFallDistance(100);
	}
    }
    

    @EventHandler
    public void FallEvent(EntityDamageEvent e) {
        //If the player falls too far, cancel the damage and enable flight if the config allows it
	if (e.getEntity() instanceof Player) {
	    Player p = (Player) e.getEntity();
	    if(e.getCause() == DamageCause.FALL && m.game.isActive && ConfigHandler.enableDoubleJump && m.getPlayerStats(p).getKit().doubleJump()) {
		e.setCancelled(true);
		p.setAllowFlight(true);
	    }
	}
    }
}
