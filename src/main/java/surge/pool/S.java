package surge.pool;

import surge.Surge;

public abstract class S extends Execution
{
	public S()
	{
		Surge.getAmp().getPluginInstance().getThreadPool().syncQueue(new Execution()
		{
			@Override
			public void run()
			{
				S.this.run();
			}
		});
	}
}
