package surge.cluster;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class ConfigurationDataOutput
{
	@SuppressWarnings("unchecked")
	public void write(IConfigurable c, File file) throws Exception
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

		new YamlDataOutput().write(cc, file);
	}
}
