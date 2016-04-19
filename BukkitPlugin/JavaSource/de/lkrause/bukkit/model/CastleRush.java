package de.lkrause.bukkit.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.lkrause.bukkit.CommunityServerPlugin;

public class CastleRush {
	
	private Map<Player, Integer> mPlayerMapping = new HashMap<Player, Integer>();
	
	private List<Player> mPlayerList = new ArrayList<Player>();
	
	private World mPlayedWorld;
	
	private List<Location> mSpawnPoints = new ArrayList<Location>();
	private Location mWorldSpawn;
	
	public CastleRush(World pWorld) {
		mPlayedWorld = pWorld;
		
		initSpawns();
	}
	
	public void addPlayer(Player pPlayer) {
		if (mPlayerMapping.size() == 0) {
			mPlayerMapping.put(pPlayer, 0);
		} else {
			mPlayerMapping.put(pPlayer, 1);
		}
		mPlayerList.add(pPlayer);
	}
	
	public Player getPlayer1() {
		return mPlayerList.get(0);
	}
	
	public Player getPlayer2() {
		return mPlayerList.get(1);
	}
	
	public List<Player> getPlayers() {
		return Collections.unmodifiableList(mPlayerList);
	}
	
	public int getTeam(Player pPlayer) {
		return mPlayerMapping.get(pPlayer);
	}
	
	public World getWorld() {
		return mPlayedWorld;
	}
	
	public Location getBase(int pTeamId) {
		return mSpawnPoints.get(pTeamId);
	}
	
	public Location getSpawn() {
		return mWorldSpawn;
	}
	
	private void initSpawns() {
		FileConfiguration lConfig = DataModel.getInstance().getPlugin().getConfig();
		
		int lCoordX = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".green.CoordX");
		int lCoordY = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".green.CoordY");
		int lCoordZ = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".green.CoordZ");
		mSpawnPoints.add(new Location(mPlayedWorld, lCoordX, lCoordY, lCoordZ));
		
		lCoordX = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".red.CoordX");
		lCoordY = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".red.CoordY");
		lCoordZ = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".red.CoordZ");
		mSpawnPoints.add(new Location(mPlayedWorld, lCoordX, lCoordY, lCoordZ));
		
		lCoordX = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".spawn.CoordX");
		lCoordY = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".spawn.CoordY");
		lCoordZ = lConfig.getInt("CastleRush." + mPlayedWorld.getName() + ".spawn.CoordZ");
		mWorldSpawn = new Location(mPlayedWorld, lCoordX, lCoordY, lCoordZ);
	}
}
