package surge;

import surge.control.AmpedPlugin;
import surge.util.D;
import surge.util.Protocol;

public class TestPlugin extends AmpedPlugin
{
	@Override
	public void onStart(Protocol serverProtocol)
	{

	}

	@Override
	public void onStop()
	{
		D.v("Plugin onStop()");
	}

	@Override
	public void onPreInit()
	{
		D.v("Plugin onPreInit()");
	}

	@Override
	public void onPostInit()
	{
		D.v("Plugin onPostInit()");
	}
}
