package surge.math;

import surge.util.F;

public class Profiler
{
	private long nanos;
	private long startNano;
	private long millis;
	private long startMillis;
	private double time;

	public Profiler()
	{
		reset();
	}

	public void begin()
	{
		startNano = System.nanoTime();
		startMillis = System.currentTimeMillis();
	}

	public void end()
	{
		nanos = System.nanoTime() - startNano;
		millis = System.currentTimeMillis() - startMillis;
		time = (double) nanos / 1000000.0;
		time = (double) millis - time > 1.01 ? millis : time;
	}

	public void reset()
	{
		nanos = -1;
		millis = -1;
		startNano = -1;
		startMillis = -1;
		time = -0;
	}

	public String getTime(int dec)
	{
		if(getNanoseconds() < 1000.0)
		{
			return F.f(getNanoseconds()) + "ns";
		}

		if(getMilliseconds() < 1000.0)
		{
			return F.f(getMilliseconds(), dec) + "ms";
		}

		if(getSeconds() < 60.0)
		{
			return F.f(getSeconds(), dec) + "s";
		}

		if(getMinutes() < 60.0)
		{
			return F.f(getMinutes(), dec) + "m";
		}

		return F.f(getHours(), dec) + "h";
	}

	public double getTicks()
	{
		return getMilliseconds() / 50.0;
	}

	public double getSeconds()
	{
		return getMilliseconds() / 1000.0;
	}

	public double getMinutes()
	{
		return getSeconds() / 60.0;
	}

	public double getHours()
	{
		return getMinutes() / 60.0;
	}

	public double getMilliseconds()
	{
		return time;
	}

	public long getNanoseconds()
	{
		return (long) (time * 1000000.0);
	}
}
