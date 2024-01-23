package me.trumpetplayer2.Pyroshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.trumpetplayer2.Pyroshot.Commands.PyroshotCommand;
import me.trumpetplayer2.Pyroshot.Debug.Debug;
import me.trumpetplayer2.Pyroshot.Effects.Effect;
import me.trumpetplayer2.Pyroshot.Effects.Nothing;
import me.trumpetplayer2.Pyroshot.Effects.Smite;
import me.trumpetplayer2.Pyroshot.Listeners.*;
import me.trumpetplayer2.Pyroshot.Localization.Localizations;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotMinigame;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.RegisterEffectsEvents;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;
import me.trumpetplayer2.Pyroshot.Saves.Savable;
import me.trumpetplayer2.Pyroshot.SoftDependencies.PyroshotPapiExpansion;
import net.md_5.bungee.api.ChatColor;
import me.trumpetplayer2.Pyroshot.SoftDependencies.ProtocolLibHandler;


public class PyroshotMain extends JavaPlugin{
	//Discord Test Comment
	protected HashMap<Player, PlayerStats> PlayerMap = new HashMap<Player, PlayerStats>();
	protected HashMap<String, Localizations> LocalizationRegistry = new HashMap<String, Localizations>();
	public PyroshotMinigame game;
	boolean ProtocolLibSupport = false;
	public static PyroshotMain instance;
	private ProtocolLibHandler plHandler;

    private ArrayList<Effect> winEffects = new ArrayList<Effect>();
    private ArrayList<Effect> deathEffects = new ArrayList<Effect>();
    
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
	    //Register effects
	    Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> registerEffects());
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
        this.getServer().getPluginManager().registerEvents(new EffectRegistration(), this);
        this.getServer().getPluginManager().registerEvents(new OwlKitListeners(), this);
        this.getServer().getPluginManager().registerEvents(new IllusionKitListeners(), this);
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
	@SuppressWarnings("unchecked")
    public void registerEffects() {
	    RegisterEffectsEvents e = new RegisterEffectsEvents();
	    Bukkit.getPluginManager().callEvent(e);
	    ArrayList<Effect> temp = new ArrayList<Effect>();
	    //If size is < 1, something is wrong!
	    if(e.getWinEffects() != null) {
	        temp = e.getWinEffects();
	    }
	    if(temp.size() < 1) {
	        Debug.TellConsole("No Win Effects found");
	        temp.add(new Smite());
	    }
	    winEffects = (ArrayList<Effect>) temp.clone();
	    temp.clear();
	    if(e.getDeathEffects() != null) {
	    temp = e.getDeathEffects();
	    }
	    if(temp.size() < 1) {
	        Debug.TellConsole("No Death Effects found");
	        temp.add(new Nothing());
	    }
	    deathEffects = (ArrayList<Effect>) temp.clone();
	}
	public ArrayList<Effect> getWinEffect(){
	    return winEffects;
	}
	public ArrayList<Effect> getDeathEffect(){
        return deathEffects;
    }

	public String getLocalizedText(Player p, String key) {
	    //Define a variable that will store the text the player looked up
	    String text = "";
	    //Get the players lang, IE: en_us
	    String lang = p.getLocale().toString();
	    //Check if its already loaded into cache
	    if(!LocalizationRegistry.containsKey(lang)) {
	        //If not, load into cache
	        LocalizationRegistry.put(lang, new Localizations(lang));
	    }
	    //Query the localization to retrieve text
	    text = LocalizationRegistry.get(lang).getLocalization(key);
	    //Return the text given
	    return ChatColor.translateAlternateColorCodes('&', text);
	}
}