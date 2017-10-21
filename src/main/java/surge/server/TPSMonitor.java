package surge.server;

import surge.Surge;
import surge.math.M;
import surge.math.Profiler;
import surge.util.D;

public abstract class TPSMonitor extends Thread
{
	private double tickTimeMS;
	private double rawTicksPerSecond;
	private Profiler tickProfiler;
	private Profiler tickTimeProfiler;
	private boolean ticked;
	private State lastState;
	private double actualTickTimeMS;
	private double ltt;

	public TPSMonitor()
	{
		setName("Surge Tick Monitor");
		tickProfiler = new Profiler();
		tickProfiler.begin();
		tickTimeProfiler = new Profiler();
		tickTimeProfiler.begin();
		actualTickTimeMS = 0;
		tickTimeMS = 0;
		ltt = 0;
		ticked = false;
		lastState = State.RUNNABLE;
	}

	public abstract void onTicked();

	@Override
	public void run()
	{
		while(!interrupted())
		{
			if(Surge.getServerThread() != null)
			{
				processState(Surge.getServerThread().getState());
			}

			else
			{
				D.f(this, "Cannot find main server thread!");
			}

			if(ticked)
			{
				tickProfiler.end();
				tickTimeMS = tickProfiler.getMilliseconds();
				rawTicksPerSecond = M.clip(1000.0 / tickTimeMS, 0, 20);
				tickProfiler.reset();
				tickProfiler.begin();
				ticked = false;
				actualTickTimeMS = actualTickTimeMS == 0 ? ltt : actualTickTimeMS;
				ltt = actualTickTimeMS > 0 ? actualTickTimeMS : ltt;
				onTicked();
				actualTickTimeMS = 0;
			}

		}
	}

	private void processState(State state)
	{
		if(state.equals(lastState))
		{
			return;
		}

		if(state.equals(State.BLOCKED))
		{
			return;
		}

		if(!state.equals(State.TIMED_WAITING) && !state.equals(State.RUNNABLE))
		{
			System.out.println(state);
			return;
		}

		if(lastState.equals(State.RUNNABLE) && state.equals(State.TIMED_WAITING))
		{
			tickTimeProfiler.end();
			actualTickTimeMS += tickTimeProfiler.getMilliseconds();
			tickTimeProfiler.reset();
		}

		if(lastState.equals(State.TIMED_WAITING) && state.equals(State.RUNNABLE))
		{
			tickTimeProfiler.begin();
		}

		lastState = state;
	}

	public double getTickTimeMS()
	{
		return tickTimeMS;
	}

	public double getRawTicksPerSecond()
	{
		return rawTicksPerSecond;
	}

	public Profiler getTickProfiler()
	{
		return tickProfiler;
	}

	public boolean isTicked()
	{
		return ticked;
	}

	public void markTick()
	{
		ticked = true;
	}

	public Profiler getTickTimeProfiler()
	{
		return tickTimeProfiler;
	}

	public State getLastState()
	{
		return lastState;
	}

	public double getActualTickTimeMS()
	{
		return actualTickTimeMS;
	}
}
