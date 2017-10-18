package surge.cluster;

public class ClusterBoolean extends Cluster<Boolean>
{
	protected ClusterBoolean(Boolean t)
	{
		super(ClusterType.BOOLEAN, t);
	}
}
