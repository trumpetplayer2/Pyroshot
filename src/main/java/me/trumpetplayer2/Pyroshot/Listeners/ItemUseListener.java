package me.trumpetplayer2.Pyroshot.Listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Grenade;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class ItemUseListener implements Listener{
    PyroshotMain plugin;
    public ItemUseListener(PyroshotMain main) {
	plugin = main;
    }
    
    @EventHandler
    public void PotionSplash(PotionSplashEvent e) {
	if(!plugin.game.isActive) {return;}
	if(!(e.getEntity().getShooter() instanceof Player)){return;}
	Player p = (Player) e.getEntity().getShooter();
	ItemStack i = e.getPotion().getItem();
	if(i.getType().equals(Material.POTION)) {
	    //Item is potion, remove empty bottle
	    if(p.getInventory().contains(Material.GLASS_BOTTLE)) {
		p.getInventory().remove(Material.GLASS_BOTTLE);
	    }
	}
	Kit k = plugin.getPlayerStats(p).getKit();
	if(!(k.equals(Kit.WITCH))) {return;}
	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> returnItem(p, i), 20 * 30);
    }
    
    @EventHandler
    public void PlayerItemConsume(PlayerItemConsumeEvent e) {
	if(!plugin.game.isActive) {return;}
	Player p = e.getPlayer();
	Kit k = plugin.getPlayerStats(p).getKit();
	if(!(k.equals(Kit.WITCH))) {return;}
	ItemStack i = e.getItem();
	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> returnItem(p, i), 20 * 30);
    }
    
    @EventHandler
    public void throwBomb(ProjectileLaunchEvent e) {
        if(!plugin.game.isActive) {return;}
        if(!(e.getEntityType().equals(EntityType.SPLASH_POTION))){return;}
        if(!(e.getEntity().getShooter() instanceof Player)) {return;}
        ThrownPotion potion  = (ThrownPotion) e.getEntity();
        Player p = (Player) potion.getShooter();
        if(p == null) {return;}
        Kit k = plugin.getPlayerStats(p).getKit();
        if(!k.equals(Kit.GRENADIER)) {return;}
        //VECTORS DOWN HERE
        double speedBoost = 10;
        e.getEntity().setVelocity(new Vector(e.getEntity().getVelocity().getX() * speedBoost, 
                e.getEntity().getVelocity().getY(), 
                e.getEntity().getVelocity().getZ() * speedBoost));
    }
    
    @EventHandler
    public void grenadeExplosion(PotionSplashEvent e) {
        if(!plugin.game.isActive) {return;}
        if(!(e.getEntity().getShooter() instanceof Player)) {return;}
        ThrownPotion potion = e.getPotion();
        Player p = (Player) potion.getShooter();
        if(p == null) {return;}
        Kit k = plugin.getPlayerStats(p).getKit();
        if(!k.equals(Kit.GRENADIER)) {return;}
        ItemStack i = potion.getItem();
        Grenade g = Grenade.grenadeFromItem(i);
        explode(g, e.getAffectedEntities(), e.getEntity().getLocation().clone());
    }

    public void explode(Grenade g, Collection<LivingEntity> affectedEntities, Location l) {
        switch(g) {
        case DYNAMITE:
            l.getWorld().createExplosion(l, 6f, false, true);
            break;
        case FLASHBANG:
            for(Entity ent : l.getWorld().getNearbyEntities(l, 4, 4, 4)) {
                if(!(ent instanceof LivingEntity)) {continue;}
                LivingEntity e = (LivingEntity) ent;
                e.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 7*20, 1, false, false, false));
                e.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 7*20, 1, false, false, false));
            }
            Location loc2 = l.clone();
            for(double phi=0; phi<=Math.PI; phi+=Math.PI/15) {
                for(double theta=0; theta<=2*Math.PI; theta+=Math.PI/30) {
                  double r = 4;
                  double x = r*Math.cos(theta)*Math.sin(phi);
                  double y = r*Math.cos(phi) + r;
                  double z = r*Math.sin(theta)*Math.sin(phi);
                  
                  loc2.add(x,y,z);
                  l.getWorld().spawnParticle(Particle.END_ROD, loc2, 1, 0F, 0F, 0F, 0.001);
                  loc2.subtract(x, y, z);
              }
            }
            break;
        case FRAG:
            l.getWorld().createExplosion(l, 7f, false, false);
            Random random = new Random();
            for(Entity ent : l.getWorld().getNearbyEntities(l, 4, 4, 4)) {
                if(!(ent instanceof Player)) {continue;}
                Player p = (Player) ent;
                double damage = random.nextDouble(4, 10);
                if(damage > 10) {damage = 10;}
                if(damage < 1) {damage = 1;}
                p.damage(damage);
                }
            break;
        case MOLOTOV:
            l.getWorld().createExplosion(l, 3f, true, false);
            //Fire several blaze fireballs in random directions
            for(int i = 0; i < 30; i++) {
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
            break;
        case WATERBALLOON:
            int radius = 1;
            ArrayList<Block> blocks = new ArrayList<Block>();
            
            for (int x = (l.getBlockX()-radius); x <= (l.getBlockX()+radius); x++) {
                for (int y = (l.getBlockY()-radius); y <= (l.getBlockY()+radius); y++) {
                for (int z = (l.getBlockZ()-radius); z <= (l.getBlockZ()+radius); z++) {
                    Location loc = new Location(l.getWorld(), x, y, z);
                    if (loc.distance(l) <= radius) {
                    blocks.add(loc.getBlock());
                    }
                }
                }
            }
            for(Block b : blocks) {
                b.setType(Material.WATER);
                BlockData bData = b.getBlockData();
                Levelled tempt = (Levelled) bData;
                tempt.setLevel(8);
                b.setBlockData(tempt);
            }
            break;
        default:
            break;
        }
        }
    
//    @EventHandler
//    public void onPearlShot(ProjectileLaunchEvent e) {
//	if (!(e.getEntity() instanceof EnderPearl)) {return;}
//	if(!(e.getEntity() instanceof Player)) {return;}
//	Player p = (Player) e.getEntity();
//	ItemStack i = ConfigHandler.raidPearl;
//	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> checkPearlLanded(p, i), 20*30);
//    }
//    
//    public void checkPearlLanded(Player p, ItemStack i) {
//	if(!plugin.getPlayerStats(p).special) {return;}
//	returnItem(p, i);
//    }
    
    public void returnItem(Player p, ItemStack i) {
	//Item is potion, remove empty bottle
        if(!plugin.game.isActive) {return;}
	if(p.getInventory().contains(Material.GLASS_BOTTLE)) {
	    p.getInventory().remove(Material.GLASS_BOTTLE);
	}
	p.getInventory().addItem(i);
    }
    
}
