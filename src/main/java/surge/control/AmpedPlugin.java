package surge.control;

import org.bukkit.plugin.java.JavaPlugin;

import surge.Surge;
import surge.util.Protocol;

public abstract class AmpedPlugin extends JavaPlugin implements SurgePlugin
{
	public AmpedPlugin()
	{
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
		onStart(Protocol.getProtocolVersion());
	}

	@Override
	public void onDisable()
	{
		Surge.getAmp().disconnect();
		onStop();
	}

	@Override
	public abstract void onStart(Protocol serverProtocol);

	@Override
	public abstract void onStop();

	@Override
	public abstract void onPostInit();

	@Override
	public abstract void onPreInit();
}
