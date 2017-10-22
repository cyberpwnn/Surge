package surge;

import surge.cluster.IConfigurable;
import surge.cluster.Key;

public class ReactSamplePlayer implements IConfigurable
{
	@Key("state.monitoring")
	public boolean isMonitoring = false;

	public ReactSamplePlayer()
	{

	}
}
