package surge.server;

import org.cyberpwn.gbench.Profiler;
import org.cyberpwn.gmath.M;

import surge.Surge;

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
	private long lastTick;
	private boolean frozen;
	private StackTraceElement[] lockedStack;
	private double lmsx;

	public TPSMonitor()
	{
		lmsx = 0;
		setName("Surge Tick Monitor");
		tickProfiler = new Profiler();
		tickProfiler.begin();
		tickTimeProfiler = new Profiler();
		tickTimeProfiler.begin();
		actualTickTimeMS = 0;
		tickTimeMS = 0;
		frozen = false;
		ltt = 0;
		ticked = false;
		lastState = State.RUNNABLE;
		lastTick = M.ms();
		lockedStack = null;
		frozen = false;
	}

	public abstract void onTicked();

	public abstract void onSpike();

	@Override
	public void run()
	{
		while(!interrupted())
		{
			if(interrupted())
			{
				return;
			}

			if(Surge.getServerThread() != null)
			{
				processState(Surge.getServerThread().getState());
			}

			else
			{

			}

			if(interrupted())
			{
				return;
			}

			if(ticked)
			{
				tickProfiler.end();
				tickTimeMS = tickProfiler.getMilliseconds();
				rawTicksPerSecond = M.clip(1000.0 / ((double) (lmsx + tickTimeMS) / 2.0), 0, 20);
				lmsx = tickTimeMS;
				tickProfiler.reset();
				tickProfiler.begin();
				ticked = false;
				actualTickTimeMS = actualTickTimeMS == 0 ? ltt : actualTickTimeMS;
				ltt = actualTickTimeMS > 0 ? actualTickTimeMS : ltt;
				onTicked();
				actualTickTimeMS = 0;
				lastTick = M.ms();
				frozen = false;
				lockedStack = null;
			}

			else if(M.ms() - lastTick > 900)
			{
				boolean wasntFrozen = !frozen;
				frozen = true;
				rawTicksPerSecond = -(M.ms() - lastTick);
				tickTimeMS = M.ms() - lastTick;

				if(wasntFrozen)
				{
					try
					{
						lockedStack = lockedStack == null ? Surge.getServerThread().getStackTrace() : lockedStack;
						onSpike();
					}

					catch(Exception e)
					{

					}
				}

				onTicked();

			}

			try
			{
				Thread.sleep(1);
			}

			catch(InterruptedException e)
			{
				return;
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

	public double getLtt()
	{
		return ltt;
	}

	public long getLastTick()
	{
		return lastTick;
	}

	public boolean isFrozen()
	{
		return frozen;
	}

	public StackTraceElement[] getLockedStack()
	{
		return lockedStack;
	}
}
