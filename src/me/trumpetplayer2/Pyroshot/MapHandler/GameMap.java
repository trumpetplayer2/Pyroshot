package me.trumpetplayer2.Pyroshot.MapHandler;

import org.bukkit.World;

public interface GameMap {
    boolean load();
    void unload();
    boolean restoreFromSource();
    boolean isLoaded();
    World getWorld();
    String getWorldName();
}
