package me.trumpetplayer2.Pyroshot.Effects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Effect {
    String Name = "Example Effect";
    ItemStack Icon = new ItemStack(Material.DIRT);
    public void playEffect();
    public String getName();
    public ItemStack getIcon();
}
