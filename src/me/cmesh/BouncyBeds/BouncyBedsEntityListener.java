package me.cmesh.BouncyBeds;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

public class BouncyBedsEntityListener extends EntityListener 
{
	private static BouncyBeds plugin;
	
	public BouncyBedsEntityListener(BouncyBeds bouncyBeds) 
	{
		plugin = bouncyBeds;
	}
	
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
