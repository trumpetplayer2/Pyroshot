package me.trumpetplayer2.Pyroshot.Effects;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.trumpetplayer2.Pyroshot.Debug.Debug;
import net.md_5.bungee.api.ChatColor;

public class Smite implements Effect {
    ItemStack icon;
    public Smite() {
        ItemStack smite;
        if(Debug.getNMSVersion() > 1.17) {
            smite = new ItemStack(Material.LIGHTNING_ROD);
        }else {
            smite = new ItemStack(Material.IRON_INGOT);
        }
        ItemMeta smiteMeta = smite.getItemMeta();
        smiteMeta.setDisplayName(getName());
        ArrayList<String> smiteLore = new ArrayList<String>();
        smiteLore.add("Die in style");
        smiteLore.add("A classic");
        smite.setItemMeta(smiteMeta);
        icon = smite;
    }
    
    @Override
    public void oneShotEffect(Player p, EffectType type) {
        if(type == EffectType.DIE) {
        //Strike players location
        p.getLocation().getWorld().strikeLightning(p.getLocation());
        }
    }

    @Override
    public void loopEffect(Player p, EffectType type) {
        if(type == EffectType.WIN) {
        //Repeated smiting, will only occur on win effect
        p.getLocation().getWorld().strikeLightning(p.getLocation());
        }
    }

    @Override
    public String getName() {
        return ChatColor.YELLOW + "Smite";
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

}
