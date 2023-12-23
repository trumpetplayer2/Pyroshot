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

import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.Debug.Debug;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotClasses.Weapons;

public enum Kit {
    DEFAULT,
    POWER,
    WATER,
    SHOTGUN,
    ENDER,
    BUILDER,
    WITCH,
    SNIPER,
    MOSS,
    MACHINEGUN,
    GRENADIER,
    GLOW,
    PYROMANIAC,
    OWLET,
    ILLUSION;
    
    public static int numberOfKits = getNumberOfKits();
    
    public static boolean isHidden(Kit k) {
	switch(k) {
	case MACHINEGUN : 
	    if(Debug.getNMSVersion() < 1.14) {
	        return true;
	    }
	    return false;
//	case GLOW :
//	    if(PyroshotMain.getInstance().getProtocolLibEnabled()) {
//	        return false;
//	    }
//	    return true;
	default: return false;
	}
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
	case "shotgun" : 
	case "burst" : 
	    kit = SHOTGUN;
		break;
	case "ender" : kit = ENDER;
		break;
	case "builder" : kit = BUILDER;
		break;
	case "witch" : kit = WITCH;
		break;
	case "sniper" : kit = SNIPER;
		break;
	case "moss" : kit = MOSS;
		break;
	case "machine gun" :
	case "machgun" :
	case "machinegun" : 
	case "barrage" :
	    kit = MACHINEGUN;
	    break;
	case "grenadier" : kit = GRENADIER; 
	    break;
	case "glow" : kit = GLOW; 
	    break;
	case "pyromaniac" : kit = PYROMANIAC;
	    break;
	case "owlet" : kit = OWLET;
	    break;
	case "illusion" : kit = ILLUSION;
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
	case 5 : kit = BUILDER;
		break;
	case 6 : kit = WITCH;
		break;
	case 7 : kit = SNIPER;
		break;
	case 8 : kit = MOSS;
		break;
	case 9 : kit = MACHINEGUN;
	    break;
	case 10 : kit = GRENADIER;
	    break;
	case 11 : kit = GLOW;
	    break;
	case 12 : kit = PYROMANIAC;
	    break;
	case 13 : kit = OWLET;
	    break;
	case 14 : kit = ILLUSION;
	    break;
	default: kit = DEFAULT;
		break;
	}
	
