package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;

public class PlayerFireball {
    
    public static void LaunchSnowball(Player p, Arrow temp, PlayerStats stat, float force) {
	p.launchProjectile(Snowball.class, temp.getVelocity().multiply(Kit.kitShotSpeedMult(Kit.WATER)));
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
	    //Sniper kit cooldown reduces damage
	    if(stat.specialCooldown == 0) {
		power = power*2;
	    }else {
		power = power * (1/stat.specialCooldown);
	    }
	    
	}
	//Cap Power at 99.
	if(power > 99) {
	    power = 99;
	}
	p.launchProjectile(Fireball.class, temp.getVelocity().multiply(Kit.kitShotSpeedMult(kit))).setYield(power);
	if(stat.getKit().equals(Kit.SNIPER)) {
	    stat.specialCooldown = Kit.baseCooldown(Kit.SNIPER);
	}
    }
    
    public static void ShotgunFireball(Player p, Arrow temp, PyroshotMain plugin, float force) {
	for(int i = 0; i < 5; i++) {
	    //This will shoot 5 equal power "Shotgun" shots
	    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Shotgun(p, temp, plugin, force), 5*i);
	}
	plugin.PlayerMap.get(p).useSpecial = false;
	plugin.PlayerMap.get(p).specialCooldown = Kit.baseCooldown(Kit.SHOTGUN);
    }
    
    private static void Shotgun(Player p, Arrow temp, PyroshotMain plugin, float force) {
	if(!plugin.game.isActive) {return;}
	if(!(p.getGameMode().equals(GameMode.ADVENTURE) || p.getGameMode().equals(GameMode.SURVIVAL))) {return;}
	Kit kit = plugin.PlayerMap.get(p).getKit();
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
	p.launchProjectile(Fireball.class, direction).setYield(power);
	plugin.PlayerMap.get(p).useSpecial = false;
	plugin.PlayerMap.get(p).specialCooldown = Kit.baseCooldown(kit);
	p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1f); 
    }
}
