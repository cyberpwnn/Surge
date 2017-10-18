package surge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Surge
{
	protected static PluginLeech leech = null;

	public static PluginLeech beginLeeching(Plugin plugin)
	{
		return new PluginLeech(plugin);
	}

	public static boolean isMainThread()
	{
		return Bukkit.isPrimaryThread();
	}

	protected static void stopLeeching()
	{
		leech = null;
	}

	public static boolean hasLeech()
	{
		return getLeech() != null && getLeech().isConnected();
	}

	public static PluginLeech getLeech()
	{
		return leech;
	}
}
