package de.lkrause.bukkit.service;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.lkrause.bukkit.model.DataModel;

public class CastleRushStart implements Runnable {

	private static final DataModel DATA = DataModel.getInstance();

	private int mGameId;
	private int mSchedulerId;
	
	public CastleRushStart(int pGameId, int pSchedulerId) {
		mGameId = pGameId;
		mSchedulerId = pSchedulerId;
	}
	
	@Override
	public void run() {
		List<Player> lPlayers = DATA.getCastleRush(mGameId).getPlayers();

		DATA.getPlugin().getServer().getScheduler().cancelTask(mSchedulerId);

		Location lSpawn = DATA.getCastleRush(mGameId).getSpawn();
		for (Player lPlayer : lPlayers) {
			lPlayer.teleport(lSpawn);
			lPlayer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[Castle Rush]" + ChatColor.RESET + "" + ChatColor.GREEN + " Die Spiele mögen beginnen.");
		}
		World lWorld = DATA.getCastleRush(mGameId).getWorld();
		
		lWorld.setPVP(false);
		
		DATA.getPlugin().getServer().getScheduler().scheduleAsyncDelayedTask(DATA.getPlugin(), new PvPEnableDisableThread(lWorld, true), 20*60);
	}

	
}
