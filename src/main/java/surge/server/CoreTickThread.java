package surge.server;

import surge.Surge;
import surge.sched.IMasterTickComponent;
import surge.util.D;

public class CoreTickThread extends Thread
{
	public CoreTickThread()
	{
		setName("Surge Sideline");
	}

	@Override
	public void run()
	{
		D.v("@Thread Sideline");

		while(!interrupted())
		{
			try
			{
				Thread.sleep(50);

				for(IMasterTickComponent i : Surge.getAsyncTickComponents())
				{
					i.onTick();
				}
			}

			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
}
