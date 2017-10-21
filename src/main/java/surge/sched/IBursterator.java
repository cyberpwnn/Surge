package surge.sched;

import java.util.List;

import surge.math.Profiler;

public interface IBursterator<T>
{
	public void burst(T t);

	public void setTimeLock(int ms);

	public int getEstimatedTimeUse();

	public int flush();

	public int flush(Profiler p);

	public void queue(T t);

	public void queue(List<T> t);

	public void queue(T[] t);
}
