package me.trumpetplayer2.Pyroshot.Saves;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;

public class Savable {
    private String dataPathS = "";
    private FileConfiguration dataConfig;
    private File dataFile;
    private PyroshotMain plugin;
    
    public Savable(String path, FileConfiguration config, File file, PyroshotMain main) {
	dataPathS = path;
	dataConfig = config;
	dataFile = file;
	plugin = main;
	getData();
    }
    
    public Savable(PyroshotMain main, String path) {
	plugin = main;
	dataPathS = path;
	getData();
    }
    
    public Savable(PyroshotMain main) {
	plugin = main;
	dataPathS = "";
	getData();
    }
    
    public void Save(Player p, PlayerStats stat) {
	if(dataConfig.getConfigurationSection(p.getUniqueId().toString()) == null) {
	    dataConfig.createSection(p.getUniqueId().toString());
	    }
	String dataPath = dataPathS;
	dataPath += "." +  p.getUniqueId().toString();
	Map<String, Object> configValues = dataConfig.getConfigurationSection(dataPath).getValues(false);
	for (Map.Entry<String, Object> entry : configValues.entrySet()) {
	    dataConfig.getConfigurationSection(dataPath).set(entry.getKey(), null);
	}
	dataPath += ".";
	if(dataConfig.getConfigurationSection(dataPath) == null) {
	    dataConfig.createSection(dataPath);
	    }
	int wins = stat.getWins();
	int loses = stat.getLoses();
	dataConfig.getConfigurationSection(dataPath).set("Wins", wins);
	dataConfig.getConfigurationSection(dataPath).set("Loses", loses);
	try {
	this.dataConfig.save(dataFile);
	}catch(Exception e) {
	    
	}
    }
    
    public PlayerStats loadPlayerStats(Player p) {
	String dataPath = dataPathS;
	dataPath += "." + p.getUniqueId().toString();
	int w = 0;
	int l = 0;
	if(dataConfig.getString(dataPath + ".Wins") != null) {
	    w = dataConfig.getInt(dataPath + ".Wins");
	    }
	if(dataConfig.getString(dataPath + ".Loses") != null) {
	    l = dataConfig.getInt(dataPath + ".Loses");
	    }
	PlayerStats ps = new PlayerStats(Kit.DEFAULT, w, l);
	return ps;
    }
    
    public FileConfiguration getData() {
	    if(dataConfig == null) {reloadData();}
	    
	    return dataConfig;
	}
	
	public void reloadData() {
	    if(this.dataFile == null) {
		this.dataFile = new File(plugin.getDataFolder(), "data.yml");
	    }
	    dataConfig = YamlConfiguration.loadConfiguration(dataFile);
	    InputStream inStream = plugin.getResource("data.yml");
	    if(inStream != null) {
	    YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(new InputStreamReader(inStream));
	    dataConfig.setDefaults(dataFile);
	    }
	}
	
	public void saveDefaultConfig() {
	    if(dataFile == null) {
		dataFile = new File(plugin.getDataFolder(), "data.yml");
	    }
	}
}
