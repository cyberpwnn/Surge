package surge;

import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		Surge.createAmp(this).connect();
	}

	@Override
	public void onDisable()
	{
		Surge.getAmp().disconnect();
	}
}
