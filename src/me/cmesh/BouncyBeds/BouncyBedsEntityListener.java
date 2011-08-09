package me.cmesh.BouncyBeds;

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
			if(!plugin.fall.containsKey(event.getEntity().getUniqueId()))
			{
				return;
			}
			
			if(plugin.fall.get(event.getEntity().getUniqueId()))
			{
				event.setCancelled(true);
    		}
		}
	}

}
