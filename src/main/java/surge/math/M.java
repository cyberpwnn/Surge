package surge.math;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import surge.collection.GBiset;
import surge.collection.GList;

public class M
{
	public static double maxSafeRadius(Location l)
	{
		return maxSafeRadius(l, 256, 112);
	}

	public static double clip(double value, double min, double max)
	{
		return Math.min(max, Math.max(min, value));
	}

	public static double maxSafeRadius(Location l, double max, double interval)
	{
		double s = 1;

		if(max < 1)
		{
			max = 1;
		}

		while(s < max)
		{
			s += interval;

			if(!isLoaded(l, s))
			{
				return s - interval;
			}
		}

		return s;
	}

	public static boolean isLoaded(Location c, double r)
	{
		for(GBiset<Integer, Integer> i : cradShift(c, r))
		{
			if(!isLoaded(c.getWorld(), i.getA(), i.getB()))
			{
				return false;
			}
		}

		return true;
	}

	public static GList<GBiset<Integer, Integer>> cradShift(Location c, double r)
	{
		GList<GBiset<Integer, Integer>> map = new GList<GBiset<Integer, Integer>>();
		int ax = (int) (c.getX() + r) >> 4;
		int az = (int) (c.getZ() + r) >> 4;
		int bx = (int) (c.getX() - r) >> 4;
		int bz = (int) (c.getZ() - r) >> 4;

		for(int i = bx; i < ax; i++)
		{
			for(int j = bz; j < az; j++)
			{
				map.add(new GBiset<Integer, Integer>(i, j));
			}
		}

		return map;
	}

	public static int chunkShift(int c)
	{
		return c >> 4;
	}

	public static boolean isLoaded(World world, int x, int z)
	{
		for(Chunk i : world.getLoadedChunks())
		{
			if(i.getX() == x && i.getZ() == z)
			{
				return true;
			}
		}

		return false;
	}

	public static boolean isLoaded(Location location)
	{
		for(Chunk i : location.getWorld().getLoadedChunks())
		{
			if(i.getX() == chunkShift(location.getBlockX()) && i.getZ() == chunkShift(location.getBlockZ()))
			{
				return true;
			}
		}

		return false;
	}

	public static double dof(double base, double range)
	{
		if(base == 0)
		{
			return 0;
		}

		return ((range - base) / base);
	}

	public static double tps(long ns, int rad)
	{
		return (20.0 * (ns / 50000000.0)) / rad;
	}

	public static int ticksFromNS(long ns)
	{
		return (int) (ns / 50000000.0);
	}

	public static long ns()
	{
		return System.nanoTime();
	}

	public static long ms()
	{
		return System.currentTimeMillis();
	}

	public static double avg(GList<Double> doubles)
	{
		double a = 0.0;

		for(double i : doubles)
		{
			a += i;
		}

		return a / doubles.size();
	}

	public static void lim(GList<Double> doubles, int limit)
	{
		while(doubles.size() > limit)
		{
			doubles.remove(0);
		}
	}

	public static GList<Chunk> getChunks(Chunk c, int rad)
	{
		GList<Chunk> cx = new GList<Chunk>();

		for(int i = c.getX() - rad + 1; i < c.getX() + rad; i++)
		{
			for(int j = c.getZ() - rad + 1; j < c.getZ() + rad; j++)
			{
				cx.add(c.getWorld().getChunkAt(i, j));
			}
		}

		cx.add(c);

		return cx;
	}
}
