package me.trumpetplayer2.Pyroshot.PlayerStates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PyroshotTeam{
    public String name;
    public List<String> players = new ArrayList<>();
    public List<String> eliminatedPlayers = new ArrayList<>();
    public Location teamSpawn = null;
    public List<Integer> coords = new ArrayList<>();
    public ChatColor teamColor;
    public Team team;
    
    public String getName() {return name;}
    public void setName(String n) {name = n;}
    
    public PyroshotTeam(String n) {
	name = n;
    }
    
    public PyroshotTeam(String n, ChatColor color) {
	name = n;
	teamColor = color;
    }
    
    public PyroshotTeam(String n, ChatColor color, List<Integer> coord) {
	name = n;
	coords = coord;
	teamColor = color;
    }
    
    public PyroshotTeam(String n, Scoreboard s, ChatColor color, Location loc) {
	name = n;
	Team t = s.registerNewTeam(n);
	t.setColor(color);
	teamSpawn = loc;
	teamColor = color;
    }
    
    //Player Handlers
    public void addPlayer(Player p) {
	players.add(p.getUniqueId().toString());
    }
    public void addPlayer(String UUID) {
	players.add(UUID);
    }
    public void removeAllTeam() {
	if(players.size() < 1) {return;}
	for(String s : players) {
	    team.removeEntry(s);
	}
	players.removeAll(players);
    }
    public void removePlayer(Player p) {
	removePlayer(p.getUniqueId().toString());
    }
    public void removePlayer(String UUID) {
	players.remove(UUID);
	if(team != null) {
	team.removeEntry(UUID);
	}
    }
    public void resetList() {
	removeAllTeam();
	for(String s : players) {
	    addPlayer(s);
	}
    }

    public void eliminatePlayer(Player p) {
        eliminatePlayer(p.getUniqueId().toString());
    }
    
    public void eliminatePlayer(String UUID) {
        if(players.contains(UUID)) {
        eliminatedPlayers.add(UUID);
        }
        removePlayer(UUID);
    }
    
    public boolean onTeam(Player p) {
	boolean onTeam = false;
	//If player is on team, set return value to true
	if(players.contains(p.getUniqueId().toString()) || eliminatedPlayers.contains(p.getUniqueId().toString())) {
	    onTeam = true;
	}
	return onTeam;
    }
    
    public int getPlayersLeft() {
	return players.size();
    }
    
    public String getColoredPlayersLeft() {
	String s = "";
	if(players.size() == 0) {
	    s = ChatColor.DARK_RED + "0";
	}else
	if(players.size() == 1) {
	    s = ChatColor.RED + "1";
	}else
	if(players.size() == 2 || players.size() == 3) {
	    s = ChatColor.GOLD + "" + players.size();
	}else
	if(players.size() > 3) {
	    s = ChatColor.GREEN + "" + players.size();
	}
	return s;
    }
    
    public Location getTeamSpawn(String world) {
	Location loc = new Location(Bukkit.getWorld(world), coords.get(0), coords.get(1), coords.get(2));
	if(teamSpawn == null) {
	    teamSpawn = loc;
	}
	return teamSpawn;
    }
    
    public Location getTeamSpawn(World world) {
	Location loc = new Location(world, coords.get(0), coords.get(1), coords.get(2));
	if(teamSpawn == null) {
	    teamSpawn = loc;
	}
	return teamSpawn;
    }
    
    public void teamToTeam() {
	for(String uuid : players) {
	    team.addEntry(uuid);
	}
    }
    public ItemStack Chestplate() {
	ItemStack i = new ItemStack(Material.LEATHER_CHESTPLATE);
	LeatherArmorMeta im = (LeatherArmorMeta) i.getItemMeta();
	int Red = 255;
	int Green = 255;
	int Blue = 255;
	switch (teamColor) {
	case AQUA:
	    Red = 85;
	    Green = 255;
	    Blue = 255;
	    break;
	case BLACK:
	    Red = 0;
	    Green = 0;
	    Blue = 0;
	    break;
	case BLUE:
	    Red = 85;
	    Green = 85;
	    Blue = 255;
	    break;
	case DARK_AQUA:
	    Red = 0;
	    Green = 170;
	    Blue = 170;
	    break;
	case DARK_BLUE:
	    Red = 0;
	    Green = 0;
	    Blue = 170;
	    break;
	case DARK_GRAY:
	    Red = 85;
	    Green = 85;
	    Blue = 85;
	    break;
	case DARK_GREEN:
	    Red = 0;
	    Green = 170;
	    Blue = 0;
	    break;
	case DARK_PURPLE:
	    Red = 170;
	    Green = 0;
	    Blue = 170;
	    break;
	case DARK_RED:
	    Red = 170;
	    Green = 0;
	    Blue = 0;
	    break;
	case GOLD:
	    Red = 255;
	    Green = 170;
	    Blue = 0;
	    break;
	case GRAY:
	    Red = 170;
	    Green = 170;
	    Blue = 170;
	    break;
	case GREEN:
	    Red = 85;
	    Green = 255;
	    Blue = 85;
	    break;
	case LIGHT_PURPLE:
	    Red = 255;
	    Green = 85;
	    Blue = 255;
	    break;
	case RED:
	    Red = 255;
	    Green = 85;
	    Blue = 85;
	    break;
	case WHITE:
	    Red = 255;
	    Green = 255;
	    Blue = 255;
	    break;
	case YELLOW:
	    Red = 255;
	    Green = 255;
	    Blue = 85;
	    break;
	default:
	    break;
	}
	im.setColor(Color.fromRGB(Red, Green, Blue));
	i.setItemMeta(im);
	return i;
    }
}
