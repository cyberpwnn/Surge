package surge.cluster;

import java.io.File;
import java.io.IOException;

public class YamlDataOutput implements IDataOutput
{
	@Override
	public void write(DataCluster c, File f)
	{
		try
		{
			c.toFileConfiguration().save(f);
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
