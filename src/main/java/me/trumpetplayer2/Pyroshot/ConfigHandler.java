package me.trumpetplayer2.Pyroshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import me.trumpetplayer2.Pyroshot.MapHandler.WorldMap;
import me.trumpetplayer2.Pyroshot.PlayerStates.PyroshotTeam;

public class ConfigHandler {
    //Static finals for all of the config values
    private static Plugin p = Bukkit.getPluginManager().getPlugin("Pyroshot");
    public static final ItemMeta bowMeta = bowMeta();
    public static int minPlayers = minPlayers();
    public static int timerMinPlayers = timerMinPlayers();
    public static int timer = timer();
    public static int initializeTimer = initializeTimer();
    public static float StageOneMult = StageOneMult();
    public static float StageTwoMult = StageTwoMult();
    public static float StageThreeMult = StageThreeMult();
    public static float DamageMult = DamageMult();
    public static boolean waterLoss = waterLoss();
    public static boolean autostart = autostart();
    public static boolean enableDoubleJump = enableDoubleJump();
    public static boolean LobbyPvp = LobbyPvp();
    public static boolean isSpectators = isSpectators();
    public static boolean returnToHub = returnToHub();
    public static boolean debug = debug();
    public static String pluginAnnouncement = ChatColor.RESET + "" + ChatColor.YELLOW + "[" + ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.YELLOW + "]" + ChatColor.RESET + ": ";
    public static Location hubLocation = hubLocation();
    public static HashMap<Integer, WorldMap> getWorldMap = getWorldMap();
    public static ItemStack raidPearl = raidPearl();
    
    
    //Methods share name with the public versions above. See config for more info on what each is used for.
    private static ItemMeta bowMeta() {
	//Get Bow Item Meta
	ItemStack bow = new ItemStack(Material.BOW);
	ItemMeta m = bow.getItemMeta();
	//Set to be unbreakable
	if(p.getConfig().contains("bow-properties.unbreakable")) {
	m.setUnbreakable(p.getConfig().getBoolean("bow-properties.unbreakable"));
	}else {
	    m.setUnbreakable(true);
	    p.getConfig().set("bow-properties.unbreakable", true);
	}
	//Create and add Display Name
	if(p.getConfig().contains("bow-properties.name")) {
	    m.setDisplayName(ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("bow-properties.name")));
	}else {
	    m.setUnbreakable(true);
	    p.getConfig().set("bow-properties.name", "&6Fireball Bow");
	}
	//Enchant with infinity and hide enchants
	m.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
	m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	//Create and add lore
	List<String> lore = new ArrayList<String>();
	if(p.getConfig().contains("bow-properties.lore")) {
	List<String> TempLore = new ArrayList<String>();
	TempLore = (p.getConfig().getStringList("bow-properties.lore"));
	for(String s : TempLore) {
	    lore.add(ChatColor.translateAlternateColorCodes('&', s));
	}
	}else {
	    lore.add(ChatColor.RED + "Official Fireball Bow");
	    lore.add(ChatColor.RED + "Not for resale");
	    lore.add(ChatColor.RED + "Pyroshot Inc");
	    p.getConfig().set("bow-properties.lore", lore);
	}
	//Set the lore
	m.setLore(lore);
	return m;
    }
    private static ItemStack raidPearl() {
	//Create the Item and meta for Enderpearl
	ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
	ItemMeta m = pearl.getItemMeta();
	//Set up the name and lore of the pearl
	m.setDisplayName(ChatColor.DARK_AQUA + "Raid Pearl");
	List<String> lore = new ArrayList<String>();
	lore.add(ChatColor.AQUA + "Standard Issue Raid PEARL Device");
	lore.add(ChatColor.AQUA + "Precise Endergetic Automatic Relocation Lob Device");
	lore.add(ChatColor.AQUA + "Just a small toss away!");
	m.setLore(lore);
	//Set the items meta to the meta and return the itemstack
	pearl.setItemMeta(m);
	return pearl;
    }
    private static int initializeTimer() {
	//Create Timer Benchmark
	int initializeTimer = 90;
	if(p.getConfig().contains("autostart.initialize")) {
	    initializeTimer = p.getConfig().getInt("autostart.initialize");
	}else {
	    p.getConfig().set("autostart.initialize", 90);
	}
	if(initializeTimer <= 0 || initializeTimer >= timer) {
	    //If timer benchmark is above timers max, or below 0, set it to 1 second less than timer
	    initializeTimer = timer - 1;
	}
	//Invert to time before match starts
	initializeTimer = timer - initializeTimer;
	return initializeTimer;
    }
    private static Location hubLocation() {
	//Create the primative location
	Location loc;
	int x = 0;
	int y = 0;
	int z = 0;
	World world;
	//Fetch values from config and assign to local variables
	if(p.getConfig().contains("match-end.x")) {
	    x = p.getConfig().getInt("match-end.x");
	}else {
	    x = 0;
	    p.getConfig().set("match-end.x", 0);
	}
	if(p.getConfig().contains("match-end.y")) {
	    y = p.getConfig().getInt("match-end.y");
	}else {
	    y = 0;
	    p.getConfig().set("match-end.y", 0);
	}
	if(p.getConfig().contains("match-end.z")) {
	    z = p.getConfig().getInt("match-end.z");
	}else {
	    z = 0;
	    p.getConfig().set("match-end.z", 0);
	}
	if(p.getConfig().contains("match-end.world")) {
	    world = Bukkit.getWorld(p.getConfig().getString("match-end.world"));
	}else {
	    world = Bukkit.getWorlds().get(0);
	    p.getConfig().set("match-end.world", "world");
	}
	//Create the location and return it
	loc = new Location(world, x, y, z);
	return loc;
    }
    private static boolean returnToHub() {
	//Create a boolean based on config, fetch value from config and return it. See config for more details
	boolean toHub;
	if(p.getConfig().contains("match-end.teleport")) {
	    toHub = p.getConfig().getBoolean("match-end.teleport");
	}else {
	    toHub = false;
	    p.getConfig().set("match-end.teleport", false);
	}
	return toHub;
    }
    private static boolean autostart() {
	//Create a boolean based on config, fetch value from config and return it. See config for more details
	boolean autostart;
	if(p.getConfig().contains("autostart.enable")) {
	    autostart = p.getConfig().getBoolean("autostart.enable");
	}else {
	    autostart = true;
	    p.getConfig().set("autostart.enable", true);
	}
	return autostart;
    }
    private static boolean enableDoubleJump() {
	//Create a boolean based on config, fetch value from config and return it. See config for more details
	boolean doubleJump = false;
	if(p.getConfig().contains("minigame.double-jump")) {
	    doubleJump = p.getConfig().getBoolean("minigame.double-jump");
	}else {
	    doubleJump = true;
	    p.getConfig().set("minigame.double-jump", true);
	}
	return doubleJump;
    }
    private static int minPlayers() {
	//Create, fetch, and return a number from the config. See config for more info on what this is for
	int min;
	if(p.getConfig().contains("minigame.minimum-players")) {
	min = p.getConfig().getInt("minigame.minimum-players");
	}else {
	    min = 2;
	    p.getConfig().set("minigame.minimum-players", 2);
	}
	return min;
    } 
    private static float StageOneMult() {
	//Create, fetch, and return a number from the config. See config for more info on what this is for
	float f = 0;
	if(p.getConfig().contains("fireball-power.level-1")) {
	f = (float) p.getConfig().getDouble("fireball-power.level-1");
	}
	else {
	    f = 0.3f;
	    p.getConfig().set("fireball-power.level-1", 0.3);
	}
	return f;
    }
    private static float StageTwoMult() {
	//Create, fetch, and return a number from the config. See config for more info on what this is for
	float f = 0;
	if(p.getConfig().contains("fireball-power.level-2")) {
	f = (float) p.getConfig().getDouble("fireball-power.level-2");
	}
	else {
	    f = 3.0f;
	    p.getConfig().set("fireball-power.level-2", 3);
	}
	return f;
    }
    private static float StageThreeMult() {
	//Create, fetch, and return a number from the config. See config for more info on what this is for
	float f = 0;
	if(p.getConfig().contains("fireball-power.level-3")) {
	f = (float) p.getConfig().getDouble("fireball-power.level-3");
	}
	else {
	    f = 2.5f;
	    p.getConfig().set("fireball-power.level-3", 2.5);
	}
	return f;
    }
    private static float DamageMult() {
        //Create, fetch, and return a number from the config. See config for more info on what this is for
        float f = 0;
        if(p.getConfig().contains("fireball-power.damage-mult")) {
        f = (float) p.getConfig().getDouble("fireball-power.damage-mult");
        }
        else {
            f = 2.5f;
            p.getConfig().set("fireball-power.damage-mult", 2);
        }
        return f;
        }
    private static boolean waterLoss() {
	//Create a boolean based on config, fetch value from config and return it. See config for more details
	boolean lose = true;
	if(p.getConfig().contains("minigame.water-loss")) {
	    lose = p.getConfig().getBoolean("minigame.water-loss");
	}else {
	    lose = true;
	    p.getConfig().set("minigame.water-loss", true);
	}
	return lose;
    }
    private static boolean isSpectators() {
	//Create a boolean based on config, fetch value from config and return it. See config for more details
	boolean spectators = true;
	if(p.getConfig().contains("minigame.spectators")) {
	    spectators = p.getConfig().getBoolean("minigame.spectators");
	}else {
	    spectators = true;
	    p.getConfig().set("minigame.spectators", true);
	}
	return spectators;
    } 
    private static int timerMinPlayers() {
	//Create, fetch, and return a number from the config. See config for more info on what this is for
	int min = 2;
	if(p.getConfig().contains("autostart.min-players")) {
	    min = p.getConfig().getInt("autostart.min-players");
	}else {
	    min = 2;
	    p.getConfig().set("autostart.min-players", 2);
	}
	return min;
    } 
    private static int timer() {
	//Create, fetch, and return a number from the config. See config for more info on what this is for
	int timer = 120;
	if(p.getConfig().contains("autostart.timer")) {
	    timer = p.getConfig().getInt("autostart.timer");
	}else {
	    p.getConfig().set("autostart.timer", 120);
	    timer = 120;
	}
	return timer;
    }
    private static HashMap<Integer, WorldMap> getWorldMap(){
	//Create map hashmap
	HashMap<Integer, WorldMap> tempmap = new HashMap<Integer, WorldMap>();
	if(p.getDataFolder() == null) {
	    p.getDataFolder().mkdir();
	}
	//Define location of the gamemap files
	File gameMapsFolder = new File(p.getDataFolder(), "GameMaps");
	//Grab all of the maps in the file
	if(p.getConfig().contains("minigame.autoreset.world")) {
	    //Loop through the sections
	    for(String s : p.getConfig().getConfigurationSection("minigame.autoreset.world").getKeys(false)){
		if(s != null) {
		    //Set up default icon and determine if there is a special one
		    ItemStack icon = null;
		    //Check config for the Icon
		    if(p.getConfig().contains("minigame.autoreset.world." + s + ".icon")) {
			icon = new ItemStack(Material.valueOf(p.getConfig().getString("minigame.autoreset.world." + s + ".icon")));
		    }else {
			p.getConfig().set("minigame.autoreset.world." + s + ".icon", "BARRIER");
		    }
		    if(icon == null) {
			icon = new ItemStack(Material.BARRIER);
		    }
		    ItemMeta meta = icon.getItemMeta();
		    //Display Name for world
		    if(p.getConfig().contains("minigame.autoreset.world." + s + ".display")) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', p.getConfig().getString("minigame.autoreset.world." + s + ".display")));
		    }else {
			meta.setDisplayName(ChatColor.DARK_RED + "No world name found");
			p.getConfig().set("minigame.autoreset.world." + s + ".display", "&4No world name found");
		    }
		    //Set up lore for world
		    if(p.getConfig().contains("minigame.autoreset.world." + s + ".lore")) {
			List<String> sl = new ArrayList<String>();
			for(String e : p.getConfig().getStringList("minigame.autoreset.world." + s + ".lore")) {
			    sl.add(ChatColor.translateAlternateColorCodes('&',e));
			}
			meta.setLore(sl);
		    }else {
			List<String> sl = new ArrayList<String>();
			sl.add(ChatColor.RED + "If you believe this to be an error, check config");
			sl.add(ChatColor.RED + "If you did not define it, please do.");
			meta.setLore(sl);
			//Create node for user
			p.getConfig().set("minigame.autoreset.world." + s + ".lore", sl);
		    }
		    icon.setItemMeta(meta);
		    //Create the World Class for the generated world
		    WorldMap m = new WorldMap(gameMapsFolder, s, false, icon, getTeams(s), getSpectators(s));
		    //Put the entry in the hashmap
		    tempmap.put(tempmap.size(), m);
		}
	    }
	}
	return tempmap;
    }
    public static ArrayList<Double> getSpectators(String worldName){
	//Create an arraylist to hold the values from the file
	ArrayList<Double> location = new ArrayList<Double>();
	double x;
	double y;
	double z;
	//Retrieve values from file
	if(configContains("minigame.autoreset.world." + worldName + ".spectators.x")) {
	    x = p.getConfig().getInt("minigame.autoreset.world." + worldName + ".spectators.x");
	}else {
	    x = 0;
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".spectators.x", 0);
	}
	if(configContains("minigame.autoreset.world." + worldName + ".spectators.y")) {
	    y = p.getConfig().getInt("minigame.autoreset.world." + worldName + ".spectators.y");
	}else {
	    y = 0;
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".spectators.y", 0);
	}
	if(configContains("minigame.autoreset.world." + worldName + ".spectators.z")) {
	    z = p.getConfig().getInt("minigame.autoreset.world." + worldName + ".spectators.z");
	}else {
	    z = 0;
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".spectators.z", 0);
	}
	//Add to list and return list
	location.add(x);
	location.add(y);
	location.add(z);
	return location;
    }
    public static ArrayList<PyroshotTeam> getTeams(String worldName){
	//Create team list
	ArrayList<PyroshotTeam> teams = new ArrayList<PyroshotTeam>();
	//Make sure there are teams in config
	if(p.getConfig().contains("minigame.autoreset.world." + worldName + ".teams")) {
	    //Loop through the teams in said world
	    for(String s : p.getConfig().getConfigurationSection("minigame.autoreset.world." + worldName + ".teams").getKeys(false)) {
		//Set up and initialize variables
		String teamName = "";
		ChatColor color = ChatColor.WHITE;
		int x = 0;
		int y = 0;
		int z = 0;
		//Fill the variables from the config
		if(configContains("minigame.autoreset.world." + worldName + ".teams." + s + ".team-name")) {
		teamName = p.getConfig().getString("minigame.autoreset.world." + worldName + ".teams." + s + ".team-name");
		}else {
		    teamName = teams.size() + "PleasePutNameHere";
		    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams." + s + ".team-name", teamName);
		}
		//Color on scoreboard
		if(configContains("minigame.autoreset.world." + worldName + ".teams." + s + ".team-color")) {
		    color = ChatColor.valueOf(p.getConfig().getString("minigame.autoreset.world." + worldName + ".teams." + s + ".team-color"));
		}else {
		    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams." + s + ".team-color", color.toString());
		}
		if(configContains("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.x")) {
		    x = p.getConfig().getInt("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.x");
		}else {
		    x = 0;
		    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.x", 0);
		}
		if(configContains("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.y")) {
		    y = p.getConfig().getInt("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.y");
		}else {
		    y = 0;
		    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.y", 0);
		}
		if(configContains("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.z")) {
		    z = p.getConfig().getInt("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.z");
		}else {
		    z = 0;
		    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams." + s + ".spawn-loc.z", 0);
		}
		//Create a list to hold the coords, and condense all information to be stored in the team list
		List<Integer> coords = new ArrayList<Integer>();
		coords.add(x);
		coords.add(y);
		coords.add(z);
		PyroshotTeam temp = new PyroshotTeam(teamName, color, coords);
		teams.add(temp);
	    }
	}else {
	    //There were no teams, Autogenerate 2 and add them to the list
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.red.team-name", "PleasePutTeamNameHere");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.red.team-color", "RED");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.red.spawn-loc.x", "0");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.red.spawn-loc.y", "0");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.red.spawn-loc.z", "0");
	    
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.blue.team-name", "PleasePutTeamNameHere");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.blue.team-color", "BLUE");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.blue.spawn-loc.x", "0");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.blue.spawn-loc.y", "0");
	    p.getConfig().set("minigame.autoreset.world." + worldName + ".teams.blue.spawn-loc.z", "0");
	    
	    PyroshotTeam red = new PyroshotTeam("Red", ChatColor.RED);
	    PyroshotTeam blue = new PyroshotTeam("Blue", ChatColor.BLUE);
	    teams.add(red);
	    teams.add(blue);
	}
	return teams;
    } 
    public static boolean configContains(String configPath) {
	//Condensed version of grabing config, checks the path and returns true/false depending on if the config contained value
	boolean temp = false;
	if(p.getConfig().contains(configPath)) {
	    temp = true;
	}
	return temp;
    }
    private static boolean LobbyPvp() {
	//Fetches if pvp is on in the lobby
	boolean Pvp = false;
	if(p.getConfig().contains("minigame.enable-pvp")) {
	    Pvp = p.getConfig().getBoolean("minigame.enable-pvp");
	}else {
	    Pvp = false;
	    p.getConfig().set("minigame.enable-pvp", false);
	}
	return Pvp;
    }
    private static boolean debug() {
	//Enable/Disable Debug This is a dev tool, please ignore
	boolean debug;
	if(p.getConfig().contains("debug")) {
	    debug = p.getConfig().getBoolean("debug");
	}else {
	    debug = false;
	}
	return debug;
    }
}
