package me.cmesh.BouncyBeds;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
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
		
		if(!plugin.enabled || !player.hasPermission("bouncybeds.bounce"))
		{
			return;
		}
		
		UUID key = player.getUniqueId();
		
		Location loc  = new Location(player.getWorld(), event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
		Location loc2  = loc.clone();
		loc2.add(0, -0.5, 0);
		
		if((plugin.materials.contains(loc.getBlock().getType()) || plugin.materials.contains(loc2.getBlock().getType())) &&
				!player.isSneaking())
		{
			if(!plugin.bounceVelocity.containsKey(key) || plugin.bounceVelocity.get(key) == 0.0)
			{
				plugin.bounceVelocity.put(player.getUniqueId(), -player.getVelocity().getY()+0.2);
			}
			if(plugin.bounceVelocity.get(key) <= plugin.maxBounce)
			{
				plugin.bounceVelocity.put(key, plugin.bounceVelocity.get(key)+.2);
			}
			
			plugin.fall.put(player.getUniqueId(), true);
			Vector dir = player.getLocation().getDirection().multiply(0.3);
			dir.setY(plugin.bounceVelocity.get(key));
			player.setVelocity(dir);
		}
		
		if(plugin.fall.containsKey(key))
		{
			if (plugin.fall.get(key) &&
				loc2.getBlock().getType() != Material.AIR &&
				!plugin.materials.contains(loc2.getBlock().getType()))
			{
				player.setFallDistance(0);
				plugin.fall.put(key, false);
			}
			
			if(!plugin.fall.get(key) && plugin.bounceVelocity.get(key) != 0.0 || player.isSneaking())
			{
				plugin.bounceVelocity.put(player.getUniqueId(), 0.0);
			}
		}
	}
}