package surge.server;

import java.util.UUID;

import surge.Surge;
import surge.math.Average;
import surge.sched.IMasterTickComponent;

public class SuperSampler implements IMasterTickComponent
{
	private Average ticksPerSecondL;
	private Average tickTimeL;
	private Average mahL;
	private double ticksPerSecond;
	private double ticksPerSecondRaw;
	private double tickTime;
	private double tickTimeRaw;
	private boolean running;
	private double tickUtilizationRaw;
	private double tickUtilization;
	private double leftoverTickTime;
	private long memoryUse;
	private long memoryAllocated;
	private long memoryCollected;
	private long mahs;
	private int totalChunks;
	private int totalEntities;
	private int totalDrops;
	private int totalTiles;
	private int totalLiving;
	private WorldMonitor worldMonitor;
	private TPSMonitor tpsMonitor;
	private MemoryMonitor memoryMonitor;

	public SuperSampler()
	{
		running = false;
		ticksPerSecondL = new Average(6);
		mahL = new Average(20);
		tickTimeL = new Average(6);
		totalChunks = 0;
		totalEntities = 0;
		totalDrops = 0;
		totalTiles = 0;
		totalLiving = 0;
		ticksPerSecondRaw = 0;
		ticksPerSecond = 0;
		tickTimeRaw = 0;
		tickTime = 0;
		tickUtilization = 0;
		tickUtilizationRaw = 0;
		memoryUse = 0;
		memoryAllocated = 0;
		memoryCollected = 0;
		mahs = 0;

		worldMonitor = new WorldMonitor()
		{
			@Override
			public void updated(int totalChunks, int totalDrops, int totalTiles, int totalLiving, int totalEntities)
			{
				SuperSampler.this.totalChunks = totalChunks;
				SuperSampler.this.totalDrops = totalDrops;
				SuperSampler.this.totalTiles = totalTiles;
				SuperSampler.this.totalEntities = totalEntities;
				SuperSampler.this.totalLiving = totalLiving;
			}
		};

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
				ticksPerSecond = ticksPerSecond > 20 ? 20 : ticksPerSecond;
				leftoverTickTime = 50 - tickUtilization < 0 ? 0 : 50 - tickUtilization;
			}
		};

		memoryMonitor = new MemoryMonitor()
		{
			@Override
			public void onAllocationSet()
			{
				memoryUse = getMemoryUsedAfterGC();
				memoryAllocated = getMemoryAllocatedPerTick();
				memoryCollected = getMemoryCollectedPerTick();
				mahL.put(getMahs());
				mahs = (long) mahL.getAverage();
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

	public Average getMahL()
	{
		return mahL;
	}

	public long getMahs()
	{
		return mahs;
	}

	public int getTotalChunks()
	{
		return totalChunks;
	}

	public int getTotalEntities()
	{
		return totalEntities;
	}

	public int getTotalDrops()
	{
		return totalDrops;
	}

	public int getTotalTiles()
	{
		return totalTiles;
	}

	public int getTotalLiving()
	{
		return totalLiving;
	}

	public WorldMonitor getWorldMonitor()
	{
		return worldMonitor;
	}
}
