package me.trumpetplayer2.Pyroshot.SoftDependencies;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.trumpetplayer2.Pyroshot.PyroshotMain;

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
	    return PyroshotMain.getInstance().PlayerMap.get(p).getWins() + "";
	}
	if(params.equalsIgnoreCase("loses")) {
	    return PyroshotMain.getInstance().PlayerMap.get(p).getLoses() + "";
	}
	return null;
    }
}