	return kit;
    }
    
    public static int startCooldown(Kit k) {
        switch(k) {
        case SNIPER: return 7;
        case BUILDER: return 15;
        
        default : return 30;
        }
    }
    
    public static int baseCooldown(Kit k) {
	int cooldown = 0;
	switch(k) {
	case DEFAULT	 : cooldown = 5; break;
	case POWER	 : cooldown = 300; break;
	case WATER	 : cooldown = 300; break;
	case SHOTGUN	 : cooldown = 15; break;
	case ENDER	 : cooldown = 300; break;
	case BUILDER	 : cooldown = 30; break;
	case WITCH	 : cooldown = 300; break;
	case SNIPER	 : cooldown = 7; break;
	case MOSS	 : cooldown = 300; break;
	case MACHINEGUN : cooldown = 300; break;
    case GLOW: cooldown = 30; break;
    case GRENADIER:cooldown = 300; break;
    case PYROMANIAC: cooldown = 60; break;
    case OWLET : cooldown = 10; break;
    case ILLUSION : cooldown = 120; break;
	}
	return cooldown;
    }
    //Mult for fireball when fired
    public static float kitPowerMult(Kit k) {
	float mult = 0f;
	switch(k) {
	case POWER : mult = 1.5f; break;
	case MACHINEGUN : mult = .5f; break;
	case PYROMANIAC : mult = .75f; break;
	default: mult = 1.0f; break;
	}
	return mult;
    }
    public static float kitShotSpeedMult(Kit k) {
	float mult = 0;
	switch(k) {
	case SNIPER : mult = 2f; break;
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
	    s = "Burst";
	    break;
		
	case ENDER :
	    s = "Ender";
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
	case MACHINEGUN :
	    s = "Barrage";
	    break;
	case GRENADIER :
	    s = "Grenadier";
	    break;
	case GLOW :
	    s = "Glow";
	    break;
	case PYROMANIAC:
	    s = "Pyromaniac";
	    break;
	case OWLET:
	    s = "Owlet";
	    break;
	case ILLUSION:
	    s = "Illusion";
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
    
    public Inventory getInventory(Player p) {
	Inventory i = Bukkit.createInventory(null, InventoryType.PLAYER);
	i.setItem(0, Weapons.bow(p));
	i.setItem(17, new ItemStack(Material.ARROW));
	//Create each inventory
	switch(this) {
	case POWER : 
	    i.setItem(40, new ItemStack(Material.SHIELD));
		break;
	case WITCH :
	    ItemStack HealthPotion = new ItemStack(Material.SPLASH_POTION);
	    ItemStack PoisonPotion = new ItemStack(Material.SPLASH_POTION);
	    PotionMeta HealthMeta = (PotionMeta) HealthPotion.getItemMeta();
	    PotionMeta PoisonMeta = (PotionMeta) PoisonPotion.getItemMeta();
	    HealthMeta.addCustomEffect(new PotionEffect(PotionEffectType.HEAL, 3*20, 1), true);
	    PoisonMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 60*20, 2), true);
	    
	    HealthMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchhealth"));
	    HealthMeta.setColor(Color.RED);
	    PoisonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchpoison"));
	    PoisonMeta.setColor(Color.GREEN);
	    
	    HealthPotion.setItemMeta(HealthMeta);
	    PoisonPotion.setItemMeta(PoisonMeta);
	    i.setItem(2, HealthPotion);
	    i.setItem(3, HealthPotion);
	    i.setItem(4, PoisonPotion);
	    ItemStack wand = new ItemStack(Material.STICK);
	    ItemMeta wandMeta = wand.getItemMeta();
	    wandMeta.addEnchant(Enchantment.KNOCKBACK, 5, true);
	    
	    wandMeta.setDisplayName(PyroshotMain.getInstance().getLocalizedText(p, "witchkbstick"));
	    wand.setItemMeta(wandMeta);
	    i.setItem(1, wand);

        ItemStack SpeedPotion = new ItemStack(Material.POTION);
        ItemStack JumpPotion = new ItemStack(Material.POTION);
        PotionMeta SpeedMeta = (PotionMeta) SpeedPotion.getItemMeta();
        PotionMeta JumpMeta = (PotionMeta) JumpPotion.getItemMeta();
        SpeedMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 2), true);
        JumpMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 60*20, 2), true);
        
        SpeedMeta.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchspeed"));
        SpeedMeta.setColor(Color.WHITE);
        JumpMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchjump"));
        JumpMeta.setColor(Color.AQUA);
        SpeedPotion.setItemMeta(SpeedMeta);
        JumpPotion.setItemMeta(JumpMeta);
        i.setItem(5, SpeedPotion);
        i.setItem(6, SpeedPotion);
        i.setItem(7, JumpPotion);
	    break;
	case MACHINEGUN:
	    //Reset inv
	    i = Bukkit.createInventory(null, InventoryType.PLAYER);
	    i.setItem(17, new ItemStack(Material.ARROW, 64));
	    i.setItem(0, Weapons.machineGun(p));
	    break;
	case SNIPER:
	    if(Debug.getNMSVersion() > 1.17) {
	        ItemStack scope = new ItemStack(Material.SPYGLASS);
	        ItemMeta scopeMeta = scope.getItemMeta();
	        scopeMeta.setDisplayName(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "sniperscope"));
	        scope.setItemMeta(scopeMeta);
	        i.setItem(40, scope);
	        break;
	    }
	default:
	    break;
	}
	return i;
    }

    public ItemStack KitSymbol(Player p) {
	ItemStack i;
	ItemMeta im;
	//Create each kits symbol
	switch(this) {
    	case POWER : 
    	    i = new ItemStack(Material.TNT);
    	    im = i.getItemMeta();
    	
    	    List<String> PowerShotLore = new ArrayList<String>();
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymbolname"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymbolshield"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymboldefense"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymboldamage"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymbolknockback"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymbolflamable"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymboliframes"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "nodoublejump"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "powersymbolspecial"));
    	    PowerShotLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
	
    	    im.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
    	    im.setLore(PowerShotLore);
    	    im.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Power Shot Kit");
    	    break;
	
	case WATER :
	    i = new ItemStack(Material.WATER_BUCKET);
	    im = i.getItemMeta();
	    
	    List<String> WaterBombLore = new ArrayList<String>();
		WaterBombLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "waterbombsymbolname"));
		WaterBombLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		WaterBombLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		WaterBombLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "waterbombsymbolspecial"));
		WaterBombLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		
		im.addEnchant(Enchantment.WATER_WORKER, 1, true);
		im.setLore(WaterBombLore);
		im.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Water Bomb Kit");
	    break;
	    
	case SHOTGUN :
	    i = new ItemStack(Material.BOW);
	    im = i.getItemMeta();
	    
	    List<String> ShotgunLore = new ArrayList<String>();
		ShotgunLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "burstsymbolname"));
		ShotgunLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		ShotgunLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		ShotgunLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "burstsymbolspecial"));
		ShotgunLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		
		im.addEnchant(Enchantment.MULTISHOT, 5, true);
		im.setLore(ShotgunLore);
		im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Burst Kit");
	    break;
		
	case ENDER :
	    i = new ItemStack(Material.ENDER_PEARL);
	    im = i.getItemMeta();
	    
	    List<String> EPearlLore = new ArrayList<String>();
		EPearlLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "endersymbolname"));
		EPearlLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		EPearlLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		EPearlLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "endersymbolspecial"));
		EPearlLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		
		im.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
		im.setLore(EPearlLore);
		im.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Ender Kit");
		
	    break;
		
