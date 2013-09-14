package net.edencampo.SQList;

public class SQListLogger
{
	SQList plugin;
	
	public SQListLogger(SQList instance)
	{
		plugin = instance;
	}
	
	public void logSevereError(String msg)
	{
		plugin.getLogger().severe(msg);
	}
	
	public void logWarning(String msg)
	{
		plugin.getLogger().warning(msg);
	}
	
	public void logInfo(String msg)
	{	
		plugin.getLogger().info(msg);
	}
	
}
