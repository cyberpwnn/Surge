package surge;

import org.bukkit.Bukkit;
import org.cyberpwn.gconcurrent.TICK;

import surge.control.AmpedPlugin;
import surge.sched.IMasterTickComponent;
import surge.util.D;
import surge.util.PluginUtil;

public class PluginAmp
{
	private AmpedPlugin plugin;
	private boolean connected;
	private int masterTask;
	public Runnable onReload;

	public PluginAmp(AmpedPlugin plugin)
	{
		this.plugin = plugin;
		connected = false;

		onReload = new Runnable()
		{
			@Override
			public void run()
			{

			}
		};
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
				onReload.run();
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

		TICK.tick++;
		D.flush();

		for(IMasterTickComponent i : Surge.getTickComponents().copy())
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

	public AmpedPlugin getPlugin()
	{
		return plugin;
	}

	public int getMasterTask()
	{
		return masterTask;
	}

	public Runnable getOnReload()
	{
		return onReload;
	}

	public void setPlugin(AmpedPlugin plugin)
	{
		this.plugin = plugin;
	}

	public void setConnected(boolean connected)
	{
		this.connected = connected;
	}

	public void setMasterTask(int masterTask)
	{
		this.masterTask = masterTask;
	}

	public void setOnReload(Runnable onReload)
	{
		this.onReload = onReload;
	}
}
