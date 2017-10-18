package surge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import surge.util.D;

public class PluginLeech
{
	private Plugin plugin;
	private boolean connected;

	public PluginLeech(Plugin plugin)
	{
		this.plugin = plugin;
		connected = false;
	}

	public Plugin getPluginInstance()
	{
		return plugin;
	}

	public boolean isConnected()
	{
		return connected;
	}

	public void connect()
	{
		connected = true;
		Surge.leech = this;
	}

	public void disconnect()
	{
		connected = false;
		Surge.stopLeeching();
	}

	public int startTask(int delay, Runnable r)
	{
		if(isConnected())
		{
			return Bukkit.getScheduler().scheduleSyncDelayedTask(getPluginInstance(), r, delay);
		}

		else
		{
			D.f("No leech to start task");
		}

		return -1;
	}

	public int startRepeatingTask(int delay, int interval, Runnable r)
	{
		if(isConnected())
		{
			return Bukkit.getScheduler().scheduleSyncRepeatingTask(getPluginInstance(), r, delay, interval);
		}

		else
		{
			D.f("No leech to start repeating task");
		}

		return -1;
	}

	public void stopTask(int id)
	{
		Bukkit.getScheduler().cancelTask(id);
	}
}
