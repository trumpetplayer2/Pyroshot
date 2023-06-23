package me.trumpetplayer2.Pyroshot.Effects;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class Nothing implements Effect {

    ItemStack icon;
    public Nothing() {
        icon = new ItemStack(Material.BARRIER);
        ItemMeta smiteMeta = icon.getItemMeta();
        smiteMeta.setDisplayName(getName());
        ArrayList<String> smiteLore = new ArrayList<String>();
        smiteLore.add("Literally no effect");
        smiteLore.add("Yes, this does NOTHING");
        icon.setItemMeta(smiteMeta);
    }
    
    @Override
    public void oneShotEffect(Player p, EffectType type) {
        //No Effects, Literally NOTHING
    }

    @Override
    public void loopEffect(Player p, EffectType type) {
        //No loop effect here
    }

    @Override
    public String getName() {
        return ChatColor.GRAY + "Nothing";
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }


}
