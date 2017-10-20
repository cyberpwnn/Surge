package surge;

import surge.control.Controller;
import surge.util.D;

public class TestController extends Controller
{
	@Override
	public void start()
	{
		D.v(this, "Controller start()");
	}

	@Override
	public void stop()
	{
		D.v(this, "Controller stop()");
	}

	@Override
	public void tick()
	{
		D.v(this, "Controller tick() ");
		D.l(this, "There are " + Surge.getAmp().getPluginInstance().getControllers().size());
	}
}
