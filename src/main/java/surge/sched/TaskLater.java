package surge.sched;

import org.cyberpwn.gbench.Profiler;

import surge.Surge;

public abstract class TaskLater implements ITask, ICancellable
{
	private int id;
	private String name;
	private boolean repeating;
	private double computeTime;
	private double totalComputeTime;
	private double activeTime;
	private boolean completed;
	private Profiler profiler;
	private Profiler activeProfiler;
	protected int ticks;

	public TaskLater(String name)
	{
		this(name, 0);
	}

	public TaskLater(String name, int delay)
	{
		setup(name, true);
		profiler.begin();

		id = Surge.getAmp().startTask(delay, new Runnable()
		{
			@Override
			public void run()
			{
				activeProfiler.begin();
				TaskLater.this.run();
				ticks++;
				activeProfiler.end();
				computeTime = activeProfiler.getMilliseconds();
				totalComputeTime += computeTime;
				profiler.end();
				activeTime += profiler.getMilliseconds();
				activeProfiler.reset();
				profiler.reset();
				profiler.begin();
			}
		});
	}

	private void setup(String n, boolean r)
	{
		profiler = new Profiler();
		activeProfiler = new Profiler();
		repeating = r;
		name = n;
		completed = false;
		computeTime = 0;
		activeTime = 0;
		totalComputeTime = 0;
		ticks = 0;
	}

	@Override
	public void cancel()
	{
		Surge.getAmp().stopTask(id);
		completed = true;
		profiler.end();
		activeTime += profiler.getMilliseconds();
		profiler.reset();
		activeProfiler.reset();
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public boolean isRepeating()
	{
		return repeating;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public double getComputeTime()
	{
		return computeTime;
	}

	@Override
	public boolean hasCompleted()
	{
		return completed;
	}

	@Override
	public double getTotalComputeTime()
	{
		return totalComputeTime;
	}

	@Override
	public double getActiveTime()
	{
		return activeTime;
	}
}
