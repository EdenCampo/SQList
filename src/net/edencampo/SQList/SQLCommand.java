package net.edencampo.SQList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SQLCommand implements CommandExecutor
{
	SQList plugin;
	
	public SQLCommand(SQList instance)
	{
		plugin = instance;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("SQList"))
			{
				if(args.length == 0)
				{
					player.sendMessage("Usage: /SQList <add/remove> <name>");
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("add"))
				{
					if(!player.hasPermission("SQList.add"))
					{
						player.sendMessage(ChatColor.RED + "Access denied");
					}
					
					plugin.SQLister.addWhitelistedPlayer(args[1]);
					
					player.sendMessage(ChatColor.GREEN + "Success! Added " + args[1] + " to the SQList!");
					
					plugin.SQLister.addAllowedPlayersToList();
					
					return true;
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					if(!player.hasPermission("SQList.remove"))
					{
						player.sendMessage(ChatColor.RED + "Access denied");
					}
					
					plugin.SQLister.removeWhitelistedPlayer(args[1]);
					
					player.sendMessage(ChatColor.GREEN + "Success! Removed " + args[1] + " from the SQList!");
					
					plugin.SQLister.recheckServer();
					
					return true;
				}
				else
				{
					player.sendMessage("Usage: /SQList <add/remove> <name>");
					
					return true;
				}
			}
		}
		else
		{
			if(cmd.getName().equalsIgnoreCase("SQList"))
			{
				if(args.length == 0)
				{
					Bukkit.getConsoleSender().sendMessage("Usage: /SQList <add/remove> <name>");
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("add"))
				{
					plugin.SQLister.addWhitelistedPlayer(args[1]);
					
					Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Success! Added " + args[1] + " to the SQList!");
					
					plugin.SQLister.addAllowedPlayersToList();
					
					return true;
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					plugin.SQLister.removeWhitelistedPlayer(args[1]);
					
					Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Success! Removed " + args[1] + " from the SQList!");
					
					plugin.SQLister.recheckServer();
					
					return true;
				}
				else
				{
					Bukkit.getConsoleSender().sendMessage("Usage: /SQList <add/remove> <name>");
					
					return true;
				}
			}
			
			return true;
		}
		
		return false;
	}
}
