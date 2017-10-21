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
		D.s(this, "TPS: " + F.f(ss.getTicksPerSecond(), 1));
		D.s(this, "TICK: " + F.f(ss.getTickTime(), 0) + " ms");
		D.s(this, "MEM: " + F.memSize(ss.getMemoryUse()));
		D.s(this, "ALLOC: " + F.memSize(ss.getMemoryAllocated()));
		D.s(this, "COLLEC: " + F.memSize(ss.getMemoryCollected()));
		D.s(this, "MAH/s: " + F.memSize(ss.getMahs()));
		D.s(this, "    ");
	}
}
