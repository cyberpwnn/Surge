package surge;

import org.bukkit.Bukkit;

import surge.control.AmpedPlugin;
import surge.sched.IMasterTickComponent;
import surge.util.D;
import surge.util.PluginUtil;

public class PluginAmp
{
	private AmpedPlugin plugin;
	private boolean connected;
	private int masterTask;

	public PluginAmp(AmpedPlugin plugin)
	{
		this.plugin = plugin;
		connected = false;
	}

	public AmpedPlugin getPluginInstance()
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
		Surge.getHotloadManager().track(Surge.getPluginJarFile(), new Runnable()
		{
			@Override
			public void run()
			{
				D.v("Reinjecting... ");
				PluginUtil.reload(getPluginInstance());
			}
		});

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

		for(IMasterTickComponent i : Surge.getTickComponents())
		{
			i.onTick();
		}
	}

	public void disconnect()
	{
		connected = false;
		stopTask(masterTask);
		Surge.stopAmp();
		Surge.getHotloadManager().untrackall();
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
