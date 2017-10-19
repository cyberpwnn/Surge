package surge.sched;

import surge.Surge;

public abstract class Task implements ITask, ICancellable
{
	private int id;

	public Task()
	{
		id = Surge.getLeech().startTask(0, new Runnable()
		{
			@Override
			public void run()
			{
				Task.this.run();
			}
		});
	}

	public Task(int interval)
	{
		id = Surge.getLeech().startRepeatingTask(0, interval, new Runnable()
		{
			@Override
			public void run()
			{
				Task.this.run();
			}
		});
	}

	@Override
	public void cancel()
	{
		Surge.getLeech().stopTask(id);
	}

	@Override
	public int getId()
	{
		return id;
	}
}
