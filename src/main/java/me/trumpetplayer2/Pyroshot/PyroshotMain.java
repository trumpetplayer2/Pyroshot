package me.trumpetplayer2.Pyroshot;


import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.trumpetplayer2.Pyroshot.Commands.PyroshotCommand;
import me.trumpetplayer2.Pyroshot.Listeners.DoubleJumpListener;
import me.trumpetplayer2.Pyroshot.Listeners.EliminationListeners;
import me.trumpetplayer2.Pyroshot.Listeners.InventoryClickListener;
import me.trumpetplayer2.Pyroshot.Listeners.ItemUseListener;
import me.trumpetplayer2.Pyroshot.Listeners.JoinListener;
import me.trumpetplayer2.Pyroshot.Listeners.LeaveListener;
import me.trumpetplayer2.Pyroshot.Listeners.PlayerDropItemListener;
import me.trumpetplayer2.Pyroshot.Listeners.PlayerShootBowListener;
import me.trumpetplayer2.Pyroshot.Listeners.PlayerTeleportListener;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotMinigame;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;
import me.trumpetplayer2.Pyroshot.Saves.Savable;
import me.trumpetplayer2.Pyroshot.SoftDependencies.PyroshotPapiExpansion;
import me.trumpetplayer2.Pyroshot.SoftDependencies.ProtocolLibHandler;


public class PyroshotMain extends JavaPlugin{
	
	protected HashMap<Player, PlayerStats> PlayerMap = new HashMap<Player, PlayerStats>();
	public PyroshotMinigame game;
	boolean ProtocolLibSupport = false;
	public static PyroshotMain instance;
	private ProtocolLibHandler plHandler;
	
	//Basic Enable
	@Override
	public void onEnable() {
	    instance = this;
	    generateGame();
	    //Grab listeners
	    this.saveDefaultConfig();
	    initializeListeners();
	    //Register command
	    getCommand("pyroshot").setExecutor(new PyroshotCommand());
	    getCommand("pyroshot").setTabCompleter(new PyroshotCommand());
	    //Register Placeholders if possible
	    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
		//PlaceholderAPI Exists, load support
		new PyroshotPapiExpansion().register();
	    }else {
		Bukkit.getServer().getConsoleSender().sendMessage(ConfigHandler.pluginAnnouncement + "Placeholder API was not found. Support disabled");
	    }
	    if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null){
	        ProtocolLibSupport = true;
	        plHandler = new ProtocolLibHandler();
	    }else {
	        Bukkit.getServer().getConsoleSender().sendMessage(ConfigHandler.pluginAnnouncement + "Protocol Lib was not found. Support disabled");
	    }
	    //Schedule
	    schedule();
	}
	
	//Basic Disable Code
	@Override
	public void onDisable() {
	    instance = null;
	    //Save player info
	    save();
	}

	public static PyroshotMain getInstance() {
	    //Retrieve instance
	    return instance;
	}
	
	public void schedule() {
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> updateDoubleJump(), 1, 1);
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> game.updateTimer(), 20, 20);
	    Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> game.endMinigame(), 20, 20);
	}
	
	public void save() {
	    //Loop through player map and save users stats
	    for(Entry<Player, PlayerStats> e : PlayerMap.entrySet()) {
		Savable s = new Savable(this);
		s.Save(e.getKey(), e.getValue());
	    }
	}
	
	public void save(Player p) {
	    //Save a specific players stats
	    Savable s = new Savable(this);
		s.Save(p, PlayerMap.get(p));
	}
	
	//Generate the minigame for later use
	private void generateGame() {
	    //Initialize game
	    game = new PyroshotMinigame(this);
	}

	
	private void updateDoubleJump() {
	    //During a game, loop through and make all players have 100 blocks of fall damage dealt to toggle flight if disabled
	    for(Player p : Bukkit.getOnlinePlayers()) {
		if(!p.getAllowFlight() &&  game.isActive && ConfigHandler.enableDoubleJump && PlayerMap.get(p).getKit().doubleJump()) {
		    p.setFallDistance(100);
		}
	    }
	}
	
	private void initializeListeners() {
	    this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        this.getServer().getPluginManager().registerEvents(new DoubleJumpListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerShootBowListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EliminationListeners(this), this);
        this.getServer().getPluginManager().registerEvents(new ItemUseListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerTeleportListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(this), this);
	}
	public boolean getProtocolLibEnabled() {
	    return ProtocolLibSupport;
	}
	
	public ProtocolLibHandler getProtocolLibHandler() {
	    return plHandler;
	}

	public PlayerStats getPlayerStats(Player p) {
	    return PlayerMap.get(p);
	}
	
	public HashMap<Player, PlayerStats> getPlayerMap(){
	    return PlayerMap;
	}
	
	public void removePlayer(Player p) {
	    if(PlayerMap.containsKey(p)) {
	        PlayerMap.remove(p);
	    }
	}
	public void addPlayer(Player p, PlayerStats s) {
	    PlayerMap.put(p, s);
	}
}