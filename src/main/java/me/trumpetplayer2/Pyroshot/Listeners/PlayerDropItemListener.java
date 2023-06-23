package me.trumpetplayer2.Pyroshot.Listeners;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Debug.Debug;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.TriggerUltimateEvent;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class PlayerDropItemListener implements Listener{
    PyroshotMain plugin;
    public PlayerDropItemListener(PyroshotMain main) {
	plugin = main;
    }
    
    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e) {
	if(!plugin.game.isActive) {return;}
	e.setCancelled(true);
	Player p = e.getPlayer();
	//Check if special is ready
	if(!plugin.getPlayerStats(p).special) {return;}
	TriggerUltimateEvent ev = new TriggerUltimateEvent(p, PyroshotMain.getInstance().getPlayerStats(p));
	//Special is ready
	switch(plugin.getPlayerStats(p).getKit()) {
	case WATER : 
	    plugin.getPlayerStats(p).useSpecial = true;
	    plugin.getPlayerStats(p).special = false;
	    break;
	case SHOTGUN :
	    plugin.getPlayerStats(p).useSpecial = true;
	    plugin.getPlayerStats(p).special = false;
	    break;
	case BUILDER:
	    Bukkit.getPluginManager().callEvent(ev);
	    if(ev.isCancelled()) {return;}
	    Build(p);
	    plugin.getPlayerStats(p).special = false;
	    plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(plugin.getPlayerStats(p).getKit());
	    break;
	case MOSS:
	    Bukkit.getPluginManager().callEvent(ev);
	    if(ev.isCancelled()) {return;}
	    replaceNearBlocks(p, Kit.MOSS.KitSymbol().getType());
	    plugin.getPlayerStats(p).special = false;
	    plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(plugin.getPlayerStats(p).getKit());
	    break;
	case POWER:
	    Bukkit.getPluginManager().callEvent(ev);
	    if(ev.isCancelled()) {return;}
	    double power = Kit.kitPowerMult(Kit.POWER) * ConfigHandler.StageTwoMult;
	    if(power > 99) {
		power = 99;
	    }
	    //Trigger the "TNT" powerup
	    TNTPrimed tnt = (TNTPrimed) p.getWorld().spawn(p.getLocation(), TNTPrimed.class);
	    tnt.setVelocity(p.getLocation().getDirection().normalize().multiply(2.5));
	    tnt.setSource(p);
	    tnt.setYield((float) power);
	    plugin.getPlayerStats(p).useSpecial = false;
	    plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(plugin.getPlayerStats(p).getKit());
	    break;
	case PYROMANIAC :
	    Bukkit.getPluginManager().callEvent(ev);
	    if(ev.isCancelled()) {return;}
	    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30*20, 1, false, false, false));
	    pyre(p);
	    plugin.getPlayerStats(p).useSpecial = false;
        plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(plugin.getPlayerStats(p).getKit());
	    break;
	case GLOW :
	    Bukkit.getPluginManager().callEvent(ev);
        if(ev.isCancelled()) {return;}
        String name = PyroshotMain.getInstance().game.map.getPlayerTeam(p).getName();
        for(Player t : Bukkit.getOnlinePlayers()) {
            if(PyroshotMain.getInstance().game.map.getPlayerTeam(t) != null) {
            if(!PyroshotMain.getInstance().game.map.getPlayerTeam(t).getName().equalsIgnoreCase(name)) {
                t.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 5 * 20, 1, false, false, false));
            }
            }
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10 * 20, 1, false, false, false));
        plugin.getPlayerStats(p).useSpecial = false;
        plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(plugin.getPlayerStats(p).getKit());
        break;
	case SNIPER :
	    Bukkit.getPluginManager().callEvent(ev);
	    quickSnipe(p);
	default: break;
	}
    }
    
    public static void replaceNearBlocks(Player p, Material Block) {
	Location loc = p.getLocation().clone();
	int radius = 5;
	ArrayList<Block> blocks = new ArrayList<Block>();
	for (int x = (loc.getBlockX()-radius); x <= (loc.getBlockX()+radius); x++) {
	    for (int y = (loc.getBlockY()-radius); y <= (loc.getBlockY()+radius); y++) {
		for (int z = (loc.getBlockZ()-radius); z <= (loc.getBlockZ()+radius); z++) {
		    Location l = new Location(loc.getWorld(), x, y, z);
		    if (l.distance(loc) <= radius) {
			blocks.add(l.getBlock());
		    }
		}
	    }
	}
	for(Block b : blocks) {
	    if(b.getType().equals(Material.AIR)) {continue;}
	    if((!b.getType().isSolid()) && (!b.getType().equals(Material.WATER))) {continue;}
	    b.setType(Block);
	}
    }
    
    private void quickSnipe(Player p) {
        plugin.getPlayerStats(p).useSpecial = true;
        plugin.getPlayerStats(p).special = false;
        plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(plugin.getPlayerStats(p).getKit()) * 100;
    }
    
