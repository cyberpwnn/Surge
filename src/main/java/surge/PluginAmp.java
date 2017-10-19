package surge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import surge.util.D;

public class PluginAmp
{
	private Plugin plugin;
	private boolean connected;
	private int masterTask;

	public PluginAmp(Plugin plugin)
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
		Surge.amp = this;

		masterTask = startRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				masterTick();
			}
		});
	}

	private void masterTick()
	{
		if(!isConnected())
		{
			stopTask(masterTask);
		}
	}

	public void disconnect()
	{
		connected = false;
		stopTask(masterTask);
		Surge.stopAmp();
	}

	public int startTask(int delay, Runnable r)
	{
		if(isConnected())
		{
			return Bukkit.getScheduler().scheduleSyncDelayedTask(getPluginInstance(), r, delay);
		}

		else
		{
			D.f("No amp to start task");
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
			D.f("No amp to start repeating task");
		}

		return -1;
	}

	public void stopTask(int id)
	{
		Bukkit.getScheduler().cancelTask(id);
	}
}
