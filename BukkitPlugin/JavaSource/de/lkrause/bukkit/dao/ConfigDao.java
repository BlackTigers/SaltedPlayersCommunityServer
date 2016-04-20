package de.lkrause.bukkit.dao;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import de.lkrause.bukkit.CommunityServerPlugin;
import de.lkrause.bukkit.model.DataModel;

public class ConfigDao {

	public static void writeSignToConfig(Location pLocation, int pGameMode, int pMaxPlayers, String pSignMode, String pSignId) {
		CommunityServerPlugin lPlugin = DataModel.getInstance().getPlugin();
		
		FileConfiguration lConfig = lPlugin.getConfig();
		
		lConfig.createSection("Signs." + pSignId);
		lConfig.set("Signs." + pSignId + ".WorldId", pLocation.getWorld().getName());
		lConfig.set("Signs." + pSignId + ".CoordX", pLocation.getBlockX());
		lConfig.set("Signs." + pSignId + ".CoordY", pLocation.getBlockY());
		lConfig.set("Signs." + pSignId + ".CoordZ", pLocation.getBlockZ());
		lConfig.set("Signs." + pSignId + ".MaxPlayers", pMaxPlayers + "");
		lConfig.set("Signs." + pSignId + ".SignMode", pSignMode);
		lConfig.set("Signs." + pSignId + ".GameMode", pGameMode + "");
		
		lPlugin.saveConfig();
	}
	
	public static int getMaxPlayers(String pSignId) {
		CommunityServerPlugin lPlugin = DataModel.getInstance().getPlugin();
		FileConfiguration lConfig = lPlugin.getConfig();
		return lConfig.getInt("Signs." + pSignId + ".MaxPlayers");
	}
	
	public static int getGameMode(String pSignId) {
		CommunityServerPlugin lPlugin = DataModel.getInstance().getPlugin();
		FileConfiguration lConfig = lPlugin.getConfig();
		return lConfig.getInt("Signs." + pSignId + ".GameMode");
	}
	
	public static int getSignMode(String pSignId) {
		CommunityServerPlugin lPlugin = DataModel.getInstance().getPlugin();
		FileConfiguration lConfig = lPlugin.getConfig();
		return lConfig.getInt("Signs." + pSignId + ".SignMode");
	}
	
	public static Location getLocation(String pSignId) {
		CommunityServerPlugin lPlugin = DataModel.getInstance().getPlugin();
		FileConfiguration lConfig = lPlugin.getConfig();
		int lCoordX = lConfig.getInt("Signs." + pSignId + ".CoordX");
		int lCoordY = lConfig.getInt("Signs." + pSignId + ".CoordY");
		int lCoordZ = lConfig.getInt("Signs." + pSignId + ".CoordZ");
		String lWorldId = lConfig.getString("Signs." + pSignId + ".WorldId");
		World lWorld = lPlugin.getServer().getWorld(lWorldId);
		
		return new Location(lWorld, lCoordX, lCoordY, lCoordZ);
	}
}
