package surge.pool;

import surge.Surge;

public abstract class A extends Execution
{
	public A()
	{
		Surge.getAmp().getPluginInstance().getThreadPool().queue(new Execution()
		{
			@Override
			public void run()
			{
				A.this.run();
			}
		});
	}
}