//    private void Build(Player p) {
//	Location underPlayer = p.getLocation().clone().subtract(0, 1, 0);
//	Block wallBlock = underPlayer.getBlock();
//	do {
//		underPlayer.subtract(0, 1, 0);
//		if(Debug.getNMSVersion() < 1.18) {
//		    if(underPlayer.getY() < -64) {
//            //Player is above void
//            wallBlock.setType(Material.OAK_WOOD);}
//		}
//		else if(underPlayer.getY() < 1) {
//			//Player is above void
//			wallBlock.setType(Material.OAK_WOOD);
//		}
//		wallBlock = underPlayer.getBlock();
//	}while(wallBlock.getType() == Material.AIR);
//	
//	//Determine middle block in front of player
//	Location origin = p.getEyeLocation();
//	Vector direction = origin.getDirection();
//	Location centerLocation = origin.clone().add(direction);
//	
//	//Determine corner
//	Location rotated = origin.clone();
//	rotated.setPitch(0);
//	rotated.setYaw(origin.getYaw() - 90);
//	Vector rotation = rotated.getDirection();
//	Location blockLocation = centerLocation.clone().add(rotation).subtract(0, 3 / 2, 0);
//	rotation.multiply(-1);
//	int initialX = blockLocation.getBlockX();
//	int initialZ = blockLocation.getBlockZ();
//	
//	//Build 3x3 walls
//	for (int y = 0; y < 3; y++) {
//	    for (int i = 0; i < 3; i++) {
//	        blockLocation.getBlock().setType(wallBlock.getType());
//	        blockLocation.add(rotation);
//	    }
//	 
//	    blockLocation.add(0, 1, 0); // Increase the height by 1
//	    blockLocation.setX(initialX);
//	    blockLocation.setZ(initialZ);
//	}
//	
//    }
    
    private void Build(Player p) {
        
        //Determine middle block in front of player
        Location origin = p.getEyeLocation();
        Vector direction = ClosestStraight(origin.getPitch(), origin.getYaw()).clone();
        Location centerLocation = origin.clone().add(direction);
        //Determine corner
        Location rotated = origin.clone();
        //rotated.setDirection(direction);
        //Set pitch to 0 bc any other pitches are odd
        rotated.setPitch(0);
        //Clone to make Forward vector
        Location forwarder = rotated.clone();
        //Set respective yaws
        forwarder.setYaw(getClosestYaw(forwarder.getYaw()));
        rotated.setYaw(getClosestYaw(rotated.getYaw() - 90));
        //Get the vectors from the previous 2 locations. I probably could consolidate these
        Vector rotation = rotated.getDirection();
        Vector forward = forwarder.getDirection();
        //Get the block location, which is where we will be working on placing the shield
        Location blockLocation = centerLocation.clone().add(rotation).subtract(0, 3 / 2, 0);
        blockLocation.setDirection(direction);
        //Move location forward twice, this is to prevent shield from spawning on top of player
        blockLocation.add(forward);
        blockLocation.add(forward);
        //Create an "Home" X and Z location for easier use later
        int initialX = blockLocation.getBlockX();
        int initialZ = blockLocation.getBlockZ();
      //Build 3x3 walls
        for (int y = 0; y < 3; y++) {
            blockLocation.add(0, 1, 0); // Increase the height by 1
            blockLocation.add(forward); // Move forward by 1, as this will be 1 block forward and will be reset at end
            for (int i = 0; i < 3; i++) {
                if(!canPlace(blockLocation, 1, 1, 1)) {continue;}
                blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
                blockLocation.add(rotation);
            }
            blockLocation.setX(initialX);
            blockLocation.setZ(initialZ);
        }
        /* KEY: - Farthest from player, s - blockLocation current location, o - Middle Row, x - closest to player
         * Shield should look like this
         *      - - s
         *      - - -
         *      - - -
         */
        //Go to the bottom and back 1 (Player bottom shield)
        blockLocation.subtract(0, 3, 0); 
        //Create the 3 in a row
        for (int i = 0; i < 3; i++) {
            if(!canPlace(blockLocation, 1, 1, 1)) {continue;}
            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
            blockLocation.add(rotation);
        }
        /* Shield should now look like this
         *      - - -
         *      - - -
         *      - - -
         *      o o s
         */
        //Return to original X/Z (Player Left Wall)
        blockLocation.setX(initialX);
        blockLocation.setZ(initialZ);
        blockLocation.subtract(rotation);
        for (int i = 0; i < 3; i++) {
            blockLocation.add(0, 1, 0);
            if(!canPlace(blockLocation, 1, 1, 1)) {continue;}
            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
        }
        /* Shield should now look like this
         *    s - - -
         *    o - - -
         *    o - - -
         *      o o o
         */
      //Return to original X/Z (Player Right Wall)
        blockLocation.setX(initialX);
        blockLocation.setZ(initialZ);
        blockLocation.subtract(0, 3, 0);
        blockLocation.add(rotation);
        blockLocation.add(rotation);
        blockLocation.add(rotation);
        for (int i = 0; i < 3; i++) {
            blockLocation.add(0, 1, 0);
            if(!canPlace(blockLocation, 1, 1, 1)) {continue;}
            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
        }
        /* Shield should now look like this
         *    o - - - s
         *    o - - - o
         *    o - - - o
         *      o o o
         */
        //Top
        blockLocation.setX(initialX);
        blockLocation.setZ(initialZ);
        blockLocation.add(0, 1, 0);
        for (int i = 0; i < 3; i++) {
            if(!canPlace(blockLocation, 1, 1, 1)) {continue;}
            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
            blockLocation.add(rotation);
        }
        /* Shield should now look like this
         *      o o s
         *    o - - - o
         *    o - - - o
         *    o - - - o
         *      o o o
         */
//        //Lets (Not) fill in corners (Because honestly it works fine and idc about this kit tbh, *Literally cutting corners*)
//        //Move towards player 1
//        blockLocation.subtract(forward);
//        //Move right 1
//        blockLocation.add(rotation);
//        if(canPlace(blockLocation, 1, 1, 1)) {
//            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
//        }
//        /* Shield should now look like this
//         *      o o o s
//         *    o - - - o
//         *    o - - - o
//         *    o - - - o
//         *      o o o
//         */
//        blockLocation.subtract(rotation.multiply(4));
//        if(canPlace(blockLocation, 1, 1, 1)) {
//            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
//        }
//        /* Shield should now look like this
//         *    s o o o x
//         *    o - - - o
//         *    o - - - o
//         *    o - - - o
//         *      o o o
//         */
//        blockLocation.subtract(0, 4, 0);
//        if(canPlace(blockLocation, 1, 1, 1)) {
//            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
//        }
//        /* Shield should now look like this
//         *    x o o o x
//         *    o - - - o
//         *    o - - - o
//         *    o - - - o
//         *    s o o o
//         */
//        blockLocation.subtract(rotation.multiply(4));
//        if(canPlace(blockLocation, 1, 1, 1)) {
//            blockLocation.getBlock().setType(Material.END_STONE_BRICKS);
//        }
//        /* Shield should now look like this
//         *    x o o o x
//         *    o - - - o
//         *    o - - - o
//         *    o - - - o
//         *    x o o o s
//         */
    }
    
    private boolean canPlace(Location loc, double x, double y, double z) {
        Collection<Entity> ent = loc.getWorld().getNearbyEntities(loc, x, y, z);
        if(ent != null) {
            for(Entity e : ent) {
                if(e.getType() == EntityType.PLAYER) {
                    Debug.TellConsole(e.getName() + " was in wall");
                    return false;
                }
            }
            return true;
        }
        return true;
    }
    
    private Vector ClosestStraight(float pitch, float yaw) {
        Boolean isNeg = false;
        //Calculate new pitch off of old pitch
        //Pitch - Straight up -90, Straight down 90, Forward 0
        if(pitch < 22.5 && pitch > -22.5) {
            pitch = 0;
        }else if(pitch < -22.5) {
            pitch = -90;
        }else if(pitch > 22.5) {
            pitch = 90;
        }
        //Yaw - North 0, East 90, South 180, West 270
        if(yaw < 45 && yaw > -45) {
            yaw = 0;
        }else if(yaw >= 45 && yaw < 135) {
            yaw = 90;
        }else if(yaw >= 135 && yaw <= 180 || yaw <= -135 && yaw >= -180) {
            yaw = 180;
            isNeg = true;
        }else if(yaw <= -45 && yaw > -135) {
            yaw = 270;
            isNeg = true;
        }
        //Calculate x, y and z
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);
        if(isNeg) {
            x = -x;
            z = -z;
        }
        //Generate and return new direction
        return new Vector(x,y,z);
    }

    
    private float getClosestYaw(float original) {
        Debug.TellConsole("Original: " + original);
        if(original < -180) {
            original += 360;
        }
        if(original > 180) {
            original -= 360;
        }
        if(original < 45 && original > -45) {
            return 0;
        }else if(original >= 45 && original < 135) {
            return 90;
        }else if(original >= 135 && original <= 180 || original <= -135 && original >= -180) {
            return 180;
        }else if(original <= -45 && original > -135) {
            return -90;
        }else {
            return 0;
        }
    }
    
    public void pyre(Player p) {
        Location l = p.getLocation().clone();
        l.setY(l.getY() + 2);
        for(int i = 0; i < 50; i++) {
            Vector direction = new Vector(0.0 + Math.random() - Math.random(), 0.0 + Math.random() - Math.random(), 0.0 + Math.random() - Math.random());
            SmallFireball fire = (SmallFireball) l.getWorld().spawnEntity(l, EntityType.SMALL_FIREBALL);
            fire.setGravity(true);
            l.setDirection(direction);
        }
        
        Location loc3 = l.clone();
        for(double phi=0; phi<=Math.PI; phi+=Math.PI/15) {
            for(double theta=0; theta<=2*Math.PI; theta+=Math.PI/30) {
              double r = 4;
              double x = r*Math.cos(theta)*Math.sin(phi);
              double y = r*Math.cos(phi) + r;
              double z = r*Math.sin(theta)*Math.sin(phi);
              
              loc3.add(x,y,z);
              l.getWorld().spawnParticle(Particle.FLAME, loc3, 1, 0F, 0F, 0F, 0.001);
              loc3.subtract(x, y, z);
          }
        }
    }
}
