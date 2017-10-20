package surge.control;

import org.bukkit.plugin.java.JavaPlugin;

import surge.Surge;
import surge.collection.GList;
import surge.pool.ParallelPoolManager;
import surge.pool.QueueMode;
import surge.sched.IMasterTickComponent;
import surge.util.Protocol;

public abstract class AmpedPlugin extends JavaPlugin implements SurgePlugin, IMasterTickComponent
{
	private GList<IController> controllers;
	private ParallelPoolManager pp;

	public AmpedPlugin()
	{
		controllers = new GList<IController>();
		pp = new ParallelPoolManager("amp-worker", 4, QueueMode.ROUND_ROBIN);
		onControllerRegistry();
		onPreInit();
	}

	@Override
	public void onLoad()
	{
		onPostInit();
	}

	@Override
	public void onEnable()
	{
		Surge.createAmp(this).connect();

		for(IController i : getControllers())
		{
			i.start();
		}

		onStart(Protocol.getProtocolVersion());
		Surge.registerTicked(this);
	}

	@Override
	public void onDisable()
	{
		for(IController i : getControllers())
		{
			i.stop();
		}

		onStop();

		pp.shutdown();
		Surge.getAmp().disconnect();
	}

	@Override
	public GList<IController> getControllers()
	{
		return controllers;
	}

	@Override
	public void registerController(IController c)
	{
		getControllers().add(c);
	}

	@Override
	public void onTick()
	{
		if(!Surge.hasAmp())
		{
			return;
		}

		for(IController i : getControllers())
		{
			i.tick();
		}
	}

	@Override
	public String getTickName()
	{
		return getName() + "-tick";
	}

	@Override
	public ParallelPoolManager getThreadPool()
	{
		return pp;
	}

	@Override
	public abstract void onControllerRegistry();

	@Override
	public abstract void onStart(Protocol serverProtocol);

	@Override
	public abstract void onStop();

	@Override
	public abstract void onPostInit();

	@Override
	public abstract void onPreInit();
}
