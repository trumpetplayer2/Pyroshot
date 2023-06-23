package me.trumpetplayer2.Pyroshot.PlayerStates;

import java.util.HashMap;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Effects.Effect;


public class PlayerStats{
    //Player W/L
    int wins = 0;
    int loses = 0;
    int deaths = 0;
    int kills = 0;
    Effect winEffect = PyroshotMain.getInstance().getWinEffect().get(0);
    Effect deathEffect = PyroshotMain.getInstance().getDeathEffect().get(0);
    public int shotsSinceReset = 0;
    //Team and current kit
    public PyroshotTeam team;
    Kit kit;
    private HashMap<Kit, Integer> kitUseCount = new HashMap<Kit,Integer>();
    //Retrieve the W/L count
    public int getWins() {return wins;}
    public int getLoses() {return loses;}
    public int getDeaths() {return deaths;}
    //Is special prepped?
    public boolean special = false;
    public boolean useSpecial = false;
    public boolean freeze = false;
    //Elimination Message
    public String eliminationMessage = "(player) was eliminated by (killer)";
    //Cooldown for special
    public int specialCooldown = 0;
    //Grab player data from file and initialize
    public PlayerStats(Kit k, PyroshotTeam t, int w, int l) {
	kit = k;
	team = t;
	wins = w;
	loses = l;
    }
    
    public PlayerStats(Kit k, int w, int l, int d, int kill) {
	kit = k;
	wins = w;
	loses = l;
	deaths = d;
	kills = kill;
    }
    
    public PlayerStats() {
	kit = Kit.DEFAULT;
	team = new PyroshotTeam("Random", null);
    }
    
    public void setTeam(PyroshotTeam t) {
	team = t;
    }
    
    public PyroshotTeam getTeam() {
	return team;
    }
    
    public void addWins(int i) {
	wins = wins + i;
    }
    
    public void addLoses(int i) {
	loses = loses + i;
    }
    
    public void removeWins(int i) {
	wins = wins - i;
    }
    
    public void removeLoses(int i) {
	loses = loses - i;
    }
    
    public void setWins(int i) {
	wins = i;
    }
    
    public void setLoses(int i) {
	loses = i;
    }
    
    public void setKit(Kit k) {
	kit = k;
    }
    
    public Kit getKit() {
	return kit;
    }

    public void incrementKit() {
        if(!kitUseCount.containsKey(getKit())) {
            kitUseCount.put(getKit(), 1);
        }else {
            kitUseCount.put(getKit(), kitUseCount.get(getKit()) + 1);
        }
    }
    public HashMap<Kit, Integer> getKitUseCount(){
        return kitUseCount;
    }
    public void initializeKit(Kit k, int count) {
        kitUseCount.put(k, count);
    }
    public Kit mostUsedKit() {
        if(kitUseCount.isEmpty()) {
            return Kit.DEFAULT;
        }
        int highest = 0;
        Kit kit = Kit.DEFAULT;
        for(Kit k : Kit.values()) {
            if(!kitUseCount.containsKey(k)) {
                continue;
            }
            if(kitUseCount.get(k) > highest) {
                kit = k;
                highest = kitUseCount.get(k);
            }
        }
        return kit;
    }
    public int getKitUseCount(Kit k) {
        if(!kitUseCount.containsKey(k)) {
            return 0;
        }else {
            return kitUseCount.get(k);
        }
    }
    public void incrementDeaths(int amount) {
        deaths += amount;
    }
    public void incrementKills(int amount) {
        kills += amount;
    }
    public int getKills() {
        return kills;
    }
    public Effect getWinEffect() {
        return winEffect;
    }
    public Effect getDeathEffect() {
        return deathEffect;
    }
    public String getDeathMessage() {
        return eliminationMessage;
    }
    
    public void setDeathMessage(String msg) {
        eliminationMessage = msg;
    }
    
    public void setWinEffect(Effect e) {
        winEffect = e;
    }
    
    public void setDeathEffect(Effect e) {
        deathEffect = e;
    }
}
