package net.edencampo.SQList;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.edencampo.SQList.utils.MySQL;
import net.edencampo.SQList.utils.Updater;
import net.edencampo.SQList.utils.Metrics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class SQList extends JavaPlugin
{
	public List<String> allowedPlayers = new ArrayList<String>();
	
	SQListLogger SQLog = new SQListLogger(this);
	SQListener SQLJoinListener = new SQListener(this);
	SQLCommand SQLCmd = new SQLCommand(this);
	SQListWhitelister SQLister = new SQListWhitelister(this);
	
	public MySQL sql;
	
	public void onEnable()
	{	
		saveDefaultConfig();
		reloadConfig();
		
		getCommand("SQList").setExecutor(SQLCmd);
		getCommand("SQList add").setExecutor(SQLCmd);
		getCommand("SQList remove").setExecutor(SQLCmd);
		
		try 
		{
		    Metrics metrics = new net.edencampo.SQList.utils.Metrics(this);
		    metrics.start();
		} 
		catch (IOException e) 
		{
			SQLog.logInfo("SimonSays failed to start usage tracking :(");
		}
		
		if(IsFirstStartup())
		{
			SQLog.logInfo("Thank you for downloading SQLister!");
			
			SQLog.logInfo("Please read the installation and setup field at: http://dev.bukkit.org/bukkit-plugins/SQLister/");
			
			SQLog.logInfo("As it's your first startup, SQLister will not check for updates.");
			
			SQLog.logInfo("Note that SimonSays utilizes PluginMetrics for usage tracking.");
		    
			SQLog.logInfo("If you don't want usage tracking disable that in /plugins/PluginMetrics/config.yml");
			
			SQLog.logInfo("Because this is your first startup, you need to enter your MySQL details in the config.yml");
			
			SQLog.logInfo("SQList had auto-generated a config for you and is now shutting down to minimize errors.");
			
			SQLog.logInfo("Please reload the server when you are finished with editing the config.yml");
		    
		    getConfig().set("firstStartup", "no");
		    
		    saveConfig();
		    reloadConfig();
		    
		    Bukkit.getPluginManager().disablePlugin(this);
		    return;
		}
		
		String sqlHost = getConfig().getString("host");
		String sqlPort = getConfig().getString("port");
		String sqlDb = getConfig().getString("database");
		String sqlUser = getConfig().getString("user");
		String sqlPw = getConfig().getString("password");
		
		SQLog.logInfo("Attempting to connect MySQL (" + sqlHost + ") using database " + sqlDb);
		
		sql = new MySQL(this, sqlHost, sqlPort, sqlDb, sqlUser, sqlPw);		
		
		try
		{
			sql.openConnection();
			
			Statement createtables = sql.getConnection().createStatement();
			createtables.executeUpdate("CREATE TABLE IF NOT EXISTS SQList_Whitelist(PlayerName varchar(255) NOT NULL, AddedDate varchar(255) NOT NULL, WhitelistID int(255) NOT NULL AUTO_INCREMENT, PRIMARY KEY (`WhitelistID`))");
		}
		catch (SQLException e) 
		{
			SQLog.logSevereError(ChatColor.RED + "Failed to connect MySQL! Shutting down...");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		
		if(!IsFirstStartup())
		{
			CheckUpdate();
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(SQLJoinListener, this);
		
		SQLister.addAllowedPlayersToList();
		
		SQLog.logInfo("Successfully loaded!");
	}
	
	public void onDisable()
	{
		SQLog.logInfo("Successfully unloaded!");
	}
	
	
	protected void CheckUpdate()
	{
		String update = getConfig().getString("autoUpdate");
		
		if(update.equalsIgnoreCase("true") || update.equalsIgnoreCase("yes"))
		{
			Updater updater = new Updater(this, "sqlist", this.getFile(), Updater.UpdateType.DEFAULT, true);
			
	        Updater.UpdateResult upresult = updater.getResult();
	        
	        switch(upresult)
	        {
	            case SUCCESS:
	            	SQLog.logInfo("SQList will be updated on next reload!");
	                break;
	            case FAIL_DOWNLOAD:
	            	SQLog.logInfo("Download Failed: The auto-updater found an update, but was unable to download SQList.");
	                break;
	            case FAIL_DBO:
	            	SQLog.logInfo("dev.bukkit.org Failed: for some reason, the updater was unable to contact DBO to download the file.");
	        }
		}
		else
		{
			SQLog.logInfo("Skipped update-checking...");
		}
	}
	
	public boolean IsFirstStartup()
	{
		if(getConfig().getString("firstStartup").equalsIgnoreCase("yes") || getConfig().getString("firstStartup").equalsIgnoreCase("true"))
		{
			return true;
		}
		
		return false;
	}
}
