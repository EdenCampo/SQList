package net.edencampo.SQList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SQListWhitelister
{
	SQList plugin;
	
	public SQListWhitelister(SQList instance)
	{
		plugin = instance;
	}
	
	
	public void addWhitelistedPlayer(String name)
	{		
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		try 
		{	
			Connection connection = plugin.sql.getConnection();
			
			Statement createtables = connection.createStatement();
			
			createtables.executeUpdate("INSERT INTO SQList_Whitelist (`PlayerName`, `AddedDate`) VALUES ('" + name + "', '" + format.format(now) +"');");
			plugin.SQLog.logInfo("Successfully added " + name + " to the SQList!");
		}
		
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void removeWhitelistedPlayer(String name)
	{
		try 
		{
			Connection connection = plugin.sql.getConnection();
			
			Statement deletearena = connection.createStatement();
			
			deletearena.executeUpdate("DELETE FROM SQList_Whitelist WHERE PlayerName = '" + name + "';");
			plugin.SQLog.logInfo("Successfully removed " + name + " from the SQList!");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getWhitecount()
	{
		try 
		{
			Connection connection = plugin.sql.getConnection();
			
			Statement loadarenas = connection.createStatement();
			
			ResultSet res = loadarenas.executeQuery("SELECT WhitelistID FROM SQList_Whitelist;");
			
			if(res.last())
			{
				String whitecount = res.getString("WhitelistID");
				
				res.close();
				
				return whitecount+1;	
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "0";
	}
	
	public void addAllowedPlayersToList()
	{
		String whitecount = getWhitecount();
		
		int whitelistcount = Integer.parseInt(whitecount);
		
		if(whitelistcount == 0)
		{
			return;
		}
		
		try 
		{
		
			int id = 1;
			
			while(id < whitelistcount)
			{
				if(id == 0)
				{
					id++;
					continue;
				}
				
				Connection connection = plugin.sql.getConnection();
				
				Statement loadarenas = connection.createStatement();
				
				String AddName = "";
				
				ResultSet res = loadarenas.executeQuery("SELECT PlayerName FROM SQList_Whitelist WHERE WhitelistID = '" + id + "';");
				
				if(res.next())
				{
					AddName = res.getString("PlayerName");
					res.close();
				}
				
				if(!AddName.isEmpty())
				{
					plugin.allowedPlayers.add(AddName);
				}
				
				id++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void recheckServer()
	{
		plugin.allowedPlayers.clear();
		
		addAllowedPlayersToList();
		
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(!plugin.allowedPlayers.contains(p.getName()))
			{
				p.kickPlayer(ChatColor.DARK_AQUA + "Hello " + p.getName() + ", you have been removed from the SQList on this server.");
			}
		}
	}
}
