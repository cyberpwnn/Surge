package surge.server;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import surge.Surge;

@SuppressWarnings("deprecation")
public abstract class WorldMonitor extends Thread implements Listener
{
	private boolean chunksChanged = true;
	private boolean dropChanged = true;
	private boolean tileChanged = true;
	private boolean livingChanged = true;
	private boolean totalChanged = true;
	private boolean updated = false;
	private int totalChunks = 0;
	private int totalDrops = 0;
	private int totalTiles = 0;
	private int totalLiving = 0;
	private int totalEntities = 0;

	public WorldMonitor()
	{
		Surge.register(this);
	}

	@Override
	public void run()
	{
		while(!interrupted())
		{
			try
			{
				sample();
				Thread.sleep(50);
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		Surge.unregister(this);
	}

	public abstract void updated(int totalChunks, int totalDrops, int totalTiles, int totalLiving, int totalEntities);

	@EventHandler
	public void on(ChunkLoadEvent e)
	{
		chunksChanged = true;
		tileChanged = true;
		livingChanged = true;
		dropChanged = true;
	}

	@EventHandler
	public void on(ChunkUnloadEvent e)
	{
		chunksChanged = true;
		tileChanged = true;
		livingChanged = true;
		dropChanged = true;
	}

	@EventHandler
	public void on(EntitySpawnEvent e)
	{
		livingChanged = true;
	}

	@EventHandler
	public void on(EntityDeathEvent e)
	{
		livingChanged = true;
	}

	@EventHandler
	public void on(PlayerDropItemEvent e)
	{
		dropChanged = true;
	}

	@EventHandler
	public void on(PlayerPickupItemEvent e)
	{
		dropChanged = true;
	}

	@EventHandler
	public void on(EntityPickupItemEvent e)
	{
		dropChanged = true;
	}

	@EventHandler
	public void on(BlockPlaceEvent e)
	{
		tileChanged = true;
	}

	@EventHandler
	public void on(BlockBreakEvent e)
	{
		tileChanged = true;
	}

	private void doUpdate()
	{
		updated = true;
	}

	private void sample()
	{
		if(chunksChanged || dropChanged || tileChanged || livingChanged)
		{
			totalChanged = true;
		}

		if(chunksChanged)
		{
			sampleChunkCount();
			chunksChanged = false;
			doUpdate();
		}

		if(dropChanged)
		{
			sampleDropCount();
			dropChanged = false;
			doUpdate();
		}

		if(tileChanged)
		{
			sampleTileCount();
			tileChanged = false;
			doUpdate();
		}

		if(livingChanged)
		{
			sampleLivingCount();
			livingChanged = false;
			doUpdate();
		}

		if(totalChanged)
		{
			sampleTotalCount();
			totalChanged = false;
			doUpdate();
		}

		if(updated)
		{
			updated(totalChunks, totalDrops, totalTiles, totalLiving, totalEntities);
		}
	}

	private void sampleTotalCount()
	{
		totalEntities = 0;

		for(World i : Bukkit.getWorlds())
		{
			totalEntities += i.getEntities().size();
		}
	}

	private void sampleLivingCount()
	{
		totalLiving = 0;

		for(World i : Bukkit.getWorlds())
		{
			totalLiving += i.getLivingEntities().size();
		}
	}

	private void sampleTileCount()
	{
		totalTiles = 0;

		for(World i : Bukkit.getWorlds())
		{
			for(Chunk j : i.getLoadedChunks())
			{
				totalTiles += j.getTileEntities().length;
			}
		}
	}

	private void sampleDropCount()
	{
		totalDrops = 0;

		for(World i : Bukkit.getWorlds())
		{
			totalDrops += i.getEntitiesByClass(Item.class).size();
		}
	}

	private void sampleChunkCount()
	{
		totalChunks = 0;

		for(World i : Bukkit.getWorlds())
		{
			totalChunks += i.getLoadedChunks().length;
		}
	}
}