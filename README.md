# Surge
A quick, shade-able api for making bukkit plugins quickly

### Maven Coordinates
``` xml
<repository>
  <id>surge</id>
  <url>https://raw.githubusercontent.com/cyberpwnn/Surge/master/repository/</url>
</repository>

<dependency>
  <groupId>surge</groupId>
  <artifactId>Surge</artifactId>
  <version>1.0</version>
  <scope>provided</scope>
</dependency>
```

### Example Plugin
``` java
package surge;

import java.io.File;

import surge.control.AmpedPlugin;
import surge.sched.Task;
import surge.util.D;
import surge.util.Protocol;

public class TestPlugin extends AmpedPlugin
{
	@Override
	public void onStart(Protocol serverProtocol)
	{
		D.v("Minecraft Version: " + serverProtocol.toString());

		// Get your plugin instance anywhere
		Surge.getAmp().getPluginInstance();

		// Track a file
		Surge.getHotloadManager().track(new File("plugins/SomeJar.jar"), new Runnable()
		{
			@Override
			public void run()
			{
				// This is already checked for your plugin jar
				// Surge automatically reloads it when it's recompiled
				D.w("File was modified!");
			}
		});

		// Logging
		D.l(this, "Simple logging, with instance ref");
		D.l("info");
		D.v("verbose");
		D.f("fail");
		D.w("warning");
		D.s("success");

		// Tasks
		new Task("update-check?")
		{
			@Override
			public void run()
			{
				D.v("fires once");
			}
		};

		// Repeating Tasks
		new Task("repeat-every-tick", 0)
		{
			@Override
			public void run()
			{
				D.v("Run Time: " + getActiveTime());
				D.v("Compute Time: " + getComputeTime());
				D.v("Total Compute Time: " + getTotalComputeTime());

				// cancel it anywhere any time
				cancel();
			}
		};

		// Repeating Tasks with limits
		new Task("repeat every second for 10 seconds", 20, 10)
		{
			@Override
			public void run()
			{
				D.v("Tick!");
			}
		};
	}

	@Override
	public void onStop()
	{
		D.v("Plugin onStop()");
	}

	@Override
	public void onPreInit()
	{
		D.v("Plugin onPreInit()");
	}

	@Override
	public void onPostInit()
	{
		D.v("Plugin onPostInit()");
	}
}

```
