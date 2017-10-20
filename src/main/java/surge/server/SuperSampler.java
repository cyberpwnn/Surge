package surge.server;

import java.util.UUID;

import surge.Surge;
import surge.math.Average;
import surge.sched.IMasterTickComponent;
import surge.sched.TICK;

public class SuperSampler implements IMasterTickComponent
{
	private Average ticksPerSecondL;
	private Average tickTimeL;
	private double ticksPerSecond;
	private double ticksPerSecondRaw;
	private double tickTime;
	private double tickTimeRaw;
	private boolean running;
	private double tickUtilizationRaw;
	private double tickUtilization;
	private double leftoverTickTime;
	private double gcPerSecond;
	private long memoryUse;
	private long memoryAllocated;
	private long memoryCollected;
	private long tgc;
	private TPSMonitor tpsMonitor;
	private MemoryMonitor memoryMonitor;

	public SuperSampler()
	{
		running = false;
		ticksPerSecondL = new Average(6);
		tickTimeL = new Average(6);
		ticksPerSecondRaw = 0;
		ticksPerSecond = 0;
		tickTimeRaw = 0;
		tickTime = 0;
		tickUtilization = 0;
		tickUtilizationRaw = 0;
		gcPerSecond = 0;
		memoryUse = 0;
		tgc = 0;
		memoryAllocated = 0;
		memoryCollected = 0;

		tpsMonitor = new TPSMonitor()
		{
			@Override
			public void onTicked()
			{
				ticksPerSecondRaw = getRawTicksPerSecond();
				tickTimeRaw = getActualTickTimeMS();
				ticksPerSecondL.put(ticksPerSecondRaw);
				tickTimeL.put(tickTimeRaw);
				ticksPerSecond = ticksPerSecondL.getAverage();
				tickTime = tickTimeL.getAverage();
				tickUtilizationRaw = tickTimeRaw / 50.0;
				tickUtilization = tickTime / 50.0;
				ticksPerSecond = ticksPerSecond > 19.84 ? 20 : ticksPerSecond;
				leftoverTickTime = 50 - tickUtilization < 0 ? 0 : 50 - tickUtilization;
			}
		};

		memoryMonitor = new MemoryMonitor()
		{
			@Override
			public void onGc()
			{

			}

			@Override
			public void onAllocationSet()
			{
				tgc += getMemoryCollectionsPerTick();

				if(TICK.tick % 20 == 0)
				{
					gcPerSecond = tgc;
					tgc = 0;
				}

				memoryUse = getMemoryUsedAfterGC();
				memoryAllocated = getMemoryAllocatedPerTick();
				memoryCollected = getMemoryCollectedPerTick();
			}
		};
	}

	public void start()
	{
		tpsMonitor.start();
		memoryMonitor.start();
		running = true;
		Surge.registerTicked(this);
	}

	public void stop()
	{
		tpsMonitor.interrupt();
		memoryMonitor.interrupt();
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

	public double getTickTime()
	{
		return tickTime;
	}

	public double getTickTimeRaw()
	{
		return tickTimeRaw;
	}

	public double getTickUtilizationRaw()
	{
		return tickUtilizationRaw;
	}

	public double getTickUtilization()
	{
		return tickUtilization;
	}

	public double getLeftoverTickTime()
	{
		return leftoverTickTime;
	}

	public double getGcPerSecond()
	{
		return gcPerSecond;
	}

	public long getTgc()
	{
		return tgc;
	}

	public MemoryMonitor getMemoryMonitor()
	{
		return memoryMonitor;
	}

	public long getMemoryUse()
	{
		return memoryUse;
	}

	public long getMemoryAllocated()
	{
		return memoryAllocated;
	}

	public long getMemoryCollected()
	{
		return memoryCollected;
	}
}
