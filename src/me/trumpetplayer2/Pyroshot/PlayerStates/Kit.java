package me.trumpetplayer2.Pyroshot.PlayerStates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.trumpetplayer2.Pyroshot.Debug.Debug;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Weapons;

public enum Kit {
    DEFAULT,
    POWER,
    WATER,
    SHOTGUN,
    ENDER,
    BUFFER,
    BUILDER,
    WITCH,
    SNIPER,
    MOSS;
    
    public static int numberOfKits = getNumberOfKits();
    
    public static boolean isHidden(Kit k) {
	boolean isHidden = false;
	switch(k) {
	default: isHidden = false;
	break;
	}
	return isHidden;
    }
    
    private static int getNumberOfKits() {
	int numOfKits = 0;
	for(int i = 0; i < (Kit.values().length); i++) {
		if(Kit.isHidden(Kit.kitFromInt(i))) {continue;}
		numOfKits += 1;
	}
	return numOfKits;
    }

    public boolean isHidden() {
	return isHidden(this);
    }
    
    public static Kit kitFromString(String s) {
	Kit kit = DEFAULT;
	switch(s.toLowerCase()) {
	case "power" : kit = POWER;
		break;
	case "power shot" : kit = POWER;
		break;
	case "water" : kit = WATER;
		break;
	case "water bomb" : kit = WATER;
		break;
	case "shotgun" : kit = SHOTGUN;
		break;
	case "ender" : kit = ENDER;
		break;
	case "buffer" : kit = BUFFER;
		break;
	case "builder" : kit = BUILDER;
		break;
	case "witch" : kit = WITCH;
		break;
	case "sniper" : kit = SNIPER;
		break;
	case "moss" : kit = MOSS;
		break;
	default: kit = DEFAULT;
		break;
	}
	
	return kit;
    }
    public static Kit kitFromInt(int i) {
	Kit kit = DEFAULT;
	switch(i) {
	case 1 : kit = POWER;
		break;
	case 2 : kit = WATER;
		break;
	case 3 : kit = SHOTGUN;
		break;
	case 4 : kit = ENDER;
		break;
	case 5 : kit = BUFFER;
		break;
	case 6 : kit = BUILDER;
		break;
	case 7 : kit = WITCH;
		break;
	case 8 : kit = SNIPER;
		break;
	case 9 : kit = MOSS;
		break;
	default: kit = DEFAULT;
		break;
	}
	
	return kit;
    }
    public static int baseCooldown(Kit k) {
	int cooldown = 0;
	switch(k) {
	case DEFAULT	 : cooldown = 5; break;
	case POWER	 : cooldown = 300; break;
	case WATER	 : cooldown = 300; break;
	case SHOTGUN	 : cooldown = 15; break;
	case ENDER	 : cooldown = 300; break;
	case BUFFER	 : cooldown = 300; break;
	case BUILDER	 : cooldown = 300; break;
	case WITCH	 : cooldown = 300; break;
	case SNIPER	 : cooldown = 7; break;
	case MOSS	 : cooldown = 300; break;
	}
	return cooldown;
    }
    //Mult for fireball when fired
    public static float kitPowerMult(Kit k) {
	float mult = 0f;
	switch(k) {
	case POWER : mult = 1.5f; break;
	default: mult = 1.0f; break;
	}
	return mult;
    }
    public static float kitShotSpeedMult(Kit k) {
	float mult = 0;
	switch(k) {
	case SNIPER : mult = 1.5f; break;
	default: mult = 1.0f; break;
	}
	return mult;
    }
    public static boolean hasPermission(Player p, Kit k) {
	//Dynamically check kit permission
	return p.hasPermission("Pyroshot.Minigame.Kits." + k.kitToString());
    }
    public static String kitToString(Kit k) {
	String s = "default";
	switch(k) {
    	case POWER : 
    	    s = "Power";
    	    break;
	
	case WATER :
	    s = "Water";
	    break;
		
	case SHOTGUN :
	    s = "Shotgun";
	    break;
		
	case ENDER :
	    s = "Ender";
	    break;
		
	case BUFFER :
	    s = "Buffer";
	    break;
		
	case BUILDER :
	    s = "Builder";
	    break;
		
	case WITCH :
	    s = "Witch";
	    break;
		
	case SNIPER :
	    s = "Sniper";
	    break;
	
	case MOSS :
	    s = "Moss";
	    break;
	default: 
	    s = "Default";
	}
	return s;
    }
    public static boolean doubleJump(Kit k) {
	boolean canDoubleJump = true;
	
	switch(k) {
	case POWER : canDoubleJump = false; break;
	default : canDoubleJump = true; break;
	}
	
	return canDoubleJump;
    }
    
