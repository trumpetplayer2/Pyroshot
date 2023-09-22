package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;
import me.trumpetplayer2.Pyroshot.Saves.Savable;

public class LeaveListener implements Listener{
    PyroshotMain plugin;
    Savable s;
    
    public LeaveListener(PyroshotMain m) {
	plugin = m;
	s = new Savable(m);
    }
    
    @EventHandler
    private void playerLeave(PlayerQuitEvent e) {
	//Get player and their stats
	Player p = e.getPlayer();
	PlayerStats stats = plugin.getPlayerStats(p);
	s.Save(p, stats);
	//If they are on a team remove them
	if(stats.team != null) {
	stats.team.removePlayer(p);
	}
	//Remove their vote and their data
	plugin.game.removeVote(p);
	plugin.removePlayer(p);
	p.getInventory().clear();
	p.teleport(ConfigHandler.hubLocation);
    }
}
