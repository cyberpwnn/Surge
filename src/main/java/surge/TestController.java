package surge;

import surge.control.Controller;
import surge.server.SuperSampler;
import surge.util.D;
import surge.util.F;

public class TestController extends Controller
{
	private SuperSampler ss;

	@Override
	public void start()
	{
		D.v(this, "Controller start()");
		ss = new SuperSampler();
		ss.start();
	}

	@Override
	public void stop()
	{
		D.v(this, "Controller stop()");
		ss.stop();
	}

	@Override
	public void tick()
	{
		D.s(this, "    ");
		D.s(this, "TPS: " + F.f(ss.getTicksPerSecond(), 1));
		D.s(this, "TICK: " + F.time(ss.getTickTime(), 0) + " (" + F.pc(ss.getTickUtilization(), 0) + ")");
	}
}
