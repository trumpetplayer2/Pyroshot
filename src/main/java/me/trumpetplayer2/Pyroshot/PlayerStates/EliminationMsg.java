package me.trumpetplayer2.Pyroshot.PlayerStates;


import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;

public class EliminationMsg {
    ItemStack Display;
    String msg;
    
    public EliminationMsg(@NotNull Material Icon, @NotNull ArrayList<String> ItemInfo, @NotNull String message) {
        msg = message;
        String itemName = Icon.toString();
        if(ItemInfo.size() > 0) {
            itemName = ItemInfo.get(0);
            ItemInfo.remove(0);
        }
        
        ItemStack ico = new ItemStack(Icon);
        ItemMeta icoMeta = ico.getItemMeta();
        
        icoMeta.setDisplayName(itemName);
        ItemInfo.add(ChatColor.GOLD + "Preview: " + ChatColor.translateAlternateColorCodes('&', msg));
        icoMeta.setLore(ItemInfo);
        ico.setItemMeta(icoMeta);
        Display = ico;
    }
    
    public ItemStack getIcon() {
        return Display;
    }
    
    public String getMsg() {
        return msg;
    }
}
