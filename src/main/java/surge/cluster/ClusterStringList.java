package surge.cluster;

import java.util.List;

public class ClusterStringList extends Cluster<List<String>>
{
	protected ClusterStringList(List<String> t)
	{
		super(ClusterType.STRING_LIST, t);
	}
}
