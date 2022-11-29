package me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import net.md_5.bungee.api.ChatColor;

public enum Grenade {
    FRAG,
    DYNAMITE,
    MOLOTOV,
    WATERBALLOON,
    FLASHBANG;
    
    public int getCooldown() {
        int cooldown = 30;
        //Grenade Cooldowns
        switch(this) {
        case DYNAMITE:
            cooldown = 20;
            break;
        case FLASHBANG:
            cooldown = 15;
            break;
        case FRAG:
            cooldown = 30;
            break;
        case MOLOTOV:
            cooldown = 25;
            break;
        case WATERBALLOON:
            cooldown = 35;
            break;
        }
        return cooldown;
    }
    
    public ItemStack getSymbol() {
        ItemStack symbol = new ItemStack(Material.SPLASH_POTION);
        PotionMeta symbolMeta = (PotionMeta) symbol.getItemMeta();
        List<String> symbolLore = new ArrayList<String>();
        switch(this) {
        case DYNAMITE:
            symbolMeta.setDisplayName(ChatColor.RED + "Dynamite");
            symbolMeta.setColor(Color.fromRGB(174, 0, 0));
            symbolLore.add(ChatColor.BLUE + "Explosive I");
            break;
        case FLASHBANG:
            symbolMeta.setDisplayName(ChatColor.WHITE + "Flashbang");
            symbolMeta.setColor(Color.fromRGB(255, 255, 255));
            symbolLore.add(ChatColor.BLUE + "Flash I (0:30)");
            break;
        case FRAG:
            symbolMeta.setDisplayName(ChatColor.DARK_GREEN + "Frag Grenade");
            symbolMeta.setColor(Color.fromRGB(17, 85, 0));
            symbolLore.add(ChatColor.BLUE + "Fragmenting I");
            break;
        case MOLOTOV:
            symbolMeta.setDisplayName(ChatColor.GOLD + "Molotov");
            symbolMeta.setColor(Color.fromRGB(239, 159, 0));
            symbolLore.add(ChatColor.BLUE + "Flaming (0:30)");
            break;
        case WATERBALLOON:
            symbolMeta.setDisplayName(ChatColor.BLUE + "Water Balloon");
            symbolMeta.setColor(Color.fromRGB(27, 114, 181));
            symbolLore.add(ChatColor.BLUE + "Water");
            break;
        }
        
        symbol.setItemMeta(symbolMeta);
        return symbol;
    }
    
    public static Grenade grenadeFromItem(ItemStack s) {
        Grenade g;
        switch(ChatColor.stripColor(s.getItemMeta().getDisplayName().toLowerCase())) {
        case "dynamite" :
            g = DYNAMITE;
            break;
        case "flashbang" :
            g = FLASHBANG;
            break;
        case "frag grenade" :
            g = FRAG;
            break;
        case "molotov":
            g = MOLOTOV;
            break;
        case "water balloon":
            g = WATERBALLOON;
            break;
        default: g = FLASHBANG;
        }
        return g;
    }
    
    public static Grenade randomGrenade() {
        Random random = new Random();
        int rand = random.nextInt(0,Grenade.values().length);
        return Grenade.values()[rand];
    }
}
