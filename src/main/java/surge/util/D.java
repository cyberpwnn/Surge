package surge.util;

import org.bukkit.Bukkit;
import org.cyberpwn.glang.GList;

import surge.Surge;

public class D
{
	private static GList<String> buffer = new GList<String>();

	public static void flush()
	{
		while(!buffer.isEmpty())
		{
			Bukkit.getServer().getConsoleSender().sendMessage(buffer.pop());
		}
	}

	private static void log(String tag, String message)
	{
		String m = "React: " + message;

		if(Surge.isMainThread())
		{
			Bukkit.getServer().getConsoleSender().sendMessage(m);
		}

		else
		{
			buffer.add(C.GRAY + "[ASYNC] " + C.WHITE + m);
		}
	}

	public static void l(Object instance, String message)
	{
		log(C.AQUA + "React", C.WHITE + message);
	}

	public static void s(Object instance, String message)
	{
		log(C.GREEN + "React", C.WHITE + message);
	}

	public static void v(Object instance, String message)
	{
		log(C.GRAY + "React", C.WHITE + message);
	}

	public static void w(Object instance, String message)
	{
		log(C.RED + "React", C.YELLOW + message);
	}

	public static void f(Object instance, String message)
	{
		log(C.RED + "React", C.RED + message);
	}

	public static void l(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.AQUA + "React", C.WHITE + message);
			return;
		}

		log(C.AQUA + "React", C.WHITE + message);
	}

	public static void s(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.GREEN + "React", C.WHITE + message);
			return;
		}

		log(C.GREEN + "React", C.WHITE + message);
	}

	public static void v(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.GRAY + "React", C.WHITE + message);
			return;
		}

		log(C.GRAY + "React", C.WHITE + message);
	}

	public static void w(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.RED + "React", C.YELLOW + message);
			return;
		}

		log(C.RED + "React", C.YELLOW + message);
	}

	public static void f(String message)
	{
		if(!Surge.hasAmp())
		{
			log(C.RED + "React", C.RED + message);
			return;
		}

		log(C.RED + "React", C.RED + message);
	}
}
