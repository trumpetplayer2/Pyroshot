package me.trumpetplayer2.Pyroshot.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.trumpetplayer2.Pyroshot.ConfigHandler;
import me.trumpetplayer2.Pyroshot.PyroshotMain;
import me.trumpetplayer2.Pyroshot.MapHandler.WorldMap;
import me.trumpetplayer2.Pyroshot.MinigameHandler.PyroshotMinigame;
import me.trumpetplayer2.Pyroshot.PlayerStates.Kit;
import me.trumpetplayer2.Pyroshot.PlayerStates.PlayerStats;
import me.trumpetplayer2.Pyroshot.PlayerStates.PyroshotTeam;

public class PyroshotCommand implements TabCompleter, CommandExecutor{
    //Hold the game and plugin
    PyroshotMinigame game;
    PyroshotMain plugin;
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        //Set the game and plugin
        game = PyroshotMain.getInstance().game;
        plugin = PyroshotMain.getInstance();
        //Determine if its a pyroshot command, if not, return
        if(!cmd.getName().equalsIgnoreCase("Pyroshot")) {return null;}
        //Create the args for tab completion, then depending on length and possible entries, fill the completion list and return it
        List<String> completions = new ArrayList<String>();
        if(args.length == 1) {
            completions.add("help");
            completions.add("initialize");
            completions.add("kit");
            completions.add("modifyStats");
            completions.add("pause");
            completions.add("start");
            completions.add("togglePause");
            completions.add("unpause");
            completions.add("vote");
            completions.add("voteCount");
            return completions;
        }else if(args.length == 2) {
            switch(args[0].toLowerCase()) {
            case "kit" : 
                for(Kit k : Kit.values()) {
                    if(k.isHidden()) {continue;}
                    completions.add(k.toString().toLowerCase());
                }
                break;
            case "modifystats" :
                completions.add("addwins");
                completions.add("remwins");
                completions.add("setwins");
                completions.add("addloses");
                completions.add("remloses");
                completions.add("setloses");
                break;
            case "team" :
                completions.add("join");
                completions.add("leave");
                completions.add("get");
                break;
            }
            return completions;
        }else if(args.length == 3) {
            switch(args[1].toLowerCase()) {
            case "addwins" : completions.add("0"); break;
            case "remwins" : completions.add("0"); break;
            case "setwins" : completions.add("0"); break;
            case "addloses" : completions.add("0"); break;
            case "remloses" : completions.add("0"); break;
            case "setloses" : completions.add("0"); break;
            case "join" : 
                if(!game.isInitialized) {return completions;}
                for(PyroshotTeam t : game.map.teams) {
                    completions.add(t.getName());
                }
                break;
            }
            return completions;
        }else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("modifystats")) {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            }
            return completions;
        }
	return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	game = PyroshotMain.getInstance().game;
	plugin = PyroshotMain.getInstance();
	    //Make sure the command is Pyroshot
	    if(!cmd.getName().equalsIgnoreCase("Pyroshot")) {return true;}
		//Determine if sender is player, if so set up p for later use
		Player p = null;
		Boolean isPlayer = false;
		if(sender instanceof Player) {
		    p = (Player) sender;
		    isPlayer = true;
		}
		//If no arguments are provided, send help menu
		if(args.length == 0) {
		    help(sender);
		    return true;
		}
		//Get subcommand and determine which command to run
		String subCommand = args[0];
		switch(subCommand.toLowerCase()) {
		case "help" : help(sender); break;
		case "team" : teamCommand(sender, args); break;
		case "givekit" : 
		    //Check permissions
		    if(!(sender.hasPermission("Pyroshot.Admin.GiveKit"))) {
			invalidPermission(sender);
			return true;
		    }//If player then give kit
		    if(isPlayer && p != null) {
			if(args.length < 2) {giveKit(p);}
			else{giveKit(p, args[1]);}
		    }else {//Else tell user must be player 
			sender.sendMessage(ChatColor.DARK_RED + "This command must be sent by a player");
		    }
		    break;
		case "modifystats" :
		    //Permission check
		    if(!sender.hasPermission("Pyroshot.Admin.ModifyStats")) {
			invalidPermission(sender);
			return true;
		    }//If not long enough, tell user
		    if(args.length < 3) {
			modifyStatsHelp(sender);
			break;
		    }//User provided attiquite information, proceed to new function
		    modifyStats(sender, args);
		    break;
		case "kit" :
		    //Perm check
		    if(!sender.hasPermission("Pyroshot.Minigame.SelectKit")) {
			invalidPermission(sender);
			return true;
		    }//If user is player continue, else tell user must be ran by player
		    if(isPlayer) {
			if(args.length < 2) {
			    //Only recieved kit, go through with GUI
			    kit(p);
			}else {
			    //Kit was specified
			    kit(p, args[1]);
			}
		    }else {
			sender.sendMessage("This command must be ran by a player.");
		    }
		    break;
		case "vote" :
		    //Check if player has permission
		    if(!sender.hasPermission("Pyroshot.Minigame.Vote")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Allow user to vote
			vote(sender);
		    }
		    break;
		case "pause" :
		    //Check if player has permission
		    if(!sender.hasPermission("Pyroshot.Admin.Pause")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Allow user to pause countdown
			pauseMinigame(sender);
		    }
		    break;
		case "unpause" :
		    //Check if player has permission
		    if(!sender.hasPermission("Pyroshot.Admin.Unpause")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Allow user to pause countdown
			unpauseMinigame(sender);
		    }
		    break;
		case "togglepause" :
		    //Check if player has permission
		    if(!sender.hasPermission("Pyroshot.Admin.togglePause")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Allow user to pause countdown
			togglePause(sender);
		    }
		    break;
		case "votecount" :
		    //Check if player has permission to check the votecount
		    if(!sender.hasPermission("Pyroshot.Minigame.VoteCount")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Show user votecount
			game.voteCount(sender);
		    }
		    break;
		case "initialize" :
		    //Check if player has permission to check the votecount
		    if(!sender.hasPermission("Pyroshot.Admin.Initialize")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Show user votecount
			initializeMatch(sender);
		    }
		    break;
		case "start" :
		    //Check if player has permission to check the votecount
		    if(!sender.hasPermission("Pyroshot.Admin.Start")) {
			invalidPermission(sender);
			return true;
		    }else {
			//Show user votecount
			startMatch(sender);
		    }
		    break;
		default : 
			help(sender);
			break;
		}
	    return true;
	}

    public void pauseMinigame(CommandSender sender) {
	    //Send required message based on if the minigame was paused, then pause the minigame
	    if(game.isPaused) {
		sender.sendMessage("The game is already paused");
	    }else {
		sender.sendMessage("The game is now paused");
	    }
	    game.isPaused = true;
	}
	
    public void unpauseMinigame(CommandSender sender) {
        //Unpause the minigame and send user appropriate messages
	    if(game.isPaused) {
		sender.sendMessage("The game is no longer paused");
	    }else {
		sender.sendMessage("The game is not paused");
	    }
	    game.isPaused = false;
	}
	
    public void togglePause(CommandSender sender) {
        //Toggle the pause state of the game
	    game.isPaused = !game.isPaused;
	    if(game.isPaused) {
		sender.sendMessage("The game is now paused");
	    }else {
		sender.sendMessage("The game is no longer paused");
	    }
	}
    
    public void initializeMatch(CommandSender sender) {
        //If the match is not already initialized, initialize.
	    if(!game.isInitialized) {
	    game.timer = ConfigHandler.initializeTimer;
	    }else {
	        //If match is already initialize inform player
	        sender.sendMessage("The match has already been initialized!");
	    }
	}
	
    public void startMatch(CommandSender sender) {
        //Determine if match is active, if so, tell user so
	    if(game.isActive) {
	        sender.sendMessage("There is already a match in play.");
	        return;
	    }
	    //Check if the game is initialized and initialize if not
	    if(!game.isInitialized) {
	        game.InitializeMinigame();
	    }
	    //Start the game
	    if(game.isInitialized) {
	        game.timer = 0;
	        game.MinigameStart();
	        sender.sendMessage("Game Started");
	    }else {
	        //If there was an issue with initialization, end game. This prevents map failure attempted loading
	        sender.sendMessage("An issue has occured while initializing the game.");
	    }
	    if(game.isPaused) {
	        //Unpause the game
	        game.isPaused = false;
	    }
	}
	
    public void invalidPermission(CommandSender sender) {
        //Invalid permission error
	    sender.sendMessage(ChatColor.DARK_RED + "You do not have permission for this command.");
	}
	
    public void giveKit(Player p, String kit) {
	    //Give the player the kit
	    p.getInventory().setContents(Kit.kitFromString(kit).getInventory().getContents());
	}
	
	public void giveKit(Player p) {
	    //Give the player the kit
	    p.getInventory().setContents(PyroshotMain.instance.getPlayerStats(p).getKit().getInventory().getContents());
	}
	
	public void help(CommandSender sender) {
	  //If the command "/Pyroshot" is typed, bring up help menu
	  sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	  sender.sendMessage(ChatColor.GOLD + "          Pyroshot Command Help         ");
	  sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Initialize/Start' -> Initializes/Starts a match");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Pause/Unpause/TogglePause' -> Pauses/Unpauses the autostart countdown");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Help' -> Displays this Menu");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Kit' -> Displays the Kit Selection GUI");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot ModifyStats (addWins|remWins|setWins|addLoses|remLoses|setLoses) <Whole Number> [Player]' -> Modifies Specified player stat");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Vote' -> Displays vote gui");
	  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot VoteCount' -> Displays the current vote amounts");
	  sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	}
		
	public void teamCommand(CommandSender sender, String[] args) {
	    if(!(sender instanceof Player)) {sender.sendMessage("This command is intended to be used by a player");}
	    Player p = (Player) sender;
	    //Team Subcommands
	    if(args.length > 1) {
		String subcommand = args[1];
		if(!game.isInitialized) {
		    sender.sendMessage(ChatColor.RED + "Map has not been decided. Please wait before joining teams.");
		}
		//Team commands
		switch(subcommand.toLowerCase()) {
		case "join" : joinTeam(p, args);
			return;
		case "leave" : leaveTeam(p);
			return;
		case "get" : getTeam(p, args);
			return;
		
		default : helpTeams(sender);
			return;
		}
	    }
	    else {
		helpTeams(sender);
	    }
	}
	
	public void helpTeams(CommandSender sender) {
		  sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
		  sender.sendMessage(ChatColor.GOLD + "       Pyroshot Team Command Help       ");
		  sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
		  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Team Join' -> Joins a team");
		  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Team Leave' -> Leaves a team");
		  sender.sendMessage(ChatColor.GOLD + "'/Pyroshot Team Get [Team Name] ' -> Gets the specified team. If none, gets your team");
		  sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	}
	
	public void getTeam(Player p, String[] args) {
	    
	    //If player is on team, tell them
	    for(PyroshotTeam team : game.map.teams) {
		p.sendMessage(ChatColor.GOLD + "Team " + team.getName() + ": " + team.getPlayersLeft());
		if(team.onTeam(p)) {
		    p.sendMessage(ChatColor.GREEN + "Team " + team.getName() + " is selected.");
		}
	    }
	}
	
	public void joinTeam(Player p, String[] args) {
	    //Create a boolean to hold if player is on the team
	    boolean onTeam = false;
	    //Check to make sure the game is initialized
	    if(!game.isInitialized) {
		p.sendMessage(ChatColor.GOLD + "Please wait for minigame to initialize before choosing a team");
	    }
	    if(game.timer <= 3 || game.isActive) {p.sendMessage(ChatColor.GOLD + "You cannot join or leave a team now");return;}
	    //Check that a team was provided, if none, send valid teams and return
	    if(args.length < 2) {
		p.sendMessage(ChatColor.GOLD + "Please enter a valid team name. The teams are:");
		for(PyroshotTeam team : game.map.teams) {p.sendMessage(ChatColor.GOLD + team.getName());}
		return;
	    }else {
	        //Check if the team the player requested to join is valid, and join the team
		for(PyroshotTeam team : game.map.teams) {
		    if(team.onTeam(p)) {
		        team.removePlayer(p);
		    }
		    if(team.getName().equalsIgnoreCase(args[2].toLowerCase())) {
		        p.sendMessage(ChatColor.GOLD + "Joined team " + team.teamColor + team.name);
			team.addPlayer(p);
			onTeam = true;
		    }else {
		    }
		}
	    }
	    if(!onTeam) {
	        //Team did not exist, tell user they couldnt find the team
		p.sendMessage("Could not find team " + args[2]);
	    }
	}
	
	public void leaveTeam(Player p) {
	    for(PyroshotTeam team : game.map.teams) {
		if(team.onTeam(p)) {
		    team.removePlayer(p);
		    p.sendMessage("You have left " + team.getName());
		}
	    }
	}

	public void modifyStats(CommandSender sender, String[] args) {
	    //Determine if there is a valid player, if not, send Invalid Player
	    Player p = null;
	    if(args.length == 4) {
		p = Bukkit.getPlayer(args[3]);
		if(p == null) {
		    modifyStatsHelp(sender);
		    sender.sendMessage(ChatColor.DARK_RED + "Invalid Player.");
		    return;
		}
		//Check if player is the sender, otherwise respond that the sender must provide a player
	    }else if(args.length == 3){
		if((sender instanceof Player)) {
		p = (Player) sender;
		}else {
		    modifyStatsHelp(sender);
		    sender.sendMessage(ChatColor.DARK_RED + "Invalid Player.");
		    return;
		}
	    }
	    //Detect if valid numerical information was provided
	    try{
		Integer.parseInt(args[2]);
	    }catch(NumberFormatException e){
		modifyStatsHelp(sender);
		sender.sendMessage(ChatColor.DARK_RED + "Invalid Number.");
		return;
	    }
	    //Send the modify stats function the player, the selected method, the number provided and the command executor
	    if(p != null) {
	    modifyStats(p, args[1], args[2], sender);
	    }else {
	        //Send the player the help command with an Invalid Player marker
		modifyStatsHelp(sender);
		sender.sendMessage(ChatColor.DARK_RED + "Invalid Player.");
	    }
	}
	
	public void modifyStatsHelp(CommandSender sender) {
	    sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	    sender.sendMessage(ChatColor.GOLD + "   Pyroshot ModifyStats Command Help    ");
	    sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	    sender.sendMessage(ChatColor.GOLD + "'/Pyroshot ModifyStats (addWins|addLoses) <Whole Number> [Player]' -> Adds Wins/Loses");
	    sender.sendMessage(ChatColor.GOLD + "'/Pyroshot ModifyStats (remWins|remLoses) <Whole Number> [Player]' -> Removes Wins/Loses");
	    sender.sendMessage(ChatColor.GOLD + "'/Pyroshot ModifyStats (setWins|setLoses) <Whole Number> [Player]' -> Sets Wins/Loses");
	    sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	    sender.sendMessage(ChatColor.GOLD + "Player optional if sent by a player");
	    sender.sendMessage(ChatColor.GOLD + "-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
	}
	
	public void modifyStats(Player p, String action , String number, CommandSender sender) {
	    //Variable to hold provided increase/decrease value
	    int i = 0;
	    try {
		i = Integer.parseInt(number);
	    }catch(NumberFormatException e){
		return;   
	    }
	    //Get the players current stats
	    PlayerStats stats = plugin.getPlayerStats(p);
	    //Default to wins but will be changed later
	    String modifyType = "Wins";
	    int endNum = 0;
	    switch(action.toLowerCase()) {
	    case "addwins" : stats.addWins(i);
	    	modifyType = "Wins";
	    	break;
	    case "remwins" : stats.removeWins(i);
	   	modifyType = "Wins";
	   	break;
	    case "removewins" : stats.removeWins(i);
	   	modifyType = "Wins";
	   	break;
	    case "setwins" : stats.setWins(i);
		modifyType = "Wins";
	   	break;
	    case "addloses" : stats.addLoses(i);
		modifyType = "Loses";
	   	break;
	    case "remloses" : stats.removeLoses(i);
		modifyType = "Loses";
	   	break;
	    case "removeloses" : stats.removeLoses(i);
		modifyType = "Loses";
	   	break;
	    case "setloses" : stats.setLoses(i);
		modifyType = "Loses";
	   	break;
	    default : sender.sendMessage(ChatColor.DARK_RED + "Invalid subcommand"); return;
	   }
	    //Determine what number to use in calculations
	   if(modifyType == "Wins") {
	       endNum = stats.getWins();
	   }else {
	       endNum = stats.getLoses();
	   }
	   //Tell user the updated number.
	   sender.sendMessage(p.getName() + " is now set to " + endNum + " " + modifyType);
	   plugin.save(p);
	}
	
	public void kit(Player p) {
	    //Open the kit inventory
	    p.openInventory(getKitInv());
	}
	
	public Inventory getKitInv() {
	    //Determine size based on how many visable kits
	    int size = getNearestNine((int) Math.ceil(Kit.numberOfKits));
	    Inventory inv = Bukkit.createInventory(null, size, ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Kits");
	    //Fill inventory with kits, removing hidden kits
	    int invisible = 0;
	    for(int i = 0; i < (Kit.values().length); i++) {
		if(Kit.isHidden(Kit.kitFromInt(i))) { invisible += 1; continue;}
		inv.setItem(i - invisible, Kit.kitFromInt(i).KitSymbol());
	    }
	    return inv;
	}
	
	public void kit(Player p, String kit) {
	    //Kits was provided, select the provided kit
	    Kit k;
	    //If kit was not valid, open GUI
	    if(Kit.kitFromString(kit) == null) {
		kit(p);
		return;
	    }
	    k = Kit.kitFromString(kit);
	    //Check if user has required permission for specific kit. By default all kits are available
	    if(k.hasPermission(p)) {
	    plugin.getPlayerStats(p).setKit(k);
	    p.sendMessage("Set kit to " + plugin.getPlayerStats(p).getKit().kitToString());
	    }else {
		invalidPermission(p);
	    }
	}
	
	public void vote(CommandSender sender) {
	    //Check if user is player
	    if(!(sender instanceof Player)) {sender.sendMessage(ChatColor.DARK_RED + "This command should be sent by a player"); return;}
	    Player p = (Player) sender;
	    //Display GUI for user
	    p.openInventory(getWorldMap());
	}
	
	public Inventory getWorldMap() {
	    //Dynamically create the inventory based on available worlds in the games world database, then return it
	    HashMap<Integer, WorldMap> map = ConfigHandler.getWorldMap;
	    int size = getNearestNine((int) Math.ceil(map.size()));
	    Inventory inv = Bukkit.createInventory(null, size, ChatColor.DARK_RED + "Pyro" + ChatColor.GOLD + "shot" + ChatColor.RESET + " Map Vote");
	    for(int i = 0; i < map.size(); i++) {
		inv.setItem(i, map.get(i).getSymbol());
	    }
	    return inv;
	}
	
	private int getNearestNine(int current) {
	    //Grab number fed
	    int MultOfNine = 0;
	    boolean isPositive = true;
	    while(isPositive) {
		//Loop until the number is negative, adding 1 to a counter and removing nine from number
		current -= 9;
		MultOfNine += 1;
		if(current <= 0) {
		    isPositive = false;
		}
	    }
	    //Return the number of loops multiplied by nine
	    return MultOfNine*9;
	}

}
