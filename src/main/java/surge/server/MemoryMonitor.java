package surge.server;

import surge.math.M;

public abstract class MemoryMonitor extends Thread
{
	private long memoryFree;
	private long memoryUsed;
	private long memoryMax;
	private long lastMemoryUsed;
	private long memoryUsedAfterGC;
	private long allocated;
	private long collected;
	private long collections;
	private long gcs;
	private long sms;
	private long memoryAllocatedPerTick;
	private long memoryCollectedPerTick;
	private long memoryCollectionsPerTick;
	private long memoryFullyAllocatedPerTick;

	public MemoryMonitor()
	{
		setName("Surge Memory Monitor");
		memoryFree = Runtime.getRuntime().freeMemory();
		memoryMax = Runtime.getRuntime().maxMemory();
		memoryUsed = memoryMax - memoryFree;
		lastMemoryUsed = memoryUsed;
		allocated = 0;
		collected = 0;
		collections = 0;
		memoryUsedAfterGC = 0;
		sms = M.ms();
		gcs = 0;
		memoryAllocatedPerTick = 0;
		memoryCollectedPerTick = 0;
		memoryCollectionsPerTick = 0;
		memoryFullyAllocatedPerTick = 0;
	}

	public abstract void onGc();

	public abstract void onAllocationSet();

	@Override
	public void run()
	{
		while(!interrupted())
		{
			memoryFree = Runtime.getRuntime().freeMemory();
			memoryMax = Runtime.getRuntime().maxMemory();
			memoryUsed = memoryMax - memoryFree;

			if(memoryUsed >= lastMemoryUsed)
			{
				allocated += memoryUsed - lastMemoryUsed;
			}

			else
			{
				collected = lastMemoryUsed - memoryUsed;
				memoryUsedAfterGC = memoryUsed;
				gcs++;
				onGc();
			}

			if(M.ms() - sms >= 50)
			{
				sms = M.ms();
				memoryAllocatedPerTick = allocated;
				memoryCollectedPerTick = collected;
				memoryCollectionsPerTick = gcs;
				memoryFullyAllocatedPerTick = Math.max(0, memoryAllocatedPerTick - memoryCollectedPerTick);
				collected = 0;
				allocated = 0;
				gcs = 0;
				onAllocationSet();
			}

			try
			{
				Thread.sleep(1);
			}

			catch(InterruptedException e)
			{

			}
		}
	}

	public long getMemoryFree()
	{
		return memoryFree;
	}

	public long getMemoryUsed()
	{
		return memoryUsed;
	}

	public long getMemoryMax()
	{
		return memoryMax;
	}

	public long getLastMemoryUsed()
	{
		return lastMemoryUsed;
	}

	public long getMemoryUsedAfterGC()
	{
		return memoryUsedAfterGC;
	}

	public long getAllocated()
	{
		return allocated;
	}

	public long getCollected()
	{
		return collected;
	}

	public long getCollections()
	{
		return collections;
	}

	public long getGcs()
	{
		return gcs;
	}

	public long getSms()
	{
		return sms;
	}

	public long getMemoryAllocatedPerTick()
	{
		return memoryAllocatedPerTick;
	}

	public long getMemoryCollectedPerTick()
	{
		return memoryCollectedPerTick;
	}

	public long getMemoryFullyAllocatedPerTick()
	{
		return memoryFullyAllocatedPerTick;
	}

	public long getMemoryCollectionsPerTick()
	{
		return memoryCollectionsPerTick;
	}
}