    public String kitToString() {
	return (kitToString(this));
    }
    
    public Inventory getInventory() {
	Inventory i = Bukkit.createInventory(null, InventoryType.PLAYER);
	i.setItem(0, Weapons.bow);
	    i.setItem(17, new ItemStack(Material.ARROW));
	//Create each inventory
	switch(this) {
	case POWER : 
	    i.setItem(40, new ItemStack(Material.SHIELD));
		break;
	case BUFFER :
	    ItemStack SpeedPotion = new ItemStack(Material.POTION);
	    ItemStack JumpPotion = new ItemStack(Material.POTION);
	    PotionMeta SpeedMeta = (PotionMeta) SpeedPotion.getItemMeta();
	    PotionMeta JumpMeta = (PotionMeta) JumpPotion.getItemMeta();
	    SpeedMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 2), true);
	    JumpMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 60*20, 2), true);
	    SpeedMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Speed Potion");
	    SpeedMeta.setColor(Color.WHITE);
	    JumpMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Jumpboost Potion");
	    JumpMeta.setColor(Color.AQUA);
	    SpeedPotion.setItemMeta(SpeedMeta);
	    JumpPotion.setItemMeta(JumpMeta);
	    i.setItem(1, SpeedPotion);
	    i.setItem(2, SpeedPotion);
	    i.setItem(3, JumpPotion);
	    break;
	case WITCH :
	    ItemStack HealthPotion = new ItemStack(Material.SPLASH_POTION);
	    ItemStack PoisonPotion = new ItemStack(Material.SPLASH_POTION);
	    PotionMeta HealthMeta = (PotionMeta) HealthPotion.getItemMeta();
	    PotionMeta PoisonMeta = (PotionMeta) PoisonPotion.getItemMeta();
	    HealthMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 3*20, 1), true);
	    PoisonMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 60*20, 2), true);
	    HealthMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Health Potion");
	    HealthMeta.setColor(Color.RED);
	    PoisonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Splash Poison");
	    PoisonMeta.setColor(Color.GREEN);
	    HealthPotion.setItemMeta(HealthMeta);
	    PoisonPotion.setItemMeta(PoisonMeta);
	    i.setItem(2, HealthPotion);
	    i.setItem(3, HealthPotion);
	    i.setItem(4, PoisonPotion);
	    ItemStack wand = new ItemStack(Material.STICK);
	    ItemMeta wandMeta = wand.getItemMeta();
	    wandMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
	    wandMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "uheih" + ChatColor.RESET + "" + ChatColor.DARK_PURPLE + "Wand" + ChatColor.DARK_PURPLE + "" + ChatColor.MAGIC + "uheih");
	    wand.setItemMeta(wandMeta);
	    i.setItem(1, wand);
	    break;
	default:
	    break;
	}
	return i;
    }

    public ItemStack KitSymbol() {
	ItemStack i;
	ItemMeta im;
	//Create each kits symbol
	switch(this) {
    	case POWER : 
    	    i = new ItemStack(Material.TNT);
    	    im = i.getItemMeta();
    	
    	    List<String> PowerShotLore = new ArrayList<String>();
    	    PowerShotLore.add(ChatColor.GOLD + "Power Shot Kit");
    	    PowerShotLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
    	    PowerShotLore.add(ChatColor.GOLD + " + 1x Shield");
    	    PowerShotLore.add(ChatColor.GOLD + " + Bonus Defense");
    	    PowerShotLore.add(ChatColor.GOLD + " + Bonus Power on Regular Shot");
    	    PowerShotLore.add(ChatColor.GOLD + " + Immune to Normal Knockback");
    	    PowerShotLore.add(ChatColor.GOLD + " - Melts in Fire");
    	    PowerShotLore.add(ChatColor.GOLD + " - No Invulnerability");
    	    PowerShotLore.add(ChatColor.GOLD + " - Cannot Double Jump");
    	    PowerShotLore.add(ChatColor.GOLD + " + Special - TNT Fling");
    	    PowerShotLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
	
    	    im.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
    	    im.setLore(PowerShotLore);
    	    im.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Power Shot Kit");
    	    break;
	
	case WATER :
	    i = new ItemStack(Material.WATER_BUCKET);
	    im = i.getItemMeta();
	    
	    List<String> WaterBombLore = new ArrayList<String>();
		WaterBombLore.add(ChatColor.GOLD + "Water Bomb Kit");
		WaterBombLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		WaterBombLore.add(ChatColor.GOLD + " + Double Jump");
		WaterBombLore.add(ChatColor.GOLD + " + Special - Water Shot");
		WaterBombLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.WATER_WORKER, 1, true);
		im.setLore(WaterBombLore);
		im.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Water Bomb Kit");
	    break;
	    
	case SHOTGUN :
	    i = new ItemStack(Material.BOW);
	    im = i.getItemMeta();
	    
	    List<String> ShotgunLore = new ArrayList<String>();
		ShotgunLore.add(ChatColor.GOLD + "Shotgun Kit");
		ShotgunLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		ShotgunLore.add(ChatColor.GOLD + " + Double Jump");
		ShotgunLore.add(ChatColor.GOLD + " + Special - Shotgun");
		ShotgunLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.MULTISHOT, 5, true);
		im.setLore(ShotgunLore);
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Shotgun Kit");
	    break;
		
	case ENDER :
	    i = new ItemStack(Material.ENDER_PEARL);
	    im = i.getItemMeta();
	    
	    List<String> EPearlLore = new ArrayList<String>();
		EPearlLore.add(ChatColor.GOLD + "Ender Kit");
		EPearlLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		EPearlLore.add(ChatColor.GOLD + " + Double Jump");
		EPearlLore.add(ChatColor.GOLD + " + Special - Ender Pillage");
		EPearlLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
		im.setLore(EPearlLore);
		im.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Ender Kit");
		
	    break;
		
	case BUFFER :
	    if(Debug.getNMSVersion() < 1.8) {
	    i = new ItemStack(Material.RABBIT_FOOT);
	    }else {
	        i = new ItemStack(Material.BLAZE_POWDER);
	    }
	    im = i.getItemMeta();
	    
	    List<String> BufferLore = new ArrayList<String>();
		BufferLore.add(ChatColor.GOLD + "Buffer Kit");
		BufferLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		BufferLore.add(ChatColor.GOLD + " + Double Jump");
		BufferLore.add(ChatColor.GOLD + " + 2x Speed Potion");
		BufferLore.add(ChatColor.GOLD + " + 1x Jumpboost Potion");
		BufferLore.add(ChatColor.GOLD + " + Potions regen 30 seconds after use");
		BufferLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.SOUL_SPEED, 3, true);
		im.setLore(BufferLore);
		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Buffer Kit");
		
	    break;
		
	case BUILDER :
	    i = new ItemStack(Material.OAK_PLANKS);
	    im = i.getItemMeta();
	    
	    List<String> BuildLore = new ArrayList<String>();
		BuildLore.add(ChatColor.GOLD + "Builder Kit");
		BuildLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		BuildLore.add(ChatColor.GOLD + " + Double Jump");
		BuildLore.add(ChatColor.GOLD + " + Special - Wall");
		BuildLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.DIG_SPEED, 5, true);
		im.setLore(BuildLore);
		im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Builder Kit");
	    break;
		
	case WITCH :
	    i = new ItemStack(Material.STICK);
	    im = i.getItemMeta();
	    
	    List<String> WitchLore = new ArrayList<String>();
		WitchLore.add(ChatColor.GOLD + "Witch Kit");
		WitchLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		WitchLore.add(ChatColor.GOLD + " + Double Jump");
		WitchLore.add(ChatColor.GOLD + " + 2x Healing Splash Potions");
		WitchLore.add(ChatColor.GOLD + " + 1x Poison Splash Potions");
		WitchLore.add(ChatColor.GOLD + " + 1x Knockback Wand");
		WitchLore.add(ChatColor.GOLD + " + Potions regen 30 secs after use");
		WitchLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.KNOCKBACK, 3, true);
		im.setLore(WitchLore);
		im.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Witch Kit");
		
	    break;
		
	case  SNIPER :
	    if(Debug.getNMSVersion() < 1.16) {
	    i = new ItemStack(Material.HAY_BLOCK);
	    }else {
	        i = new ItemStack(Material.TARGET);
	    }
	    im = i.getItemMeta();
	    
	    List<String> SniperLore = new ArrayList<String>();
		SniperLore.add(ChatColor.GOLD + "Sniper Kit");
		SniperLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		SniperLore.add(ChatColor.GOLD + " + Double Jump");
		SniperLore.add(ChatColor.GOLD + " + Faster Shots");
		SniperLore.add(ChatColor.GOLD + " + More Powerful Base Damage");
		SniperLore.add(ChatColor.GOLD + " - 7 sec cooldown when moving or firing");
		SniperLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		
		im.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
		im.setLore(SniperLore);
		im.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Sniper Kit");
	    break;
		
	case DEFAULT: 
	    i = new ItemStack(Material.BOW);
	    im = i.getItemMeta();
	    
	    List<String> DefaultLore = new ArrayList<String>();
		DefaultLore.add(ChatColor.GOLD + "Default Kit");
		DefaultLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
		DefaultLore.add(ChatColor.GOLD + " + Double Jump");
		DefaultLore.add(ChatColor.GOLD + " - Nothing Special");
		DefaultLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
			
		im.setLore(DefaultLore);
		im.setDisplayName(ChatColor.GRAY + "Default Kit");
		break;
		
	case MOSS:
	    if(Debug.getNMSVersion() < 1.17) {
	    i = new ItemStack(Material.GREEN_WOOL);
	    }else {
	        i = new ItemStack(Material.MOSS_BLOCK);
	    }
	    im = i.getItemMeta();
	    
	    List<String> MossLore = new ArrayList<String>();
	    	MossLore.add(ChatColor.GOLD + "Moss Kit");
	    	MossLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
	    	MossLore.add(ChatColor.GOLD + " + Double Jump");
	    	MossLore.add(ChatColor.GOLD + " + Tough Enviornments");
	    	MossLore.add(ChatColor.GOLD + " - Flammable");
	    	MossLore.add(ChatColor.GOLD + " + Special - Water Absorption");
	    	MossLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
			
	    	im.addEnchant(Enchantment.MENDING, 2, true);
		im.setLore(MossLore);
		im.setDisplayName(ChatColor.GREEN + "" +  ChatColor.BOLD + "Moss Kit");
		break;
	default:
	    i = new ItemStack(Material.BARRIER);
	    im = i.getItemMeta();
	    List<String> UndefinedLore = new ArrayList<String>();
	    	UndefinedLore.add(ChatColor.GOLD + "Undefined Kit");
	    	UndefinedLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
	    	UndefinedLore.add(ChatColor.GOLD + " - This kit has not been Defined");
	    	UndefinedLore.add(ChatColor.GOLD + " - If you see this, please contact the Dev");
	    	UndefinedLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
			
		im.setLore(UndefinedLore);
		im.setDisplayName(ChatColor.DARK_RED + "Undefined Kit");
	}
	i.setItemMeta(im);
	return i;
    }
    
    public boolean hasPermission(Player p) {
	//Dynamically check kit permission
	return(hasPermission(p, this));
    }
    
    public boolean doubleJump() {
	return doubleJump(this);
    }
}