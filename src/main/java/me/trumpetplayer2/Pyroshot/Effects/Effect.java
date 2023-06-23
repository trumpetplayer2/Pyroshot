package me.trumpetplayer2.Pyroshot.Effects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Effect {
    
    public void oneShotEffect(Player p, EffectType type);
    public void loopEffect(Player p, EffectType type);
    public String getName();
    public ItemStack getIcon();
    
    
}
