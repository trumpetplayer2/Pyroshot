package me.trumpetplayer2.Pyroshot.MinigameHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Debug.Debug;
import me.trumpetplayer2.Pyroshot.Effects.EffectType;
import me.trumpetplayer2.Pyroshot.MapHandler.*;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Grenade;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.MinigameEndEvent;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.MinigameStartEvent;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.MinigameTickEvent;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.PlayVictoryEffectEvent;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;
import me.trumpetplayer2.Pyroshot.PlayerStates.PyroshotTeam;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PyroshotMinigame {
    public WorldMap map;
    HashMap<String, Integer> votes = new HashMap<String, Integer>();
    HashMap<Integer, WorldMap> mapID = new HashMap<Integer, WorldMap>();
    public PyroshotMain PyroshotPlugin;
    public int timer = 120;
    public boolean isActive = false;
    public boolean isInitialized = false;
    PyroshotTeam winner = null;
    HashMap<String, ItemStack[]> InventoryMap = new HashMap<String, ItemStack[]>();
    public boolean isPaused = false;
    
    //If no map list provided, go with default
    public PyroshotMinigame(PyroshotMain plugin) {
	//Make sure the folder exists, necessary for resetting maps
	if(plugin.getDataFolder() == null) {
	    plugin.getDataFolder().mkdir();
	}
	//Map list comes from loading config
	PyroshotPlugin = plugin;
	timer = ConfigHandler.timer;
	mapID.putAll(ConfigHandler.getWorldMap);
    }
    
    public PyroshotMinigame(PyroshotMain plugin, HashMap<Integer, WorldMap> maps) {
	//Make sure the folder exists, necessary for resetting maps
	if(plugin.getDataFolder() == null) {
	    plugin.getDataFolder().mkdir();
	}
	//Provided another map, go with the new map
	mapID = maps;
	PyroshotPlugin = plugin;
    }
    
    ///-------------------------------\\\
    ///      MINIGAME CONTROLS	      \\\
    ///-------------------------------\\\
    
    
    //10 seconds prior to minigame start
    public void InitializeMinigame() {
	if(!chooseMap()) {
	    File gameMapsFolder = new File(PyroshotPlugin.getDataFolder(), "GameMaps");
	    //Check if folder exists, if not make it
	    if(!gameMapsFolder.exists()) {
	        //Create gamemaps folder
	        gameMapsFolder.mkdir();
	    }
	    //Create world folder
	    File worldFolder = new File(gameMapsFolder, "example");
        //Check if folder exists, if not make it
        if(!worldFolder.exists()) {
            //Create gamemaps folder
            worldFolder.mkdir();
        }
	    //Create a world "Example" in the folder
        final WorldCreator worldCreator = new WorldCreator("example");
        worldCreator.type(WorldType.FLAT);
        World world = worldCreator.createWorld();
        Bukkit.unloadWorld(world, true);
        try {
            LocalGameMap.copy(world.getWorldFolder(), worldFolder);
            LocalGameMap.delete(world.getWorldFolder());
        } catch (IOException e) {
            PyroshotPlugin.getLogger().log(Level.WARNING, "Error creating a new world in GameMaps!");
            if(ConfigHandler.debug) {
            e.printStackTrace();
            }
            return;
        }
        //Tell user to reboot server
        PyroshotPlugin.getLogger().log(Level.INFO, "A temporary map has been generated. To play on this map reboot the server, alternatively replace it with a world of your choice.");
	    //Return
	    return;
	}
	isInitialized = true;
	initializationInform();
    }
    
    //Minigame Start
    public void MinigameStart() {
	if(map.getBukkitWorld() == null) {
	    Debug.TellConsole("World was not loaded, reloading.");
	    map.restoreFromSource();
	    ReloadMap();
	    if(map.getBukkitWorld() == null) {
		Debug.TellConsole("A fatal error has occured");
		timer = ConfigHandler.timer;
		return;
	    }
	}
	timer = ConfigHandler.timer + 10;
	//Activate pointer
	//isActive = true;
	//Game is active, disable initalization.
	isInitialized = false;
	//Add team priority vote
	assignTeams();
	//Update Scoreboard
	updateScoreboard();
	//Check if event is cancelled
	MinigameStartEvent e = new MinigameStartEvent(this);
	Bukkit.getPluginManager().callEvent(e);
	if(e.isCancelled()) {
	    //Allow other plugins to prevent round from starting
	    Debug.TellConsole("Minigame cancelled by external event. If this was an error, please contact the addon's dev");
	    return;
	}
	//Display scoreboard for all players
	for(Player p : Bukkit.getOnlinePlayers()) {
	    p.setScoreboard(map.scoreboard);
	    InventoryMap.put(p.getUniqueId().toString(), p.getInventory().getContents());
	    for(PotionEffect eff : p.getActivePotionEffects()) {
	        p.removePotionEffect(eff.getType());
	    }
	}
	//Teleport players to game
	for(Player p : Bukkit.getOnlinePlayers()) {
	    p.getInventory().setContents(PyroshotPlugin.getPlayerStats(p).getKit().getInventory().getContents());
	    for(PyroshotTeam t : map.teams) {
		if(t.onTeam(p)) {
		    p.getInventory().setChestplate(t.Chestplate());
		    try {
			p.teleport(t.getTeamSpawn(map.getBukkitWorld()));
		    }catch(Exception exc) {
			Bukkit.getLogger().severe("Error loading world: " + exc.getStackTrace());
		    }
		}
	    }
	    PyroshotPlugin.getPlayerStats(p).freeze = false;
	    PyroshotPlugin.getPlayerStats(p).useSpecial = false;
	    PyroshotPlugin.getPlayerStats(p).special = false;
	    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000, 1, false, false, false));
	    
	    //Add kit to tracker
	    PlayerStats s = PyroshotPlugin.getPlayerStats(p);
	    s.incrementKit();
	}
	for(Player p : PyroshotPlugin.getPlayerMap().keySet()) {
	    PyroshotPlugin.getPlayerStats(p).specialCooldown = Kit.startCooldown(PyroshotPlugin.getPlayerStats(p).getKit());
	}
	//Update Gamerules
	map.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
	map.getWorld().setGameRule(GameRule.DO_TILE_DROPS, false);
	map.getWorld().setGameRule(GameRule.KEEP_INVENTORY, true);
	map.getWorld().setGameRule(GameRule.DO_FIRE_TICK, false);
	map.getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
	map.getWorld().setDifficulty(Difficulty.NORMAL);
	startCountdown();
    }
    
    //Start countdown
    public void startCountdown() {
        //Temp disable game
        isActive = false;
        for(int i = 0; i<=10; i++) {
            final int ti = 10 - i;
            Bukkit.getScheduler().scheduleSyncDelayedTask(PyroshotMain.getInstance(), () -> countdown(ti), 20*i);
        }
    }
    
    public void countdown(int i) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(i > 0) {
               p.sendTitle(ChatColor.RED + "Starting in", null, 0, 25, 0);
               p.sendTitle(null, ChatColor.GOLD + "" + i, 0, 20, 0);
               p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 3f); 
            }
            if(i <= 0) {
                p.sendTitle(ChatColor.RED + "Round Starting", null, 0, 25, 0);
                String team = "";
                PyroshotTeam t = map.getPlayerTeam(p);
                team = t.teamColor + t.getName() + ": ";
                for(String u : t.players) {
                    team += Bukkit.getPlayer(UUID.fromString(u)).getName();
                    team += " ";
                }
                p.sendTitle(null, ChatColor.GOLD + "" + team + " ", 0, 60, 10);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1f); 
            }
        }
        if(i <= 0) {
            isActive = true;
        }
    }
    
    //Minigame End
    public void MinigameEnd() {
	//Disable minigame
	isActive = false;
	//Call end event
	MinigameEndEvent e = new MinigameEndEvent(this, winner);
	Bukkit.getPluginManager().callEvent(e);
	if(winner != null) {
	for(Player p : Bukkit.getOnlinePlayers()) {
	    p.sendMessage(winner.teamColor + winner.getName() + " Team" + ChatColor.GOLD + " has won the game! GG!");
	}
	
	for(String uuid : winner.players) {
	    Player p = Bukkit.getPlayer(UUID.fromString(uuid));
	    //Summon particles around winners
	    PlayVictoryEffectEvent victory = new PlayVictoryEffectEvent(p);
	    Bukkit.getPluginManager().callEvent(victory);
	    //Loop through and play win effects
	    PyroshotPlugin.getPlayerStats(p).getWinEffect().oneShotEffect(p, EffectType.WIN);
	    for(int i = 0; i < 9; i++) {
	        //This will shoot 5 equal power "Shotgun" shots
	        Bukkit.getScheduler().scheduleSyncDelayedTask(PyroshotPlugin, () -> PyroshotPlugin.getPlayerStats(p).getWinEffect().loopEffect(p, EffectType.WIN), 20*i);
	    }
	}
	}
	//Wait 10 secs to trigger return to spawn
	Bukkit.getScheduler().scheduleSyncDelayedTask(PyroshotPlugin, () -> reset(),  200);
    }
    
    public void reset() {
	//Update player stats
	for(Player p : Bukkit.getOnlinePlayers()) {
	    //Update players wins/loses
	    PlayerStats s = PyroshotPlugin.getPlayerStats(p);
	    if(winner != null) {
		if(winner.onTeam(p)) {
		    s.addWins(1);
		}else {
		    s.addLoses(1);
		}
	    }else {
		s.addLoses(1);
	    }
	    //Remove team
	    s.setTeam(null);
	    //Send back to hub
	    p.teleport(ConfigHandler.hubLocation);
	    //Give back original inventory
	    if(InventoryMap.containsKey(p.getUniqueId().toString())) {
		p.getInventory().clear();
		p.getInventory().setContents(InventoryMap.get(p.getUniqueId().toString()));
	    }else {
		Debug.TellConsole("Player " + p.getName() + " did not have a saved inventory.");
	    }
	    p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	    //Set Gamemode to adventure
	    p.setGameMode(GameMode.ADVENTURE);
	    //Reset Max HP and Absorption
	    p.setAbsorptionAmount(0);
	    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
	    p.setHealth(20);
	    PyroshotPlugin.getPlayerStats(p).freeze = false;
	    PyroshotPlugin.getPlayerStats(p).useSpecial = false;
	    PyroshotPlugin.getPlayerStats(p).special = false;
	    if(p.hasPermission("pyroshot.minigame.vote")) {
	        TextComponent voteMessage = new TextComponent(ChatColor.GOLD + "" + ChatColor.BOLD + "Click here to vote for a map!" + ChatColor.RESET);
            voteMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pyroshot vote"));
            voteMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to vote!")));
            BaseComponent[] component = new ComponentBuilder().append(voteMessage).create();
            p.spigot().sendMessage(component);
        }
	}
	//Reset the settings
	clearVotes();
	map.resetAllTeams();
	winner = null;
	//Unload map
	map.unload();
    }
    
    public void initializationInform() {
	for(Player p : Bukkit.getOnlinePlayers()) {
	    p.sendMessage(ChatColor.GOLD + "Votes have been tallied! Map " + map.getSymbol().getItemMeta().getDisplayName() + ChatColor.RESET + "" + ChatColor.GOLD + " has won!");
	    TextComponent kitMessage = new TextComponent(ChatColor.YELLOW + "" + ChatColor.BOLD + "Don't forget to grab a " + ChatColor.ITALIC + "kit!"+ ChatColor.RESET);
	    kitMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pyroshot kit"));
	    kitMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to select a kit")));
	    TextComponent teamMessage = new TextComponent(ChatColor.GREEN + "" + ChatColor.BOLD + ChatColor.ITALIC + "Team " + ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "requests are now open!" + ChatColor.RESET + " ");
	    teamMessage.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/pyroshot team join "));
	    teamMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to request to join a team")));
	    BaseComponent[] component = new ComponentBuilder().append(teamMessage).append("\n").append(kitMessage).create();
	    p.spigot().sendMessage(component);
	}
    }
    
    //Scoreboard Updates
    public void updateScoreboard() {
	//Update Scoreboard
	map.createBoard();
	for(Player p : Bukkit.getOnlinePlayers()) {
	    p.setScoreboard(map.scoreboard);
	}
	
    }
    
    //Assign teams
    public void assignTeams() {
	ArrayList<Player> players = new ArrayList<Player>();
	for(Player p : Bukkit.getOnlinePlayers()) {
		players.add(p);
	}
	for(PyroshotTeam t : map.teams) {
	    for(String uid : t.players) {
	        Player p = Bukkit.getPlayer(UUID.fromString(uid));
	        players.remove(p);
	    }
	}
	//Get total players online
	int playersOnline = Bukkit.getOnlinePlayers().size();
	//Figure out average team size
	int averageSize = 0;
	if(playersOnline%map.teams.size() > 0) {
	averageSize = (int) Math.floor(playersOnline/map.teams.size());
	}else {
	    averageSize = playersOnline/map.teams.size();
	}
	//Determine if there should be a team with one more
	boolean plusOne = false;
	if(playersOnline%map.teams.size() > 0) {
	    plusOne = true;
	}
	for(PyroshotTeam t : map.teams) {
	    while(t.players.size() > averageSize) {
		if((t.players.size() - averageSize == 1) && plusOne) {
		    break;
		}else {
		    Random rand = new Random();
		    Player p = Bukkit.getPlayer(UUID.fromString(t.players.get(rand.nextInt(t.players.size()))));
		    t.removePlayer(p);
		    players.add(p);
		}
	    }
	}
	//Randomize order of players
	Collections.shuffle(players);
	//Loop back through shuffled list
	for(PyroshotTeam t : map.teams) {
	    while(t.players.size() < averageSize) {
		Random rand = new Random();
		int temp = rand.nextInt(players.size());
		t.addPlayer(players.get(temp));
		players.remove(temp);
	    }
	}
	//Teams should be balanced, if there is a player left add to a random team
	while(players.size() > 0) {
	    Random rand = new Random();
	    int temp = rand.nextInt(0, map.teams.size());
	    if(temp == map.teams.size()) {temp = 0;}
	    if(map.teams.get(temp).players.size() < averageSize + 1) {
		map.teams.get(temp).addPlayer(players.get(0));
		players.remove(0);
	    }
	}
	HashMap<String, PyroshotTeam> uniqueIDs = new HashMap<>();
	for(PyroshotTeam t : map.teams) {
	    for(String p : t.players) {
	        if(uniqueIDs.containsKey(p)) {
	            if(t.getPlayersLeft() > 1) {
	                t.removePlayer(p);
	                continue;
	            }else {
	                if(uniqueIDs.get(p).getPlayersLeft() > 1) {
	                    t.removePlayer(p);
	                }
	            }
	        }
	    }
	}
    }
    
    //Update the timer every second
    public void updateTimer() {
	
	if(!isActive) {
	    //Disable if requirements not met
	    if(isPaused) {return;}
	    if(!ConfigHandler.autostart) {return;}
	    if(Bukkit.getOnlinePlayers().size() < ConfigHandler.minPlayers) {timer = ConfigHandler.timer;}
	    if(timer == ConfigHandler.initializeTimer) {InitializeMinigame();}
	    if(timer == 0) {MinigameStart(); return;}
	    for(Player p : Bukkit.getOnlinePlayers()) {
		p.setLevel(timer);
	    }
	    timer -= 1;
	    return;
	}else {
	    //Minigame Events
	    MinigameTickEvent e = new MinigameTickEvent(PyroshotPlugin);
	    Bukkit.getPluginManager().callEvent(e);
	    updateKitCooldowns();
	}
    }
    
    //Check if minigame should end
    public void endMinigame() {
	if(!isActive) {return;}
	int numberOfTeamsLeft = 0;
	for(PyroshotTeam t : map.teams) {
	    if(t.getPlayersLeft() > 0) {
		numberOfTeamsLeft += 1;
	    }
	    if(numberOfTeamsLeft > 1) {
		return;
	    }
	}
	if(numberOfTeamsLeft < 1) {
	    winner = null;
	    MinigameEnd();
	}
	if(numberOfTeamsLeft == 1) {
	    for(PyroshotTeam t : map.teams) {
		if(t.getPlayersLeft() > 0){
		    winner = t;
		}
	    }
	    MinigameEnd();
	}
    }
    
    public void updateKitCooldowns() {
   	for(Player p : PyroshotPlugin.getPlayerMap().keySet()) {
   	    int cooldown = PyroshotPlugin.getPlayerStats(p).specialCooldown;
   	    if(cooldown > 0) {
   		PyroshotPlugin.getPlayerStats(p).specialCooldown = cooldown - 1;
   	    }
   	    if(cooldown == 0) {
		    //Fire off special checks
		    updateSpecials(p, PyroshotPlugin.getPlayerStats(p), PyroshotPlugin.getPlayerStats(p).getKit());
   	    }
   	    p.setLevel(cooldown);
//   	    if(PyroshotPlugin.getPlayerStats(p).getKit().equals(Kit.GLOW)) {
//   	     p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1, false, false, false));
//   	    }
   	}
    }
       
    public void updateSpecials(Player p, PlayerStats stats, Kit k) {
	//Set special to true
	stats.special = true;
   	//Based on players kit
   	switch(k) {
   	case ENDER:
   	    if(p.getInventory().contains(ConfigHandler.raidPearl)) {return;}
   	    //Add raid pearl
   	    p.getInventory().addItem(new ItemStack(ConfigHandler.raidPearl));
   	    break;
   	case GRENADIER:
   	    giveGrenade(p, stats);
   	    break;
   	default:
   	    //No special kits
   	    break;
   	}
    }
    
    public void giveGrenade(Player p, PlayerStats stats) {
        Grenade g = Grenade.randomGrenade();
        if(!p.getInventory().containsAtLeast(g.getSymbol(), 3)) {
            p.getInventory().addItem(g.getSymbol());
        }
        stats.specialCooldown = g.getCooldown();
        stats.special = false;
    }
    
    
    ///-------------------------------\\\
    ///         MAP HANDLERS          \\\
    ///-------------------------------\\\
    
    //Choose Map based on vote
    public boolean chooseMap() {
	//Create the variable to hold winner
	int win = 0;
	//Prevent choosing without a map
	if(mapID.size() == 0) {
		Bukkit.getServer().getConsoleSender().sendMessage(ConfigHandler.pluginAnnouncement + ChatColor.RED + "ERROR - No maps found, could not initialize selection");
		timer = ConfigHandler.timer;
		return false;
	    }
	//If there are votes, Count votes and choose arena
	if(votes.size() >= 1) {
	    HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
	    int max = 0;
	    for(String s : votes.keySet()) {
		int id = votes.get(s);
		if(count.containsKey(id)) {
		    count.put(id, count.get(id) + 1);
		}else {
		    count.put(id, 1);
		}
		
		if(count.get(id) > max) {
		    max = count.get(id);
		    win = id;
		}
	    }
	    //No votes, choose a random available map
	    if(max == 0) {
		//Choose Random Map
		Random rand = new Random();
		win = rand.nextInt(mapID.size()-1);
	    }
	}else {
	    //Choose Random Map
	    Random rand = new Random();
	    win = rand.nextInt(mapID.size());
	}
	//Set map to proper ID
	map = mapID.get(win);
	map.id = win;
	//Reload the map to load the map
	ReloadMap();
	return true;
    }
    
    //Adds a vote for player
    public void addVote(Player p, int i) {
	String ID = p.getUniqueId().toString();
	votes.put(ID, i);
    }
    
    //Removes a players vote
    public void removeVote(Player p) {
	if(votes.containsKey(p.getUniqueId().toString())) {
	    votes.remove(p.getUniqueId().toString());
	}
    }
    
    //Get current votes
    public void voteCount(CommandSender p) {
	if(mapID.size() == 0) {
		Bukkit.getServer().getConsoleSender().sendMessage(ConfigHandler.pluginAnnouncement + ChatColor.RED + "ERROR - No maps found, could not initialize selection");
		MinigameEnd();
		return;
	    }
	//If there are votes, Count votes and choose arena
	if(votes.size() >= 1) {
	    HashMap<Integer, Integer> count = new HashMap<Integer, Integer>();
	    for(String s : votes.keySet()) {
		int id = votes.get(s);
		if(count.containsKey(id)) {
		    count.put(id, count.get(id) + 1);
		}else {
		    count.put(id, 1);
		}
	    }
	    p.sendMessage(ChatColor.GOLD + "------" + ChatColor.DARK_AQUA + "Vote count" + ChatColor.GOLD + "------");
	    for(int i : count.keySet()) {
		   p.sendMessage(mapID.get(i).getSymbol().getItemMeta().getDisplayName() + ": " + count.get(i)); 
		}
	}
    }
    
    //Clear votes
    public void clearVotes() {
	votes.clear();
    }
    
    //Add additional maps after initialization
    public void RegisterMap(WorldMap tempmap) {
	tempmap.id = mapID.size();
	mapID.put(tempmap.id, tempmap);
    }
    
    public void RegisterMap(String name, ItemStack sym) {
	if(!PyroshotPlugin.getDataFolder().exists()) {
	    PyroshotPlugin.getDataFolder().mkdirs();
	}
	File gameMapsFolder = new File(PyroshotPlugin.getDataFolder(), "GameMaps");
	WorldMap tempMap = new WorldMap(gameMapsFolder, name, false, sym, mapID.size(), map.teams);
	RegisterMap(tempMap);
    }
    
    //Reloads the map
    public void ReloadMap() {
	//Grab unique ID's from old map
	int id = map.id;
	ItemStack sym = map.getSymbol();
	String name = map.getWorldName();
	if(!PyroshotPlugin.getDataFolder().exists()) {
	    PyroshotPlugin.getDataFolder().mkdirs();
	}
	File gameMapsFolder = new File(PyroshotPlugin.getDataFolder(), "GameMaps");
	map = new WorldMap(gameMapsFolder, name, true, sym, ConfigHandler.getTeams(name), ConfigHandler.getSpectators(name));
	map.id = id;
    }

 
}
