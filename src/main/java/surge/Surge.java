package surge;

import java.io.File;

import org.bukkit.Bukkit;

import surge.collection.GList;
import surge.control.AmpedPlugin;
import surge.hotload.HotloadManager;
import surge.sched.IMasterTickComponent;
import surge.sched.TaskManager;
import surge.util.PluginUtil;

public class Surge
{
	protected static PluginAmp amp = null;
	private static GList<IMasterTickComponent> tickComponents = new GList<IMasterTickComponent>();
	private static TaskManager taskmgr;
	private static HotloadManager hotloadmgr;
	private static Thread mainThread;

	public static File getPluginJarFile()
	{
		File parent = getAmp().getPluginInstance().getDataFolder().getParentFile();
		String plname = PluginUtil.getPluginFileName(getAmp().getPluginInstance().getName());
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

	public static void registerTicked(IMasterTickComponent tick)
	{
		tickComponents.add(tick);
	}

	public static void unregisterTicked(IMasterTickComponent tick)
	{
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
