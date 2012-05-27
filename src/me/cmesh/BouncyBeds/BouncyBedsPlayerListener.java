package me.cmesh.BouncyBeds;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class BouncyBedsPlayerListener implements Listener
{
	private static BouncyBeds plugin;
	
	public BouncyBedsPlayerListener(BouncyBeds instance)
	{
		plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event)
	{		
		Player player  = event.getPlayer();

		if(player.isSleeping())
		{
			return;
		}
		
		UUID key = player.getUniqueId();
		
		Location loc  = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
		Location loc2  = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY() -0.5, event.getTo().getZ());
		
		if(loc.getBlock().getTypeId() == 26 && plugin.enabled)
		{
			if(!plugin.bounceHight.containsKey(key))
			{
				plugin.bounceHight.put(player.getUniqueId(), .2);
			}
			else
			{
				if(plugin.bounceHight.get(key) <= plugin.maxBounce)
				{
					plugin.bounceHight.put(key, plugin.bounceHight.get(key)+.2);
				}
			}
			
			plugin.fall.put(player.getUniqueId(), true);
			Vector dir = player.getLocation().getDirection().multiply(0.3);
			dir.setY(plugin.bounceHight.get(key));
			player.setVelocity(dir);
			player.setFallDistance(0);
		}
		
		if(plugin.fall.containsKey(key))
		{
			if(plugin.fall.get(key) && loc2.getBlock().getTypeId() != 0 && loc2.getBlock().getTypeId() != 26)
			{
				player.setFallDistance(0);
				plugin.fall.put(key, false);
			}
			
			if(!plugin.fall.get(key) && plugin.bounceHight.get(key) != 0.0)
			{
				plugin.bounceHight.put(player.getUniqueId(), 0.0);
			}
		}
	}
}
