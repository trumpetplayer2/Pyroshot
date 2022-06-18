package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;
import me.trumpetplayer2.Pyroshot.Saves.Savable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class JoinListener implements Listener{
    PyroshotMain plugin;
    
    public JoinListener(PyroshotMain m) {
	plugin = m;
    }
    
    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
	Savable s = new Savable(plugin);
	Player p = e.getPlayer();
	PlayerStats stats = s.loadPlayerStats(e.getPlayer());
	plugin.PlayerMap.put(p, stats);
	if(plugin.game.isActive) {
	    p.teleport(plugin.game.map.getSpectatorLocation());
	}else {
	    p.teleport(ConfigHandler.hubLocation);
	    if(p.hasPermission("pyroshot.minigame.vote")) {
	        TextComponent voteMessage = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "Click here to vote for a map!" + ChatColor.RESET);
	        voteMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pyroshot vote"));
	        voteMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to vote!")));
	        BaseComponent[] component = new ComponentBuilder().append(voteMessage).create();
	        p.spigot().sendMessage(component);
	    }
	}
    }
    
}
