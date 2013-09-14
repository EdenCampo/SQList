package net.edencampo.SQList;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SQListener implements Listener
{
	SQList plugin;
	
	public SQListener(SQList instance)
	{
		plugin = instance;
	}
	
	@EventHandler
	public void onPlayerLoginAttempt(PlayerLoginEvent e)
	{
		String playername = e.getPlayer().getName();
		
		if(plugin.allowedPlayers.contains(playername))
		{
			e.allow();
		}
		else
		{
			e.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.DARK_AQUA + "Hello " + playername + ", you are not SQListed on this server.");
		}
	}
}
