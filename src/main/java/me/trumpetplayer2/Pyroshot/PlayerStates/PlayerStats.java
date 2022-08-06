package me.trumpetplayer2.Pyroshot.PlayerStates;

public class PlayerStats{
    //Player W/L
    int wins = 0;
    int loses = 0;
    //Team and current kit
    public PyroshotTeam team;
    Kit kit;
    //Retrieve the W/L count
    public int getWins() {return wins;}
    public int getLoses() {return loses;}
    //Is special prepped?
    public boolean special = false;
    public boolean useSpecial = false;
    public boolean freeze = false;
    //Elimination Message
    public String eliminationMessage;
    //Cooldown for special
    public int specialCooldown = 0;
    //Grab player data from file and initialize
    public PlayerStats(Kit k, PyroshotTeam t, int w, int l) {
	kit = k;
	team = t;
	wins = w;
	loses = l;
    }
    
    public PlayerStats(Kit k, int w, int l) {
	kit = k;
	wins = w;
	loses = l;
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
}
