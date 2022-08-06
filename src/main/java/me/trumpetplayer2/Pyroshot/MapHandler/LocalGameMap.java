package me.trumpetplayer2.Pyroshot.MapHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class LocalGameMap implements GameMap {
	private final File sourceWorldFolder;
	private File activeWorldFolder;
	private String worldName;
	private World bukkitWorld;
	private boolean isLoaded = false;
	
	public String getWorldName() {
		return worldName;
	}
	
	public LocalGameMap(File worldFolder, String clonedWorldName, boolean loadOnInit) {
		worldName = clonedWorldName;
		this.sourceWorldFolder = new File(worldFolder, worldName);
		if(loadOnInit) load();
	}
	
	
	public boolean load(){
		if(isLoaded()) return true;
		this.activeWorldFolder = new File(
				Bukkit.getWorldContainer().getParentFile(),
				sourceWorldFolder.getName()
				);
		try {
			copy(sourceWorldFolder, activeWorldFolder);
		}catch (IOException e) {
			Bukkit.getLogger().severe("Failed to load gamemap from source folder");
			e.printStackTrace();
			return false;
		}
		this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
		
		if(bukkitWorld != null) this.bukkitWorld.setAutoSave(false); isLoaded = true;
		return isLoaded();
	}
	
	public World getBukkitWorld() {
	    return bukkitWorld;
	}
	
	public static void copy(File source, File destination) throws IOException {
		if(source.isDirectory()) {
			if(!destination.exists()) {
				destination.mkdir();
			}
			
			String[] files = source.list();
			if(files == null) return;
			for(String file : files) {
				File newSource = new File(source, file);
				File newDestination = new File(destination, file);
				copy(newSource, newDestination);
			}
		} else {
			InputStream in = new FileInputStream (source);
			OutputStream out = new FileOutputStream(destination);
			
			byte[] buffer = new byte[1024];
			
			int length;
			while((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}
	
	public void unload() {
	    	
		if(bukkitWorld != null) {
		    for(Chunk c : bukkitWorld.getLoadedChunks()) {
			c.unload();
		    }
		    Bukkit.unloadWorld(bukkitWorld, false);
		}
		if(activeWorldFolder != null) delete(activeWorldFolder);
		
		bukkitWorld = null;
		activeWorldFolder = null;
		isLoaded = false;
	}
	
	public static void delete(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File child : files) {
				delete(child);
			}
		}
		file.delete();
	}
	
	public boolean restoreFromSource() {
		unload();
		return load();
	}
	@Override
	public boolean isLoaded() {
		return isLoaded;
	}
	@Override
	public World getWorld() {
		return bukkitWorld;
	}
}
