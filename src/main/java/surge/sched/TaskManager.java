package surge.sched;

import surge.collection.GList;
import surge.collection.GMap;

public class TaskManager implements IMasterTickComponent
{
	private GList<ITask> tasks;
	private GMap<String, TaskProfile> taskProfiles;

	public TaskManager()
	{
		tasks = new GList<ITask>();
		taskProfiles = new GMap<String, TaskProfile>();
	}

	@Override
	public void onTick()
	{
		for(ITask i : tasks.copy())
		{
			putTask(i);

			if(i.hasCompleted())
			{
				putFinishingTask(i);
				tasks.remove(i);
			}
		}
	}

	private void putFinishingTask(ITask t)
	{
		taskProfiles.remove(t.getName());
	}

	private void putTask(ITask t)
	{
		if(!taskProfiles.containsKey(t.getName()))
		{
			taskProfiles.put(t.getName(), new TaskProfile());
		}

		TaskProfile p = taskProfiles.get(t.getName());
		p.setActiveTime(t.getActiveTime());
		p.setComputeTickTime(t.getComputeTime());
		p.setComputeTime(t.getTotalComputeTime());
	}

	@Override
	public String getTickName()
	{
		return "taskmgr";
	}
}
