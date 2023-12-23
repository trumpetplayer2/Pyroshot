package me.trumpetplayer2.Pyroshot.Listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Panda.Gene;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import net.md_5.bungee.api.ChatColor;

public class OwlKitListeners implements Listener {
    
    
    @EventHandler
    public void onEggThrow(ProjectileLaunchEvent e) {
        if(!PyroshotMain.getInstance().game.isActive) {return;}
        if(e.getEntity() == null) {return;}
        if(!(e.getEntity() instanceof Egg)) {return;}
        //Egg was thrown! Check who threw
        if(e.getEntity().getShooter() == null) {return;}
        if(!(e.getEntity().getShooter() instanceof Player)) {return;}
        Egg egg = (Egg) e.getEntity();
        Player p = (Player) egg.getShooter();
        //Check that the player is using the owlet kit
        if(!(PyroshotMain.getInstance().getPlayerStats(p).getKit().equals(Kit.OWLET))){ return;}
        //We got an owl! Proceed!
        Vector eggVelocity = egg.getVelocity();
        double y = egg.getVelocity().getY();
        eggVelocity.multiply(2.5);
        eggVelocity.setY(y);
        egg.setVelocity(eggVelocity);
    }
    
    
    @EventHandler
    public void onEggHit(ProjectileHitEvent e) {
        if(!PyroshotMain.getInstance().game.isActive) {return;}
        if(e.getEntity() == null) {return;}
        if(!(e.getEntity() instanceof Egg)) {return;}
        //Egg was thrown! Check who threw
        if(e.getEntity().getShooter() == null) {return;}
        if(!(e.getEntity().getShooter() instanceof Player)) {return;}
        Egg egg = (Egg) e.getEntity();
        Player p = (Player) egg.getShooter();
        //Check that the player is using the owlet kit
        if(!(PyroshotMain.getInstance().getPlayerStats(p).getKit().equals(Kit.OWLET))){ return;}
        //We got an owl! Proceed!
        Random rand = new Random();
        switch(rand.nextInt(0, 4)) {
        case 0:
            spawnegg(e.getEntity().getLocation());
            break;
        case 1:
            runAway(e.getEntity().getLocation());
            break;
        case 2:
            popcorn(e.getEntity().getLocation());
        default: break;
        }
        //Anything else
    }
    
    public void spawnegg(Location l) {
        //Determine entity via random choice
        Random rand = new Random();
        switch(rand.nextInt(0, 8)) {
        case 0:
            //Chicken named Owlet
            Chicken c = (Chicken) l.getWorld().spawnEntity(l, EntityType.CHICKEN);
            c.setCustomName(ChatColor.WHITE + "" + ChatColor.BOLD + "Owlet");
            break;
        case 1:
            //Killer Bunny named Emotional Support Bun
            Rabbit b = (Rabbit) l.getWorld().spawnEntity(l, EntityType.RABBIT);
            b.setRabbitType(Type.THE_KILLER_BUNNY);
            b.setCustomName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Emotional Support Bun");
            break;
        case 2:
            //Vindicator (Johnny)
            Vindicator Johnny = (Vindicator) l.getWorld().spawnEntity(l, EntityType.VINDICATOR);
            Johnny.setCustomName("Johnny");
            break;
        case 3:
            //Rainbow Shep
            Sheep s = (Sheep) l.getWorld().spawnEntity(l, EntityType.SHEEP);
            s.setCustomName("jeb_");
            break;
        case 4:
            //Dog named Cubby
            Wolf w = (Wolf) l.getWorld().spawnEntity(l, EntityType.WOLF);
            w.setCustomName(ChatColor.LIGHT_PURPLE + "Cubby");
            w.setAngry(true);
            break;
        case 5:
            //Chicken named Geese
            Chicken g = (Chicken) l.getWorld().spawnEntity(l, EntityType.CHICKEN);
            g.setCustomName(ChatColor.translateAlternateColorCodes('&', "&3Geese"));
            break;
        case 6:
            //Panda named Floof
            Panda p = (Panda) l.getWorld().spawnEntity(l, EntityType.PANDA);
            p.setCustomName(ChatColor.translateAlternateColorCodes('&', "&0F&fl&0o&fo&0f"));
            p.setMainGene(Gene.BROWN);
            break;
        case 7:
            //Llama named Zhop
            Llama z = (Llama) l.getWorld().spawnEntity(l, EntityType.LLAMA);
            z.setCustomName(ChatColor.translateAlternateColorCodes('&', "&4Zhop"));
            break;
        }
    }
    
    public void runAway(Location loc) {
        //Explode
        loc.getWorld().createExplosion(loc, 1);
    }
    
    public void popcorn(Location l) {
        Random rand = new Random();
        for(int i = 0; i < rand.nextInt(5, 10); i++) {
            //Random Direction time!
            LlamaSpit spit = (LlamaSpit) l.getWorld().spawnEntity(l, EntityType.LLAMA_SPIT);
            spit.setVelocity(new Vector(rand.nextFloat(-1, 1), 1, rand.nextFloat(-1, 1)));
            spit.setCustomName("Popcorn");
        }
    }
}
