package surge.pool;

import java.util.ConcurrentModificationException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import surge.collection.GList;
import surge.math.M;
import surge.util.D;

public class ParallelPoolManager
{
	private QueueMode mode;
	private GList<ParallelThread> threads;
	private int next;
	private int threadCount;
	private Queue<Execution> squeue;
	private String key;
	private ThreadInformation info;

	public void syncQueue(Execution e)
	{
		squeue.offer(e);
	}

	public void tickSyncQueue()
	{
		long ns = M.ns();

		while(!squeue.isEmpty())
		{
			squeue.poll().run();

			long nns = M.ns() - ns;

			if(nns > 250000)
			{
				break;
			}
		}
	}

	public ParallelPoolManager(String key, int threadCount, QueueMode mode)
	{
		this(threadCount, mode);
		D.v(this, "Starting Parallel Pool: " + key);
		this.key = key;
	}

	public ParallelPoolManager(int threadCount, QueueMode mode)
	{
		if(threadCount < 1)
		{
			threadCount = 1;
		}

		if(threadCount > 4)
		{
			System.out.println("WARNING: HIGH THREAD COUNT FOR CORETICK");
		}

		threads = new GList<ParallelThread>();
		this.threadCount = threadCount;
		next = 0;
		this.mode = mode;
		key = "Worker Thread";
		info = new ThreadInformation(-1);
		squeue = new ConcurrentLinkedQueue<Execution>();
	}

	public long lock()
	{
		long k = M.ms();

		while(getQueueSize() != 0)
		{
			try
			{
				Thread.sleep(1);
			}

			catch(InterruptedException e)
			{

			}
		}

		return M.ms() - k;
	}

	public void start()
	{
		createThreads(threadCount);
	}

	public void shutdown()
	{
		for(ParallelThread i : threads)
		{
			i.interrupt();
		}
	}

	public ParallelPoolManager(int threadCount)
	{
		this(threadCount, QueueMode.ROUND_ROBIN);
	}

	public void queue(Execution e)
	{
		nextThread().queue(e);
	}

	public int getSize()
	{
		return threads.size();
	}

	public int getQueueSize()
	{
		int s = 0;

		for(ParallelThread i : getThreads())
		{
			s += i.getQueue().size();
		}

		return s;
	}

	public ParallelThread[] getThreads()
	{
		return threads.toArray(new ParallelThread[threads.size()]);
	}

	private void updateThreadInformation()
	{
		try
		{
			if(threads.isEmpty())
			{
				return;
			}

			double ticksPerSecond = 0;
			int queuedSize = 0;
			double utilization = 0;

			for(ParallelThread ph : threads.copy())
			{
				ticksPerSecond += ph.getInfo().getTicksPerSecondAverage();
				queuedSize += ph.getQueue().size();
				utilization += ph.getInfo().getUtilization();
			}

			utilization /= threads.size();
			ticksPerSecond /= threads.size();
			getAverageInfo().setTicksPerSecond(ticksPerSecond);
			getAverageInfo().setQueuedSize(queuedSize);
			getAverageInfo().setUtilization(utilization);
		}

		catch(ConcurrentModificationException e)
		{

		}
	}

	private ParallelThread nextThread()
	{
		updateThreadInformation();

		if(threads.size() == 1)
		{
			return threads.get(0);
		}

		int id = 0;

		switch(mode)
		{
			case ROUND_ROBIN:
				next = (next > threads.size() - 1 ? 0 : next + 1);
				id = next;
			case SMALLEST:
				int min = Integer.MAX_VALUE;

				for(ParallelThread i : threads)
				{
					int size = i.getQueue().size();

					if(size < min)
					{
						min = size;
						id = i.getInfo().getId();
					}
				}

			default:
				break;
		}

		return threads.get(id);
	}

	private void createThreads(int count)
	{
		for(int i = 0; i < count; i++)
		{
			ParallelThread p = new ParallelThread(key, i);
			p.start();
			threads.add(p);
		}
	}

	public QueueMode getMode()
	{
		return mode;
	}

	public int getNext()
	{
		return next;
	}

	public int getThreadCount()
	{
		return threadCount;
	}

	public Queue<Execution> getSqueue()
	{
		return squeue;
	}

	public String getKey()
	{
		return key;
	}

	public ThreadInformation getAverageInfo()
	{
		return info;
	}
}