//	case BUFFER :
//	    if(Debug.getNMSVersion() < 1.8) {
//	    i = new ItemStack(Material.RABBIT_FOOT);
//	    }else {
//	        i = new ItemStack(Material.BLAZE_POWDER);
//	    }
//	    im = i.getItemMeta();
//	    
//	    List<String> BufferLore = new ArrayList<String>();
//		BufferLore.add(ChatColor.GOLD + "Buffer Kit");
//		BufferLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
//		BufferLore.add(ChatColor.GOLD + " + Double Jump");
//		BufferLore.add(ChatColor.GOLD + " + 2x Speed Potion");
//		BufferLore.add(ChatColor.GOLD + " + 1x Jumpboost Potion");
//		BufferLore.add(ChatColor.GOLD + " + Potions regen 30 seconds after use");
//		BufferLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
//		
//		im.addEnchant(Enchantment.SOUL_SPEED, 3, true);
//		im.setLore(BufferLore);
//		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Buffer Kit");
//		
//	    break;
		
	case BUILDER :
	    i = new ItemStack(Material.OAK_PLANKS);
	    im = i.getItemMeta();
	    
	    List<String> BuildLore = new ArrayList<String>();
		BuildLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "buildersymbolname"));
		BuildLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		BuildLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		BuildLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "buildersymbolspecial"));
		BuildLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		
		im.addEnchant(Enchantment.DIG_SPEED, 5, true);
		im.setLore(BuildLore);
		im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Builder Kit");
	    break;
		
	case WITCH :
	    i = new ItemStack(Material.STICK);
	    im = i.getItemMeta();
	    
	    List<String> WitchLore = new ArrayList<String>();
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchsymbolname"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchsymbolhealing"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchsymbolpoison"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchsymbolkbwand"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "witchsymbolpotionrefresh"));
		WitchLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		
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
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "snipersymbolname"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "snipersymbolshotspeed"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "snipersymbolbonusdamage"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "snipersymbolslowfirerate"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "snipersymbolspecial"));
		SniperLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		
		im.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
		im.setLore(SniperLore);
		im.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Sniper Kit");
	    break;
		
	case DEFAULT: 
	    i = new ItemStack(Material.BOW);
	    im = i.getItemMeta();
	    
	    List<String> DefaultLore = new ArrayList<String>();
		DefaultLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "defaultsymbolname"));
		DefaultLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
		DefaultLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
		DefaultLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "defaultsymbolnothing"));
		DefaultLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
			
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
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "mosssymbolname"));
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "mosssymboltough"));
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "mosssymbolflamable"));
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "mosssymbolspecial"));
	    	MossLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
			
	    	im.addEnchant(Enchantment.MENDING, 2, true);
		im.setLore(MossLore);
		im.setDisplayName(ChatColor.GREEN + "" +  ChatColor.BOLD + "Moss Kit");
		break;
	case MACHINEGUN:
        i = new ItemStack(Material.GOLDEN_CARROT);
        im = i.getItemMeta();
        
        List<String> MachLore = new ArrayList<String>();
        MachLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "barragesymbolname"));
        MachLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        MachLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
        MachLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "barragesymbolfirerate"));
        MachLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "barragesymbollowdamage"));
        MachLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        
        im.addEnchant(Enchantment.QUICK_CHARGE, 3, true);
        im.setLore(MachLore);
        im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Barrage Kit");
        break;
	case GRENADIER:
	    i = new ItemStack(Material.FIREWORK_STAR);
        im = i.getItemMeta();
        
        List<String> GrenadierLore = new ArrayList<String>();
        GrenadierLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "grenadiersymbolname"));
        GrenadierLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        GrenadierLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
        GrenadierLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "grenadiersymbolmidrange"));
        GrenadierLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "grenadiersymboleffects"));
        GrenadierLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        
        im.addEnchant(Enchantment.KNOCKBACK, 3, true);
        im.setLore(GrenadierLore);
        im.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "Grenadier Kit");
        break;
	case GLOW:
	    if(Debug.getNMSVersion() < 1.17) {
	        i = new ItemStack(Material.GLOWSTONE);
	        }else {
	            i = new ItemStack(Material.GLOW_BERRIES);
	        }
	        im = i.getItemMeta();
	        
	        List<String> GlowLore = new ArrayList<String>();
	        GlowLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "glowkitname"));
	        GlowLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
	        GlowLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
	        GlowLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "glowspecialhv"));
	        GlowLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "glowspecialbl"));
	        GlowLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
	        
	        im.addEnchant(Enchantment.DAMAGE_UNDEAD, 2, true);
	        im.setLore(GlowLore);
	        im.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Glow Kit");
	        break;
	case PYROMANIAC:
        i = new ItemStack(Material.FIRE_CHARGE);
        im = i.getItemMeta();
        
        List<String> PyroLore = new ArrayList<String>();
        PyroLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "pyromaniacsymbolname"));
        PyroLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        PyroLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
        PyroLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "pyromaniacsymbolpyro"));
        PyroLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "pyromaniacsymbolspecial"));
        PyroLore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        
        im.addEnchant(Enchantment.ARROW_FIRE, 3, true);
        im.setLore(PyroLore);
        im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Pyromaniac Kit");
        break;
	case OWLET:
	    i = new ItemStack(Material.EGG);
	    im = i.getItemMeta();
	    
	    List<String> Owlore = new ArrayList<String>();
	    Owlore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "owlsymbolname"));
	    Owlore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));

        Owlore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
	    Owlore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "owlsymbolegg"));
	    Owlore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
	    
	    im.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3, true);
	    im.setLore(Owlore);
	    im.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "Owlet Kit");
	    break;
	case ILLUSION :
	    i = new ItemStack(Material.OAK_LOG);
	    im = i.getItemMeta();
	    
	    List<String> Ilore = new ArrayList<String>();
	    Ilore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "illusionsymbolname"));
	    Ilore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
        Ilore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "doublejump"));
	    Ilore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "illusionsymbolsneak"));
	    Ilore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "illusionsymbolspecial"));
	    Ilore.add(ChatColor.GOLD + PyroshotMain.getInstance().getLocalizedText(p, "line"));
	    
	    im.addEnchant(Enchantment.CHANNELING, 2, true);
	    im.setLore(Ilore);
	    im.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Illusion Kit");
	    break;
	default:
	    i = new ItemStack(Material.BARRIER);
	    im = i.getItemMeta();
	    List<String> UndefinedLore = new ArrayList<String>();
	    	UndefinedLore.add(ChatColor.GOLD + "Undefined Kit");
	    	UndefinedLore.add(ChatColor.GOLD + "-=-=-=-=-=-=-");
	    	UndefinedLore.add(ChatColor.GOLD + " - This kit has not been properly Defined");
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