package me.cmesh.BouncyBeds;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class BouncyBedsPlayerListener extends PlayerListener
{
	private static BouncyBeds plugin;
	
	public BouncyBedsPlayerListener(BouncyBeds instance)
	{
		plugin = instance;
	}
	
	public void onPlayerMove(PlayerMoveEvent event)
	{		
		Player player  = event.getPlayer();	
		
		Location loc  = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
		
		Location loc2  = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY() -0.5, event.getTo().getZ());
		
		if(loc.getBlock().getTypeId() == 26)
		{
			if(!plugin.bounceHight.containsKey(player.getUniqueId()))
			{
				plugin.bounceHight.put(player.getUniqueId(), .2);
			}
			else
			{
				if(plugin.bounceHight.get(player.getUniqueId()) <= 3.6)
				{
					plugin.bounceHight.put(player.getUniqueId(), plugin.bounceHight.get(player.getUniqueId())+.2);
				}
			}
			
			plugin.fall.put(player.getUniqueId(), true);
			Vector dir = player.getLocation().getDirection().multiply(0.3);
			dir.setY(plugin.bounceHight.get(player.getUniqueId()));
			player.setVelocity(dir);
			player.setFallDistance(0);
		}
		
		if(plugin.fall.containsKey(player.getUniqueId()) || plugin.bounceHight.containsKey(player.getUniqueId()))
		{
			if(plugin.fall.get(player.getUniqueId()) && loc2.getBlock().getTypeId() != 0 && loc2.getBlock().getTypeId() != 26)
			{
				player.setFallDistance(0);
				plugin.fall.put(player.getUniqueId(), false);
				
				player.sendMessage(loc.getBlock().getTypeId() + "," + loc2.getBlock().getTypeId());
			}
			
			if(!plugin.fall.get(player.getUniqueId()) && plugin.bounceHight.get(player.getUniqueId()) != 0.0)
			{
				plugin.bounceHight.put(player.getUniqueId(), 0.0);
			}
		}
	}
}
