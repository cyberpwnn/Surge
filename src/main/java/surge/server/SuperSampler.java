package surge.server;

import java.util.UUID;

import surge.Surge;
import surge.math.Average;
import surge.math.M;
import surge.sched.IMasterTickComponent;

public class SuperSampler implements IMasterTickComponent
{
	private Average ticksPerSecondL;
	private Average ticksPerSecondM;
	private Average ticksPerSecondS;
	private Average tickTimeL;
	private Average tickTimeM;
	private Average tickTimeS;
	private double ticksPerSecond;
	private double ticksPerSecondRaw;
	private double tickTime;
	private double tickTimeRaw;
	private boolean running;
	private TPSMonitor tpsMonitor;

	public SuperSampler()
	{
		running = false;
		ticksPerSecondL = new Average(20);
		ticksPerSecondM = new Average(10);
		ticksPerSecondS = new Average(3);
		tickTimeL = new Average(20);
		tickTimeM = new Average(10);
		tickTimeS = new Average(3);
		ticksPerSecondRaw = 0;
		ticksPerSecond = 0;
		tickTimeRaw = 0;
		tickTime = 0;

		tpsMonitor = new TPSMonitor()
		{
			@Override
			public void onTicked()
			{
				ticksPerSecondRaw = getRawTicksPerSecond();
				tickTimeRaw = getActualTickTimeMS();
				ticksPerSecondL.put(ticksPerSecondRaw);
				ticksPerSecondM.put(ticksPerSecondRaw);
				ticksPerSecondS.put(ticksPerSecondRaw);
				ticksPerSecond = 0;
				ticksPerSecond += ticksPerSecondL.getAverage();
				ticksPerSecond += ticksPerSecondM.getAverage();
				ticksPerSecond /= 2;
				ticksPerSecond += ticksPerSecondS.getAverage();
				ticksPerSecond /= 2;
				tickTimeL.put(tickTimeRaw);
				tickTimeM.put(tickTimeRaw);
				tickTimeS.put(tickTimeRaw);
				tickTime = 0;
				tickTime += tickTimeL.getAverage();
				tickTime += tickTimeM.getAverage();
				tickTime /= 2;
				tickTime += tickTimeS.getAverage();
				tickTime /= 2;
				double maxms = getTickTimeMS();
				tickTime = M.clip(tickTime, 0, maxms);

				if(ticksPerSecond > 19.92)
				{
					ticksPerSecond = 20;
				}
			}
		};
	}

	public void start()
	{
		tpsMonitor.start();
		running = true;
		Surge.registerTicked(this);
	}

	public void stop()
	{
		tpsMonitor.interrupt();
		running = false;
		Surge.unregisterTicked(this);
	}

	@Override
	public void onTick()
	{
		if(running)
		{
			tpsMonitor.markTick();
		}
	}

	@Override
	public String getTickName()
	{
		return "supersampler-" + UUID.randomUUID().toString();
	}

	public Average getTicksPerSecondL()
	{
		return ticksPerSecondL;
	}

	public Average getTicksPerSecondM()
	{
		return ticksPerSecondM;
	}

	public Average getTicksPerSecondS()
	{
		return ticksPerSecondS;
	}

	public double getTicksPerSecond()
	{
		return ticksPerSecond;
	}

	public double getTicksPerSecondRaw()
	{
		return ticksPerSecondRaw;
	}

	public boolean isRunning()
	{
		return running;
	}

	public TPSMonitor getTpsMonitor()
	{
		return tpsMonitor;
	}

	public Average getTickTimeL()
	{
		return tickTimeL;
	}

	public Average getTickTimeM()
	{
		return tickTimeM;
	}

	public Average getTickTimeS()
	{
		return tickTimeS;
	}

	public double getTickTime()
	{
		return tickTime;
	}

	public double getTickTimeRaw()
	{
		return tickTimeRaw;
	}
}
