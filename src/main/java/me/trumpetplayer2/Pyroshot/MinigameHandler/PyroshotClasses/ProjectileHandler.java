package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;

public class ProjectileHandler {
    
    public static void LaunchSnowball(Player p, Arrow temp, PlayerStats stat, float force) {
	Snowball s = p.launchProjectile(Snowball.class, temp.getVelocity().multiply(Kit.kitShotSpeedMult(Kit.WATER)));
	s.setGravity(false);
    }
    
    public static void LaunchFireball(Player p, Arrow temp, PlayerStats stat, float force) {
	Kit kit = stat.getKit();
	float power = 0;
	float stageMult = 0;
	if(force >= 0.2 && force != 1) {
	    stageMult = ConfigHandler.StageTwoMult;
	}else if(force == 1) {
	    stageMult = ConfigHandler.StageThreeMult;
	}else {
	    stageMult = ConfigHandler.StageOneMult;
	}
	//Modify power based on kit
	power = force * stageMult * Kit.kitPowerMult(kit);
	if(kit == Kit.SNIPER) {
	    if(stat.useSpecial) {
	        power = power*2;
	    }else {
	    //Sniper kit cooldown reduces damage
	    if(stat.specialCooldown == 0) {
		power = power*2;
	    }else {
		power = power * (1/stat.specialCooldown);
	    }
	    }
	}else if(kit == Kit.PYROMANIAC) {
	    //Count nearby fire
	    final double radius = 5d;
	    final double xCoord = p.getLocation().getX();
	    final double yCoord = p.getLocation().getY();
	    final double zCoord = p.getLocation().getZ();
	    for (double x = xCoord - radius; x < xCoord + radius; x++) {
	        for (double y = yCoord - radius; y < yCoord + radius; y++) {
	            for (double z = zCoord - radius; z < zCoord + radius; z++) {
	                final Block b = new Location(p.getWorld(), x, y, z).getBlock();
	                if(!b.getType().equals(Material.FIRE)) {
	                    continue;
	                }
	                power += 0.1;
	            }
	        }
	    }
	}
	//Cap Power at 99.
	if(power > 99) {
	    power = 99;
	}
	Fireball shot = p.launchProjectile(Fireball.class, temp.getVelocity().normalize().multiply(Kit.kitShotSpeedMult(kit)));
	shot.setYield(power);
	shot.setShooter(p);
	shot.setGravity(false);
	if(stat.getKit().equals(Kit.SNIPER)) {
	    if(stat.useSpecial) {
	        stat.useSpecial = false;
	        stat.specialCooldown = Kit.baseCooldown(Kit.SNIPER)*2;
	    }else {
	        int cooldown = Kit.baseCooldown(Kit.SNIPER);
	        if(stat.specialCooldown == 0) {
	            stat.shotsSinceReset += 1;
	            cooldown -= stat.shotsSinceReset;
	        }else {
	            stat.shotsSinceReset = 0; 
	        }
	        if(cooldown < 3) {
	            cooldown = 3;
	        }
	        stat.specialCooldown = cooldown;
	    }
	}
    }
    
    public static void ShotgunFireball(Player p, Arrow temp, PyroshotMain plugin, float force) {
	for(int i = 0; i < 5; i++) {
	    //This will shoot 5 equal power "Shotgun" shots
	    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Shotgun(p, temp, plugin, force), 5*i);
	}
	plugin.getPlayerStats(p).useSpecial = false;
	plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(Kit.SHOTGUN);
    }
    
    private static void Shotgun(Player p, Arrow temp, PyroshotMain plugin, float force) {
	if(!plugin.game.isActive) {return;}
	if(!(p.getGameMode().equals(GameMode.ADVENTURE) || p.getGameMode().equals(GameMode.SURVIVAL))) {return;}
	Kit kit = plugin.getPlayerStats(p).getKit();
	float power = 0;
	float stageMult = 0;
	if(force >= 0.2 && force != 1) {
	    stageMult = ConfigHandler.StageTwoMult;
	}else if(force == 1) {
	    stageMult = ConfigHandler.StageThreeMult;
	}else {
	    stageMult = ConfigHandler.StageOneMult;
	}
	//Modify power based on kit
	power = force * stageMult * Kit.kitPowerMult(kit);
	//Cap Power at 99.
	if(power > 99) {
	    power = 99;
	}
	float accuracy = 1.0F;
	Vector direction = temp.getVelocity().clone();
	Vector skew = new Vector(Math.random()* accuracy-accuracy/2, Math.random()*accuracy-accuracy/2, Math.random()*accuracy-accuracy/2);
	direction.add(skew);
	Fireball shot = p.launchProjectile(Fireball.class, direction);
	shot.setYield(power);
    shot.setShooter(p);
    shot.setGravity(false);
	plugin.getPlayerStats(p).useSpecial = false;
	plugin.getPlayerStats(p).specialCooldown = Kit.baseCooldown(kit);
	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f); 
    }
}
