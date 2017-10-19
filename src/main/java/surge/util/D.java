package surge.util;

import org.bukkit.Bukkit;

import surge.Surge;
import surge.collection.GList;

public class D
{
	private static GList<String> buffer = new GList<String>();

	private static void log(String tag, String message)
	{
		String m = tag + ": " + message;

		if(Surge.isMainThread())
		{
			Bukkit.getServer().getConsoleSender().sendMessage(m);

			while(!buffer.isEmpty())
			{
				Bukkit.getServer().getConsoleSender().sendMessage(buffer.pop());
			}
		}

		else
		{
			buffer.add(C.LIGHT_PURPLE + "[ASYNC] " + C.WHITE + m);
		}
	}

	public static void l(Object instance, String message)
	{
		log(C.AQUA + instance.getClass().getSimpleName(), C.WHITE + message);
	}

	public static void s(Object instance, String message)
	{
		log(C.GREEN + instance.getClass().getSimpleName(), C.WHITE + message);
	}

	public static void v(Object instance, String message)
	{
		log(C.LIGHT_PURPLE + instance.getClass().getSimpleName(), C.WHITE + message);
	}

	public static void w(Object instance, String message)
	{
		log(C.RED + instance.getClass().getSimpleName(), C.YELLOW + message);
	}

	public static void f(Object instance, String message)
	{
		log(C.RED + instance.getClass().getSimpleName(), C.RED + message);
	}

	public static void l(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.AQUA + "<NO HEAD>", C.WHITE + message);
			return;
		}

		log(C.AQUA + Surge.getAmp().getPluginInstance().getClass().getSimpleName(), C.WHITE + message);
	}

	public static void s(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.GREEN + "<NO HEAD>", C.WHITE + message);
			return;
		}

		log(C.GREEN + Surge.getAmp().getPluginInstance().getClass().getSimpleName(), C.WHITE + message);
	}

	public static void v(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.LIGHT_PURPLE + "<NO HEAD>", C.WHITE + message);
			return;
		}

		log(C.LIGHT_PURPLE + Surge.getAmp().getPluginInstance().getClass().getSimpleName(), C.WHITE + message);
	}

	public static void w(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.RED + "<NO HEAD>", C.YELLOW + message);
			return;
		}

		log(C.RED + Surge.getAmp().getPluginInstance().getClass().getSimpleName(), C.YELLOW + message);
	}

	public static void f(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.RED + "<NO HEAD>", C.RED + message);
			return;
		}

		log(C.RED + Surge.getAmp().getPluginInstance().getClass().getSimpleName(), C.RED + message);
	}
}
