package surge.collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class GSound
{
	private String sound;
	private Sound iSound;
	private Float volume;
	private Float pitch;
	
	public GSound(String sound, Float volume, Float pitch)
	{
		this.sound = sound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public GSound(String sound)
	{
		this.sound = sound;
		this.volume = 1f;
		this.pitch = 1f;
	}
	
	public GSound(Sound iSound, Float volume, Float pitch)
	{
		this.iSound = iSound;
		this.volume = volume;
		this.pitch = pitch;
	}
	
	public GSound(Sound iSound)
	{
		this.iSound = iSound;
		this.volume = 1f;
		this.pitch = 1f;
	}
	
	public void play(Player p)
	{
		if(iSound != null)
		{
			p.playSound(p.getLocation(), iSound, volume, pitch);
		}
		
		if(sound != null)
		{
			String cmd = "playsound " + sound + " " + p.getName() + " " + p.getLocation().getX() + " " + p.getLocation().getY() + " " + p.getLocation().getZ() + " " + volume + " " + pitch;
			
			p.getServer().dispatchCommand(p.getServer().getConsoleSender(), cmd);
		}
	}
	
	public void play(Location l)
	{
		if(iSound != null)
		{
			l.getWorld().playSound(l, iSound, volume, pitch);
		}
		
		if(sound != null)
		{
			String cmd = "playsound " + sound + " @a " + l.getX() + " " + l.getY() + " " + l.getZ() + " " + volume + " " + pitch;
			
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);
		}
	}
	
	public void play(Player p, Vector v)
	{
		Location l = p.getLocation().clone().add(v);
		
		if(iSound != null)
		{
			p.playSound(l, iSound, volume, pitch);
		}
		
		if(sound != null)
		{
			String cmd = "playsound " + sound + " " + p.getName() + " " + l.getX() + " " + l.getY() + " " + l.getZ() + " " + volume + " " + pitch;
			
			p.getServer().dispatchCommand(p.getServer().getConsoleSender(), cmd);
		}
	}
	
	public String getSound()
	{
		return sound;
	}
	
	public void setSound(String sound)
	{
		this.sound = sound;
	}
	
	public Sound getiSound()
	{
		return iSound;
	}
	
	public void setiSound(Sound iSound)
	{
		this.iSound = iSound;
	}
	
	public Float getVolume()
	{
		return volume;
	}
	
	public void setVolume(Float volume)
	{
		this.volume = volume;
	}
	
	public Float getPitch()
	{
		return pitch;
	}
	
	public void setPitch(Float pitch)
	{
		this.pitch = pitch;
	}
}
