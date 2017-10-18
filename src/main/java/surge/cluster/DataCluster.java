package surge.cluster;

import java.util.List;

import surge.collection.GMap;

public class DataCluster
{
	private GMap<String, ICluster<?>> clusters;

	public DataCluster()
	{
		this.clusters = new GMap<String, ICluster<?>>();
	}

	public boolean contains(String k)
	{
		return clusters.containsKey(k);
	}

	public ClusterType getType(String k)
	{
		return clusters.get(k).getType();
	}

	public int getInt(String k)
	{
		return (int) clusters.get(k).get();
	}

	public String getString(String k)
	{
		return clusters.get(k).get().toString();
	}

	public double getDouble(String k)
	{
		return (double) clusters.get(k).get();
	}

	public boolean getBoolean(String k)
	{
		return (boolean) clusters.get(k).get();
	}

	@SuppressWarnings("unchecked")
	public List<String> getStringList(String k)
	{
		return (List<String>) clusters.get(k).get();
	}

	public void set(String k, int integer)
	{
		clusters.put(k, new ClusterInt(integer));
	}

	public void set(String k, boolean bool)
	{
		clusters.put(k, new ClusterBoolean(bool));
	}

	public void set(String k, double d)
	{
		clusters.put(k, new ClusterDouble(d));
	}

	public void set(String k, String s)
	{
		clusters.put(k, new ClusterString(s));
	}

	public void set(String k, List<String> slist)
	{
		clusters.put(k, new ClusterStringList(slist));
	}
}
