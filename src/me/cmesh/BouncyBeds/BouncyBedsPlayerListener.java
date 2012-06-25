package me.cmesh.BouncyBeds;

import java.util.HashMap;
import java.util.UUID;

import net.h31ix.anticheat.api.AnticheatAPI;
import net.h31ix.anticheat.manage.CheckType;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class BouncyBedsPlayerListener implements Listener
{
	private static BouncyBeds plugin;
	private HashMap<UUID, Integer> runners;
	
	
	public BouncyBedsPlayerListener(BouncyBeds instance)
	{
		plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runners = new HashMap<UUID, Integer>();
	}
	
	private class ACheatHelper implements Runnable
	{
		private Player player;
		
		public ACheatHelper(Player p)
		{
			super();
			player = p;
		}
		
		public void run()
		{
			AnticheatAPI.exemptPlayer(player, CheckType.FLY);
			player.sendMessage("Exempt " + this.hashCode());
			try 
			{
				Thread.sleep(1000);
			}
			catch (Exception e)// We were canceled.  Another runner now has exemption control for this player.  
			{
				return;
			}
			player.sendMessage("Unexempt " + this.hashCode());
			AnticheatAPI.unexemptPlayer(player, CheckType.FLY);
		}
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

			exemptPlayer(player);
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
				exemptPlayer(player);
				player.setFallDistance(0);
				plugin.fall.put(key, false);
			}
			
			if(!plugin.fall.get(key) && plugin.bounceVelocity.get(key) != 0.0 || player.isSneaking())
			{
				plugin.bounceVelocity.put(player.getUniqueId(), 0.0);
			}
		}
	}
	
	private void exemptPlayer(Player player)
	{
		if(plugin.antiCheatEnabled)
		{
			UUID key = player.getUniqueId();
			BukkitScheduler s = plugin.getServer().getScheduler();
			
			if(runners.containsKey(key))
			{
				s.cancelTask(runners.get(key));
				runners.remove(key);
			}
			Integer id = s.scheduleAsyncDelayedTask(plugin, new ACheatHelper(player));
			runners.put(key, id);
		}
	}
}