package surge;

import surge.control.AmpedPlugin;
import surge.util.D;
import surge.util.Protocol;

public class TestPlugin extends AmpedPlugin
{
	public TestController testController;

	@Override
	public void onStart(Protocol serverProtocol)
	{
		D.v("Plugin onStart(" + serverProtocol.toString() + ")");
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

	@Override
	public void onControllerRegistry()
	{
		D.v("Plugin onControllerRegistry()");
		registerController(new TestController());
	}
}
