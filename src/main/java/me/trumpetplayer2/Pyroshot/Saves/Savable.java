package me.trumpetplayer2.Pyroshot.Saves;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Map.Entry;

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
	int deaths = stat.getDeaths();
	int kills = stat.getKills();
	dataConfig.getConfigurationSection(dataPath).set("Wins", wins);
	dataConfig.getConfigurationSection(dataPath).set("Loses", loses);
	dataConfig.getConfigurationSection(dataPath).set("Deaths", deaths);
	dataConfig.getConfigurationSection(dataPath).set("Kills", kills);
	String kitPath = dataPath + ".Kits";
	if(dataConfig.getConfigurationSection(kitPath) == null) {
	    dataConfig.createSection(kitPath);
	}
	kitPath += ".";
	for(Entry<Kit, Integer> entry : stat.getKitUseCount().entrySet()) {
	    kitSave(kitPath, entry.getKey(), entry.getValue());
	}
	try {
	this.dataConfig.save(dataFile);
	}catch(Exception e) {
	    
	}
    }
    
    public void kitSave(String path, Kit k, int count) {
        dataConfig.getConfigurationSection(path).set(k.toString(), count);
    }
    
    public PlayerStats kitLoad(String path, String key, PlayerStats s) {
        if(dataConfig.getString(path + "." + key) != null) {
            int count = dataConfig.getInt(path + "." + key);
            Kit k = Kit.valueOf(key);
            if(k == null) {
                k = Kit.kitFromString(key);
            }
            s.initializeKit(k, count);
        }
        return s;
    }
    
    public PlayerStats loadPlayerStats(Player p) {
	String dataPath = dataPathS;
	dataPath += "." + p.getUniqueId().toString();
	int w = 0;
	int l = 0;
	int d = 0;
	int kill = 0;
	if(dataConfig.getString(dataPath + ".Wins") != null) {
	    w = dataConfig.getInt(dataPath + ".Wins");
	    }
	if(dataConfig.getString(dataPath + ".Loses") != null) {
	    l = dataConfig.getInt(dataPath + ".Loses");
	    }
	if(dataConfig.getString(dataPath + ".Deaths") != null) {
        d = dataConfig.getInt(dataPath + ".Deaths");
        }
	if(dataConfig.getString(dataPath + ".Deaths") != null) {
	    kill = dataConfig.getInt(dataPath + ".Kills");
        }
	PlayerStats ps = new PlayerStats(Kit.DEFAULT, w, l, d, kill);
	if(dataConfig.getConfigurationSection(dataPath + ".Kits") != null) {
	for(String s : dataConfig.getConfigurationSection(dataPath + ".Kits").getKeys(false)) {
	    ps = kitLoad(dataPath + ".Kits", s, ps);
	}
	}else {
	    for(Kit k : Kit.values()) {
	        ps.initializeKit(k, 0);
	    }
	}
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
