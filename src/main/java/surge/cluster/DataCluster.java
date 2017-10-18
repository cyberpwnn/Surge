package surge.cluster;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import surge.collection.GList;
import surge.collection.GMap;
import surge.json.JSONObject;
import surge.util.D;

public class DataCluster
{
	private GMap<String, ICluster<?>> clusters;

	public DataCluster()
	{
		this.clusters = new GMap<String, ICluster<?>>();
	}

	public GList<String> keys()
	{
		return clusters.k();
	}

	public void fromJson(JSONObject j)
	{
		for(String i : j.keySet())
		{
			trySet(i, j.get(i));
		}
	}

	public JSONObject toJson()
	{
		JSONObject j = new JSONObject();

		for(String i : keys())
		{
			j.put(i, get(i));
		}

		return j;
	}

	public FileConfiguration toFileConfiguration()
	{
		FileConfiguration fc = new YamlConfiguration();

		for(String i : keys())
		{
			fc.set(i, get(i));
		}

		return fc;
	}

	public void fromFileConfiguration(FileConfiguration fc)
	{
		for(String i : fc.getKeys(true))
		{
			ICluster<?> c = null;

			if(fc.isBoolean(i))
			{
				c = new ClusterBoolean(fc.getBoolean(i));
			}

			else if(fc.isDouble(i))
			{
				c = new ClusterDouble(fc.getDouble(i));
			}

			else if(fc.isInt(i))
			{
				c = new ClusterInt(fc.getInt(i));
			}

			else if(fc.isString(i))
			{
				c = new ClusterString(fc.getString(i));
			}

			else if(fc.isList(i))
			{
				c = new ClusterStringList(fc.getStringList(i));
			}

			else if(fc.isLong(i))
			{
				c = new ClusterLong(fc.getLong(i));
			}

			else
			{
				D.f("Unable to parse data in file configuration: " + i);
			}

			if(c != null)
			{
				clusters.put(i, c);
			}
		}
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

	public Object get(String k)
	{
		return clusters.get(k).get();
	}

	public long getLong(String k)
	{
		return (long) clusters.get(k).get();
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

	@SuppressWarnings("unchecked")
	public void trySet(String k, Object o)
	{
		if(o instanceof Integer)
		{
			set(k, (Integer) o);
		}

		if(o instanceof Boolean)
		{
			set(k, (Boolean) o);
		}

		if(o instanceof Double)
		{
			set(k, (Double) o);
		}

		if(o instanceof String)
		{
			set(k, (String) o);
		}

		if(o instanceof Long)
		{
			set(k, (Long) o);
		}

		if(o instanceof List)
		{
			set(k, (List<String>) o);
		}

		D.f("Failed to parse object type " + o.getClass().toString());
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

	public void set(String k, long d)
	{
		clusters.put(k, new ClusterLong(d));
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
