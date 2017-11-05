package surge;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.cyberpwn.glang.GList;

import surge.control.AmpedPlugin;
import surge.hotload.HotloadManager;
import surge.sched.IMasterTickComponent;
import surge.sched.TaskManager;
import surge.server.AsyncTick;
import surge.util.PluginUtil;

public class Surge
{
	protected static PluginAmp amp = null;
	private static GList<IMasterTickComponent> tickComponents = new GList<IMasterTickComponent>();
	private static GList<IMasterTickComponent> atickComponents = new GList<IMasterTickComponent>();
	private static TaskManager taskmgr;
	private static HotloadManager hotloadmgr;
	private static Thread mainThread;

	public static void register(Listener l)
	{
		Bukkit.getServer().getPluginManager().registerEvents(l, getAmp().getPluginInstance());
	}

	public static void unregister(Listener l)
	{
		HandlerList.unregisterAll(l);
	}

	public static File folder(String f)
	{
		return new File(getAmp().getPluginInstance().getDataFolder(), f);
	}

	public static File folder()
	{
		return getAmp().getPluginInstance().getDataFolder();
	}

	public static File getPluginJarFile()
	{
		File parent = getAmp().getPluginInstance().getDataFolder().getParentFile();
		String plname = PluginUtil.getPluginFileName(getAmp().getPluginInstance().getName());
		return new File(parent, plname);
	}

	public static File getPluginJarFileUnsafe(Plugin main)
	{
		File parent = main.getDataFolder().getParentFile();
		String plname = PluginUtil.getPluginFileNameUnsafe(main.getName(), main);
		return new File(parent, plname);
	}

	private static void startup()
	{
		registerTicked(taskmgr = new TaskManager());
		registerTicked(hotloadmgr = new HotloadManager());
	}

	public static Thread getServerThread()
	{
		return mainThread;
	}

	public static GList<IMasterTickComponent> getTickComponents()
	{
		return tickComponents;
	}

	public static GList<IMasterTickComponent> getAsyncTickComponents()
	{
		return atickComponents;
	}

	public static void registerTicked(IMasterTickComponent tick)
	{
		if(tick.getClass().isAnnotationPresent(AsyncTick.class))
		{
			atickComponents.add(tick);
			return;
		}

		tickComponents.add(tick);
	}

	public static void unregisterTicked(IMasterTickComponent tick)
	{
		if(tick.getClass().isAnnotationPresent(AsyncTick.class))
		{
			atickComponents.remove(tick);
			return;
		}

		tickComponents.remove(tick);
	}

	public static PluginAmp createAmp(AmpedPlugin plugin)
	{
		if(isMainThread())
		{
			mainThread = Thread.currentThread();
		}

		return new PluginAmp(plugin);
	}

	public static boolean isMainThread()
	{
		return Bukkit.isPrimaryThread();
	}

	protected static void stopAmp()
	{
		amp = null;
	}

	public static boolean hasAmp()
	{
		return getAmp() != null && getAmp().isConnected();
	}

	public static PluginAmp getAmp()
	{
		return amp;
	}

	public static TaskManager getTaskManager()
	{
		return taskmgr;
	}

	public static HotloadManager getHotloadManager()
	{
		return hotloadmgr;
	}

	static
	{
		startup();
	}
}
