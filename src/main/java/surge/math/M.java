package surge.math;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import surge.collection.GBiset;
import surge.collection.GList;
import surge.util.Area;

public class M
{
	private static double[] fastsqrt;

	private static final int precision = 128;
	private static final int modulus = 360 * precision;
	private static final float[] sin = new float[modulus];

	/**
	 * Evaluates an expression using javascript engine and returns the double
	 *
	 * @param expression
	 *            the mathimatical expression
	 * @return the double result
	 * @throws ScriptException
	 *             dont fuck up.
	 */
	public static double evaluate(String expression) throws ScriptException
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine scriptEngine = mgr.getEngineByName("JavaScript");

		return Double.valueOf(scriptEngine.eval(expression).toString());
	}

	/**
	 * Get true or false based on random percent
	 *
	 * @param d
	 *            between 0 and 1
	 * @return true if true
	 */
	public static boolean r(Double d)
	{
		if(d == null)
		{
			return Math.random() < 0.5;
		}

		return Math.random() < d;
	}

	/**
	 * Evaluates an expression using javascript engine and returns the double
	 * result. This can take variable parameters, so you need to define them.
	 * Parameters are defined as $[0-9]. For example evaluate("4$0/$1", 1, 2); This
	 * makes the expression (4x1)/2 == 2. Keep note that you must use 0-9, you
	 * cannot skip, or start at a number other than 0.
	 *
	 * @param expression
	 *            the expression with variables
	 * @param args
	 *            the arguments/variables
	 * @return the resulting double value
	 * @throws ScriptException
	 *             dont fuck up
	 * @throws IndexOutOfBoundsException
	 *             learn to count
	 */
	public static double evaluate(String expression, Double... args) throws ScriptException, IndexOutOfBoundsException
	{
		for(int i = 0; i < args.length; i++)
		{
			String current = "$" + i;

			if(expression.contains(current))
			{
				expression = expression.replaceAll(Matcher.quoteReplacement(current), args[i] + "");
			}
		}

		return evaluate(expression);
	}

	public static Block highestBlock(Location l, int shuf, int st)
	{
		int y = st;
		Block b = null;

		while(y > 0)
		{
			y -= shuf;

			if(new Location(l.getWorld(), l.getX(), y, l.getZ()).getBlock().getType().equals(org.bukkit.Material.AIR))
			{
				if(shuf > 1)
				{
					b = highestBlock(l, 1, y + shuf);
					break;
				}

				else
				{
					b = new Location(l.getWorld(), l.getX(), y, l.getZ()).getBlock();
					break;
				}
			}
		}

		return b;
	}

	/**
	 * Get the ticks per second from a time in nanoseconds, the rad can be used for
	 * multiple ticks
	 *
	 * @param ns
	 *            the time in nanoseconds
	 * @param rad
	 *            the radius of the time
	 * @return the ticks per second in double form
	 */
	public static double tps(long ns, int rad)
	{
		return (20.0 * (ns / 50000000.0)) / rad;
	}

	/**
	 * Get the number of ticks from a time in nanoseconds
	 *
	 * @param ns
	 *            the nanoseconds
	 * @return the amount of ticks
	 */
	public static double ticksFromNS(long ns)
	{
		return (ns / 50000000.0);
	}

	/**
	 * Get roman numeral representation of the int
	 *
	 * @param num
	 *            the int
	 * @return the numerals
	 */
	public static String toRoman(int num)
	{
		LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();

		roman_numerals.put("M", 1000);
		roman_numerals.put("CM", 900);
		roman_numerals.put("D", 500);
		roman_numerals.put("CD", 400);
		roman_numerals.put("C", 100);
		roman_numerals.put("XC", 90);
		roman_numerals.put("L", 50);
		roman_numerals.put("XL", 40);
		roman_numerals.put("X", 10);
		roman_numerals.put("IX", 9);
		roman_numerals.put("V", 5);
		roman_numerals.put("IV", 4);
		roman_numerals.put("I", 1);

		String res = "";

		for(Map.Entry<String, Integer> entry : roman_numerals.entrySet())
		{
			int matches = num / entry.getValue();

			res += repeat(entry.getKey(), matches);
			num = num % entry.getValue();
		}

		return res;
	}

	/**
	 * Repeat a string
	 *
	 * @param s
	 *            the string
	 * @param n
	 *            the amount of times to repeat
	 * @return the repeated string
	 */
	private static String repeat(String s, int n)
	{
		if(s == null)
		{
			return null;
		}

		final StringBuilder sb = new StringBuilder();

		for(int i = 0; i < n; i++)
		{
			sb.append(s);
		}

		return sb.toString();
	}

	public static int rand(int f, int t)
	{
		return f + (int) (Math.random() * ((t - f) + 1));
	}

	/**
	 * Get the number representation from roman numerals.
	 *
	 * @param number
	 *            the roman number
	 * @return the int representation
	 */
	public static int fromRoman(String number)
	{
		if(number.isEmpty())
		{
			return 0;
		}

		number = number.toUpperCase();

		if(number.startsWith("M"))
		{
			return 1000 + fromRoman(number.substring(1));
		}

		if(number.startsWith("CM"))
		{
			return 900 + fromRoman(number.substring(2));
		}

		if(number.startsWith("D"))
		{
			return 500 + fromRoman(number.substring(1));
		}

		if(number.startsWith("CD"))
		{
			return 400 + fromRoman(number.substring(2));
		}

		if(number.startsWith("C"))
		{
			return 100 + fromRoman(number.substring(1));
		}

		if(number.startsWith("XC"))
		{
			return 90 + fromRoman(number.substring(2));
		}

		if(number.startsWith("L"))
		{
			return 50 + fromRoman(number.substring(1));
		}

		if(number.startsWith("XL"))
		{
			return 40 + fromRoman(number.substring(2));
		}

		if(number.startsWith("X"))
		{
			return 10 + fromRoman(number.substring(1));
		}

		if(number.startsWith("IX"))
		{
			return 9 + fromRoman(number.substring(2));
		}

		if(number.startsWith("V"))
		{
			return 5 + fromRoman(number.substring(1));
		}

		if(number.startsWith("IV"))
		{
			return 4 + fromRoman(number.substring(2));
		}

		if(number.startsWith("I"))
		{
			return 1 + fromRoman(number.substring(1));
		}

		return 0;
	}

	/**
	 * Get system Nanoseconds
	 *
	 * @return nanoseconds (current)
	 */
	public static long ns()
	{
		return System.nanoTime();
	}

	/**
	 * Get the current millisecond time
	 *
	 * @return milliseconds
	 */
	public static long ms()
	{
		return System.currentTimeMillis();
	}

	/**
	 * Average a list of doubles
	 *
	 * @param doubles
	 *            the doubles
	 * @return the average
	 */
	public static double avg(GList<Double> doubles)
	{
		double a = 0.0;

		for(double i : doubles)
		{
			a += i;
		}

		return a / doubles.size();
	}

	/**
	 * Cull a list of doubles
	 *
	 * @param doubles
	 *            the doubles
	 * @param limit
	 *            the limit size
	 */
	public static void lim(GList<Double> doubles, int limit)
	{
		while(doubles.size() > limit)
		{
			doubles.remove(0);
		}
	}

	/**
	 * An alternative method of distance calculation
	 *
	 * @param a
	 *            the first location
	 * @param b
	 *            the second
	 * @return the distance between the two
	 */
	public static double distance(Location a, Location b)
	{
		return Double.longBitsToDouble(((Double.doubleToLongBits(a.distanceSquared(b)) - (1l << 52)) >> 1) + (1l << 61));
	}

	/**
	 * Check if a location is within a given range of another without using sqrt
	 * functions
	 *
	 * @param center
	 *            the center (first position)
	 * @param check
	 *            the check location (second position)
	 * @param radius
	 *            the radius to check
	 * @return true if the check is within the given range of the center position
	 */
	public static boolean within(Location center, Location check, Double radius)
	{
		return Area.within(center, check, radius);
	}

	/**
	 * Fast sin function
	 *
	 * @param a
	 *            the number
	 * @return the sin
	 */
	public static float sin(float a)
	{
		return sinLookup((int) (a * precision + 0.5f));
	}

	/**
	 * Fast cos function
	 *
	 * @param a
	 *            the number
	 * @return the cos
	 */
	public static float cos(float a)
	{
		return sinLookup((int) ((a + 90f) * precision + 0.5f));
	}

	/**
	 * Biggest number
	 *
	 * @param ints
	 *            the numbers
	 * @return the biggest one
	 */
	public static int max(int... ints)
	{
		int max = Integer.MIN_VALUE;

		for(int i : ints)
		{
			if(i > max)
			{
				max = i;
			}
		}

		return max;
	}

	/**
	 * Smallest number
	 *
	 * @param ints
	 *            the numbers
	 * @return the smallest one
	 */
	public static int min(int... ints)
	{
		int min = Integer.MAX_VALUE;

		for(int i : ints)
		{
			if(i < min)
			{
				min = i;
			}
		}

		return min;
	}

	/**
	 * is the number "is" within from-to
	 *
	 * @param from
	 *            the lower end
	 * @param to
	 *            the upper end
	 * @param is
	 *            the check
	 * @return true if its within
	 */
	public static boolean within(int from, int to, int is)
	{
		return is >= from && is <= to;
	}

	static
	{
		for(int i = 0; i < sin.length; i++)
		{
			sin[i] = (float) Math.sin((i * Math.PI) / (precision * 180));
		}
	}

	private static float sinLookup(int a)
	{
		return a >= 0 ? sin[a % (modulus)] : -sin[-a % (modulus)];
	}

	public static double fsqrt(double k)
	{
		if(k > 8191)
		{
			return fastsqrt[8191];
		}

		if(k < 0)
		{
			return -fsqrt(-k);
		}

		return fastsqrt[(int) k];
	}

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

	static
	{
		fastsqrt = new double[8192];

		for(int i = 0; i < 8192; i++)
		{
			fastsqrt[i] = Math.sqrt(i);
		}
	}
}
