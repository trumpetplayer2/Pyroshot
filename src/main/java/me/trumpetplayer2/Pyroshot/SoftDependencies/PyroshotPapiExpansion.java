package me.trumpetplayer2.Pyroshot.SoftDependencies;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class PyroshotPapiExpansion extends PlaceholderExpansion{

    @Override
    public @NotNull String getIdentifier() {
	// TODO Auto-generated method stub
	return "Pyroshot";
    }

    @Override
    public @NotNull String getAuthor() {
	return "Trumpetplayer2";
    }

    @Override
    public @NotNull String getVersion() {
	return "2.0.0";
    }
    
    @Override
    public boolean canRegister() {
	return true;
    }
    
    @Override
    public boolean persist() {
	return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player p, String params) {
	if(p == null) {
	    return "";
	}
	if(params.equalsIgnoreCase("wins")) {
	    return PyroshotMain.getInstance().getPlayerStats(p).getWins() + "";
	}
	if(params.equalsIgnoreCase("loses")) {
	    return PyroshotMain.getInstance().getPlayerStats(p).getLoses() + "";
	}
	if(params.equalsIgnoreCase("deaths")) {
	    return PyroshotMain.getInstance().getPlayerStats(p).getDeaths() + "";
	}
	if(params.equalsIgnoreCase("kills")) {
        return PyroshotMain.getInstance().getPlayerStats(p).getKills() + "";
    }
	if(params.equalsIgnoreCase("mostusedkits")) {
	    return WordUtils.capitalizeFully(PyroshotMain.getInstance().getPlayerStats(p).mostUsedKit().toString());
	}
	for(Kit k : Kit.values()) {
	    if(params.equalsIgnoreCase(k.toString().toLowerCase())) {
	        return PyroshotMain.getInstance().getPlayerStats(p).getKitUseCount(k) + "";
	    }
	}
	return null;
    }
}
