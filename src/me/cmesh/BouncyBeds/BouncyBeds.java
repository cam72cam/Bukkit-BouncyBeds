package me.cmesh.BouncyBeds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncyBeds extends JavaPlugin 
{	
	public HashMap<UUID, Boolean> fall = new HashMap<UUID, Boolean>();
	public HashMap<UUID, Double> bounceVelocity = new HashMap<UUID, Double>();
	public List<Material> materials = Arrays.asList(Material.BED, Material.BED_BLOCK, Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2);
	
	public boolean enabled = true;
	public double maxBounce = 3.0;
	public boolean antiCheatEnabled;
	
	public void onEnable() 
	{
		Plugin antiCheat = Bukkit.getServer().getPluginManager().getPlugin("AntiCheat");
		
		if(antiCheat != null && antiCheat.isEnabled())
		{
			antiCheatEnabled = true;
		}
		
		FileConfiguration config = getConfig();
		
		maxBounce = config.getDouble("bouncybeds.max", maxBounce);
		config.set("bouncybeds.max", maxBounce);
		
		saveConfig();
		
		new BouncyBedsPlayerListener(this);
		new BouncyBedsEntityListener(this);
	}
	
	public void onDisable()
	{
    	getServer().getScheduler().cancelTasks(this);	
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
	
	private boolean hasPermission(Player player, String permission)
	{
		return player.isOp() || player.hasPermission(permission) || true;
	}
}