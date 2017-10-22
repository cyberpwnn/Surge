package surge.cluster;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ConfigurationDataInput
{
	@SuppressWarnings("unchecked")
	public void read(IConfigurable c, File f) throws Exception
	{
		DataCluster current = new DataCluster();
		Class<? extends IConfigurable> clazz = c.getClass();

		if(!f.exists())
		{
			current = fillDefaults(c);
			new YamlDataOutput().write(current, f);
		}

		current = new YamlDataInput().read(f);

		for(Field i : clazz.getDeclaredFields())
		{
			if(Modifier.isStatic(i.getModifiers()) || Modifier.isFinal(i.getModifiers()) || !Modifier.isPublic(i.getModifiers()) || !i.isAnnotationPresent(Key.class))
			{
				continue;
			}

			Key k = i.getAnnotation(Key.class);
			Object raw = current.get(k.value());

			switch(current.getType(k.value()))
			{
				case BOOLEAN:
					i.set(c, (Boolean) raw);
					break;
				case DOUBLE:
					i.set(c, (Double) raw);
					break;
				case INT:
					i.set(c, (Integer) raw);
					break;
				case LONG:
					i.set(c, (Long) raw);
					break;
				case STRING:
					i.set(c, (String) raw);
					break;
				case STRING_LIST:
					i.set(c, (List<String>) raw);
					break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public DataCluster fillDefaults(IConfigurable c) throws ReflectiveOperationException
	{
		DataCluster cc = new DataCluster();
		Class<? extends IConfigurable> clazz = c.getClass();

		for(Field i : clazz.getDeclaredFields())
		{
			if(Modifier.isStatic(i.getModifiers()) || Modifier.isFinal(i.getModifiers()) || !Modifier.isPublic(i.getModifiers()) || !i.isAnnotationPresent(Key.class))
			{
				continue;
			}

			Key k = i.getAnnotation(Key.class);
			String key = k.value();
			Object val = i.get(c);

			if(val instanceof Integer)
			{
				cc.set(key, (Integer) val);
			}

			else if(val instanceof String)
			{
				cc.set(key, (String) val);
			}

			else if(val instanceof Double)
			{
				cc.set(key, (Double) val);
			}

			else if(val instanceof Boolean)
			{
				cc.set(key, (Boolean) val);
			}

			else if(val instanceof Long)
			{
				cc.set(key, (Long) val);
			}

			else if(val instanceof List)
			{
				cc.set(key, (List<String>) val);
			}

			else
			{
				throw new ReflectiveOperationException("Unknown TYPE: " + val.getClass());
			}
		}

		return cc;
	}
}
