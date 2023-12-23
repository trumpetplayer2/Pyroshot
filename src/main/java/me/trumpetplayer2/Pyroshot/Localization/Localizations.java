package me.trumpetplayer2.Pyroshot.Localization;

import java.util.HashMap;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Saves.LanguageFiles;

public class Localizations {
    String language = "";
    HashMap<String, String> translationValues = new HashMap<String, String>();
    LanguageFiles langRef = null;
    
    public Localizations(String lang) {
        language = lang;
        updateLocalizations();
    }
    
    private void updateLocalizations() {
        LanguageFiles lang = new LanguageFiles(PyroshotMain.getInstance(), language);
        translationValues = lang.loadKeys();
        langRef = lang;
    }
    
    public String getLang() {
        return language;
    }
    
    public String getLocalization(String key) {
        //Get key from hashmap, return the translated value
        if(!translationValues.containsKey(key)) {
            //Add a key to the translation
            langRef.addKey(key, key);
            translationValues.put(key, key);
            return "Error - Key does not exist. Please add translation";
        }
        return translationValues.get(key);
    }
}
