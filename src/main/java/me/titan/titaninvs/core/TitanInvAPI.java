package me.titan.titaninvs.core;

import me.titan.titaninvs.listeners.TitanInventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * API main class, {@link #init(JavaPlugin)}
 * must be run in onEnable on your main plugin class.
 *
 * @author TitanDev
 * @since 2022
 * @version 1.0.0
 *
 */
public class TitanInvAPI {

	private static JavaPlugin plugin;
	public static void init(JavaPlugin plugin){
		Bukkit.getPluginManager().registerEvents(new TitanInventoryListener(),plugin);
		TitanInvAPI.plugin = plugin;

	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}
}
