package me.trumpetplayer2.Pyroshot.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Debug.Debug;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Events.TriggerUltimateEvent;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;

public class IllusionKitListeners implements Listener{
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent e) {
        //Get Player
        Player p = e.getPlayer();
        //Check if the game is active
        if(!PyroshotMain.getInstance().game.isActive) {return;}
        //Check if player is using correct kit
        if(!(PyroshotMain.getInstance().getPlayerStats(p).getKit().equals(Kit.ILLUSION))) return;
        p.setInvisible(e.isSneaking());
    }
    
    @EventHandler
    public void onSpecial(TriggerUltimateEvent e) {
        if(!e.getKit().equals(Kit.ILLUSION)) { return;}
        if(e.isCancelled()) {return;}
        //Get the Player
        Player p = e.getPlayer();
        //Update Special
        PyroshotMain.getInstance().getPlayerStats(p).special = false;
        PyroshotMain.getInstance().getPlayerStats(p).specialCooldown = Kit.baseCooldown(PyroshotMain.getInstance().getPlayerStats(p).getKit());
        //Spawn Armor Stand
        ArmorStand armorStand = (ArmorStand) e.getPlayer().getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta sm = (SkullMeta) playerHead.getItemMeta();
        sm.setOwningPlayer(p);
        playerHead.setItemMeta(sm);
        armorStand.setBasePlate(false);
        armorStand.setArms(true);
        armorStand.getEquipment().setHelmet(playerHead);
        armorStand.getEquipment().setChestplate(p.getEquipment().getChestplate());
        armorStand.getEquipment().setItemInMainHand(p.getEquipment().getItemInMainHand());
        armorStand.getEquipment().setItemInOffHand(p.getEquipment().getItemInOffHand());
        
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.HAND, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, LockType.ADDING_OR_CHANGING);
        
        Location loc2 = p.getLocation().clone();
        for(double phi=0; phi<=Math.PI; phi+=Math.PI/15) {
            for(double theta=0; theta<=2*Math.PI; theta+=Math.PI/30) {
              double r = 4;
              double x = r*Math.cos(theta)*Math.sin(phi);
              double y = r*Math.cos(phi) + r;
              double z = r*Math.sin(theta)*Math.sin(phi);
              
              loc2.add(x,y,z);
              if(Debug.getNMSVersion() >= 1.14) {
                  p.getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, loc2, 1, 0F, 0F, 0F, 0.001);
              }else {
                  p.getLocation().getWorld().spawnParticle(Particle.SMOKE_LARGE, loc2, 1, 0F, 0F, 0F, 0.001);
              }
              loc2.subtract(x, y, z);
            }
          }
    }
}
