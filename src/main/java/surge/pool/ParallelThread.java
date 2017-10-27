package surge.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import surge.sched.TICK;

public class ParallelThread extends Thread
{
	private Queue<Execution> queue;
	private ThreadInformation info;

	public ParallelThread(String key, int id)
	{
		queue = new ConcurrentLinkedQueue<Execution>();
		info = new ThreadInformation(id);
		setPriority(MAX_PRIORITY);
		setName("Surge " + key + " " + id);
	}

	@Override
	public void run()
	{
		while(!interrupted())
		{
			try
			{
				double time = 0;
				double diff = 0;
				double tdiff = 0;
				long ns = System.nanoTime();
				info.setProcessing(true);
				info.setQueuedSize(queue.size());
				execute();
				time = (double) (System.nanoTime() - ns) / 1000000.0;
				diff = (50.0 - time) < 0 ? 0 : 50.0 - time;
				tdiff = (long) (time + diff);
				info.setProcessing(false);
				conditionallySleep(diff);
				info.setTicksPerSecond(20 - ((1.0 - (50.0 / tdiff)) * 20));
				info.setTicksPerSecond(info.getTicksPerSecond() < 0 ? 0 : info.getTicksPerSecond());
				info.setUtilization(1.0 - (info.getTicksPerSecond() / 20.0));
				info.setTick(info.getTick() + 1);
			}

			catch(InterruptedException e)
			{
				System.out.println("Shutting Down " + getName());
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private void conditionallySleep(double diff) throws InterruptedException
	{
		if(info.getTick() >= TICK.tick)
		{
			Thread.sleep((long) diff);
		}
	}

	private void execute(Execution e)
	{
		try
		{
			e.run();
		}

		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void execute()
	{
		while(!queue.isEmpty())
		{
			if(interrupted())
			{
				System.out.println("Parallel Thread " + info.getId() + " Interrupted mid-execution");
				return;
			}

			execute(queue.poll());
		}
	}

	public void queue(Execution e)
	{
		queue.offer(e);
	}

	public Queue<Execution> getQueue()
	{
		return queue;
	}

	public ThreadInformation getInfo()
	{
		return info;
	}
}
