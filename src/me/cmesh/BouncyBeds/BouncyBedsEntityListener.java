package me.cmesh.BouncyBeds;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class BouncyBedsEntityListener implements Listener
{
	private static BouncyBeds plugin;
	
	public BouncyBedsEntityListener(BouncyBeds bouncyBeds) 
	{
		plugin = bouncyBeds;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntity() instanceof Player)
		{
			UUID key = event.getEntity().getUniqueId();
			if(plugin.fall.containsKey(key))
			{
				event.setCancelled(plugin.fall.get(key));
			}
		}
	}

}
