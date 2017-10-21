package surge.sched;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import surge.math.Profiler;

public class Bursterator<T> implements IBursterator<T>
{
	private Queue<T> queue;

	private int

	public Bursterator()
	{
		queue = new ConcurrentLinkedQueue<T>();
	}

	@Override
	public void burst(T t)
	{

	}

	@Override
	public void setTimeLock(double ms)
	{

	}

	@Override
	public double getEstimatedTimeUse()
	{
		return 0;
	}

	@Override
	public int flush()
	{
		return 0;
	}

	@Override
	public int flush(Profiler p)
	{
		return 0;
	}

	@Override
	public void queue(T t)
	{
		queue.offer(t);
	}

	@Override
	public void queue(List<T> t)
	{
		for(T i : t)
		{
			queue(i);
		}
	}

	@Override
	public void queue(T[] t)
	{
		for(T i : t)
		{
			queue(i);
		}
	}
}
