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

public final class DataModel {

	private static final DataModel INSTANCE = new DataModel();

	private CommunityServerPlugin mPlugin;
	private Location mLobbySpawn;

	/** Liste aller Castle Rush Spiele. */
	private List<CastleRush> mCastleRush = new ArrayList<CastleRush>();

	/** Mapping welcher Spieler in welchem Spielmodus ist. */
	private Map<Player, Integer> mPlayerInGamemodes = new HashMap<Player, Integer>();

	/** Liste aller aktivierten Maps. */
	private List<World> mMapsEnabled = new ArrayList<World>();

	/** Map mit allen Spawns der Welten. */
	private Map<String, Location> mWorldSpawns = new HashMap<String, Location>();

	// TODO Karma
	private Map<Player, Integer> mPlayerKarma = new HashMap<Player, Integer>();

	public static final DataModel getInstance() {
		return INSTANCE;
	}

	private DataModel() {

	}

	public void init() {
		FileConfiguration lConfig = mPlugin.getConfig();

		int lCoordX = lConfig.getInt("Lobby.spawn.CoordX");
		int lCoordY = lConfig.getInt("Lobby.spawn.CoordY");
		int lCoordZ = lConfig.getInt("Lobby.spawn.CoordZ");
		mLobbySpawn = new Location(mPlugin.getServer().getWorld("Lobby"),
				lCoordX, lCoordY, lCoordZ);
	}

	public Location loadSpawn(String pWorld) {

		Location lSpawn;
		
		if (!mWorldSpawns.containsKey(pWorld)) {
			FileConfiguration lConfig = mPlugin.getConfig();

			int lCoordX = lConfig.getInt(pWorld + ".spawn.CoordX");
			int lCoordY = lConfig.getInt(pWorld + ".spawn.CoordY");
			int lCoordZ = lConfig.getInt(pWorld + ".spawn.CoordZ");

			lSpawn = new Location(mPlugin.getServer().getWorld(pWorld), lCoordX, lCoordY, lCoordZ);
			
			mWorldSpawns.put(pWorld, lSpawn);
		} else {
			lSpawn = mWorldSpawns.get(pWorld);
		}
		return lSpawn;

	}

	public int addCastleRushGame(World pWorld) {
		CastleRush lThisCastleRush = new CastleRush(pWorld);
		mCastleRush.add(lThisCastleRush);

		return mCastleRush.indexOf(lThisCastleRush);
	}

	public void addPlayerToCastleRush(int pGameId, Player pPlayer) {
		mCastleRush.get(pGameId).addPlayer(pPlayer);
		mPlayerInGamemodes.put(pPlayer, GameMode.CASTLERUSH);
	}

	public CastleRush getCastleRush(int pGameId) {
		return mCastleRush.get(pGameId);
	}

	public Location getLobbySpawn() {
		return mLobbySpawn;
	}

	public void addWorld(World pWorld) {
		mMapsEnabled.add(pWorld);
	}

	public List<World> getWorlds() {
		return Collections.unmodifiableList(mMapsEnabled);
	}

	public void setPlugin(CommunityServerPlugin pPlugin) {
		mPlugin = pPlugin;
	}

	public CommunityServerPlugin getPlugin() {
		return mPlugin;
	}
}
