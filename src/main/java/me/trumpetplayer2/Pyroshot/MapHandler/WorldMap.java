package me.trumpetplayer2.Pyroshot.MapHandler;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PlayerStates.PyroshotTeam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class WorldMap extends LocalGameMap{
    public int id = 0;
    private ItemStack symbol;
    public ArrayList<PyroshotTeam> teams = new ArrayList<>();
    public Scoreboard scoreboard;
    public ArrayList<Double> spectator = new ArrayList<>();
    
    public void setSymbol(ItemStack i) {
	symbol = i;
    }
    public void setSymbol(String s, ItemMeta m) {
	ItemStack i = new ItemStack(Material.getMaterial(s));
	i.setItemMeta(m);
	setSymbol(i);
    }
    public void setSymbol(String s) {
	ItemStack i = new ItemStack(Material.getMaterial(s));
	setSymbol(i);
    }
    
    public WorldMap(File worldFolder, String clonedWorldName, boolean loadOnInit, ItemStack sym, @NotNull ArrayList<PyroshotTeam> newTeams, ArrayList<Double> spectators) {
	super(worldFolder, clonedWorldName, loadOnInit);
	symbol = sym;
	teams = newTeams;
	spectator = spectators;
    }
    
    public WorldMap(File worldFolder, String clonedWorldName, boolean loadOnInit, ItemStack sym, int i, @NotNull ArrayList<PyroshotTeam> newTeams) {
	super(worldFolder, clonedWorldName, loadOnInit);
	symbol = sym;
	id = i;
	teams = newTeams;
	
    }
    
    public WorldMap(File worldFolder, String clonedWorldName, boolean loadOnInit, ItemStack sym, int i, @NotNull ArrayList<PyroshotTeam> newTeams, ArrayList<Double> spectators) {
	super(worldFolder, clonedWorldName, loadOnInit);
	symbol = sym;
	id = i;
	teams = newTeams;
	spectator = spectators;
    }
    
    public ItemStack getSymbol(){
	return symbol;
    }
    
    public void removeFromTeam(Player p) {
	for(PyroshotTeam t : teams) {
	    if(t.onTeam(p)) {
		t.removePlayer(p);
	    }
	}
    }
    
    public void eliminatePlayer(Player p) {
        for(PyroshotTeam t : teams) {
            if(t.onTeam(p)) {
            t.eliminatePlayer(p);
            }
        } 
    }
    
    public void resetAllTeams() {
	for(PyroshotTeam t : teams) {
	    t.removeAllTeam();
	}
    }
    
    public Location getSpectatorLocation() {
	Location loc;
	if(!spectator.isEmpty()) {
	    loc = new Location(Bukkit.getWorld(getWorldName()), spectator.get(0), spectator.get(1), spectator.get(2));
	}else {
	    loc = ConfigHandler.hubLocation;
	}
	return loc;
    }
    
    //Update scoreboard
    public void updateBoard() {
	createBoard();
	for(Player p : Bukkit.getOnlinePlayers()) {
	    p.setScoreboard(scoreboard);
	}
    }
    
    //Scoreboard Stuff
    public void createBoard() {
	ScoreboardManager m = Bukkit.getScoreboardManager();
	scoreboard = m.getNewScoreboard();
	Objective obj = scoreboard.registerNewObjective("Pyroshot",Criteria.DUMMY,ChatColor.translateAlternateColorCodes('&',"&6&lPyro&4&lshot &c&lPlayers Left"));
	obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	//Loop through the Team list and dynamically add the teams to the list
	ArrayList<Score> scores = new ArrayList<Score>();
	scores.add(obj.getScore(ChatColor.GOLD + "/-=-=-=-=-=-=-=-=-=-=-\\"));
	//Generate Team List
	for(PyroshotTeam t : teams) {
	    scores.add(obj.getScore(ChatColor.translateAlternateColorCodes('&', t.teamColor + t.getName() + ": " + t.getColoredPlayersLeft())));
	    Team tempTeam = scoreboard.registerNewTeam(t.getName());
	    tempTeam.setColor(t.teamColor);
	    tempTeam.setCanSeeFriendlyInvisibles(true);
	    tempTeam.setAllowFriendlyFire(false);
	    tempTeam.setPrefix(t.teamColor.toString());
	    t.team = tempTeam;
	    t.teamToTeam();
	}
	//Add bottom bar
	scores.add(obj.getScore(ChatColor.GOLD + "\\-=-=-=-=-=-=-=-=-=-=-/"));
	//Loop through and add each score to board
	for(int i = 0; i < scores.size(); i++) {
	    scores.get(i).setScore(scores.size()-i);;
	}
}
    
    public void confirmTeams() {
        for(PyroshotTeam t : teams) {
            t.confirmTeams();
        }
    }
    
    @Nullable
    public PyroshotTeam getPlayerTeam(Player p) {
        for(PyroshotTeam t : teams) {
            if(t.onTeam(p)) {
                return t;
            }
        }
            return null;
    }
    
}
