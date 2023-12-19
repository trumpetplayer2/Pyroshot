package me.trumpetplayer2.Pyroshot.Saves;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.trumpetplayer2.Pyroshot.Debug.Debug;

public class LanguageFiles {
    private FileConfiguration dataConfig;
    private File directory;
    private File dataFile;
    private String lang;
    private JavaPlugin plugin;
    
    public LanguageFiles(String langfile, FileConfiguration config, File file, JavaPlugin main) {
    dataConfig = config;
    dataFile = file;
    lang = langfile;
    plugin = main;
    getData();
    }
    
    public LanguageFiles(JavaPlugin main, String langfile) {
    plugin = main;
    lang = langfile;
    getData();
    }
    
    public HashMap<String, String> loadKeys() {
        //Place to store the Keys
        HashMap<String, String> localizationValues = new HashMap<String, String>();
        for(String k : dataConfig.getKeys(true)) {
            if(dataConfig.getString(k) != null) {
                localizationValues.put(k, dataConfig.getString(k));
            }else {
                localizationValues.put(k, "Translation Error");
            }
        }
        return localizationValues;
    }
    
    public FileConfiguration getData() {
        if(dataConfig == null) {reloadData();}
        
        return dataConfig;
    }
    
    public void reloadData() {
        if(directory == null) {
            directory = new File(plugin.getDataFolder(), "lang");
            if(!directory.exists()) {
                directory.mkdir();
            }
        }
        if(this.dataFile == null) {
            this.dataFile = new File(directory, lang + ".yml");
            Debug.TellConsole(lang + ".yml");
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        InputStream inStream = plugin.getResource(lang + ".yml");
        if(inStream != null) {
        YamlConfiguration dataFile = YamlConfiguration.loadConfiguration(new InputStreamReader(inStream));
        dataConfig.setDefaults(dataFile);
        }
    }
    
    public void saveDefaultConfig() {
        if(dataFile == null) {
        dataFile = new File(plugin.getDataFolder(), lang + ".yml");
        }
    }
    
    public void addKey(String Key, String Value) {
        //Add a key if it did not exist
        dataConfig.set(Key, Value);
    }
    
    public void generateDefaultValues() {
        //Save a default value list, will be in english (Go through default and save them using addKey)
        
    }
}
