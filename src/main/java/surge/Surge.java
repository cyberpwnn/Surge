package surge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import surge.collection.GList;
import surge.sched.IMasterTickComponent;
import surge.sched.TaskManager;

public class Surge
{
	protected static PluginAmp amp = null;
	private static GList<IMasterTickComponent> tickComponents = new GList<IMasterTickComponent>();
	private static TaskManager taskmgr;

	private static void startup()
	{
		registerTicked(taskmgr = new TaskManager());
	}

	public static void registerTicked(IMasterTickComponent tick)
	{
		tickComponents.add(tick);
	}

	public static void unregisterTicked(IMasterTickComponent tick)
	{
		tickComponents.remove(tick);
	}

	public static PluginAmp createAmp(Plugin plugin)
	{
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

	static
	{
		startup();
	}
}
