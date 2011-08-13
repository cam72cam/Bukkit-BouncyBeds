package me.cmesh.BouncyBeds;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class BouncyBeds extends JavaPlugin 
{
	public static final Logger log = Logger.getLogger("Minecraft");
	
	private final BouncyBedsPlayerListener playerListener = new BouncyBedsPlayerListener(this);
	private final BouncyBedsEntityListener entityListener = new BouncyBedsEntityListener(this);

	public PermissionHandler Permissions = null;
	
	public HashMap<UUID, Boolean> fall = new HashMap<UUID, Boolean>();
	public HashMap<UUID, Double> bounceHight = new HashMap<UUID, Double>();
	public boolean enabled = true;
	public double maxBounce = 3.0;
	
	public void onEnable() 
	{
		PluginManager pm = getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);

		Plugin permissions = getServer().getPluginManager().getPlugin("Permissions");
		if (Permissions == null)
		{
			Permissions = (permissions != null) ? ((Permissions)permissions).getHandler() : null;
		}
		
		reload();
		
		log.info(getDescription().getName()+" version "+getDescription().getVersion()+" is enabled!");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if(commandLabel.equalsIgnoreCase("bounce") && sender instanceof Player) 
		{
			if(hasPermission((Player) sender, "bouncybeds.set"))
			{
				try
				{
					enabled = Boolean.parseBoolean(args[0]);
					sender.sendMessage(enabled ? "Bouncing Enabled":"Bouncing Disabled");
					return true;
				}
				catch (Exception e){}
			}
		}
		return false;
	}
	
	public void onDisable() 
	{
		log.info(getDescription().getName()+" version "+getDescription().getVersion()+" is disabled!");
	}
	
	private boolean hasPermission(Player player, String permission)
	{
		return 
		player.isOp() 
		|| player.hasPermission(permission) 
		|| (Permissions ==null ? false : Permissions.has(player,permission));
	}
	
	private void reload()
	{
		getConfiguration().load();
		maxBounce = getConfiguration().getDouble("bouncybeds.max", maxBounce);
		getConfiguration().save();
	}
}
