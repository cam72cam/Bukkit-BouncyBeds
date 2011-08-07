package me.cmesh.BouncyBeds;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncyBeds extends JavaPlugin 
{
	public static final Logger log = Logger.getLogger("Minecraft");
	
	private final BouncyBedsPlayerListener playerListener = new BouncyBedsPlayerListener(this);
	private final BouncyBedsEntityListener entityListener = new BouncyBedsEntityListener(this);
	
	public HashMap<UUID, Boolean> fall = new HashMap<UUID, Boolean>();
	public HashMap<UUID, Double> bounceHight = new HashMap<UUID, Double>();
	
	public void onEnable() 
	{
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);

		log.info(getDescription().getName()+" version "+getDescription().getVersion()+" is enabled!");
	}
	
	public void onDisable() 
	{
		// TODO Auto-generated method stub

		log.info(getDescription().getName()+" version "+getDescription().getVersion()+" is disabled!");
	}
	
}
