package de.lkrause.bukkit.service;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.lkrause.bukkit.model.DataModel;

public class CastleRushThread implements Runnable {

	private static final DataModel DATA = DataModel.getInstance();
	
	private int mGameId;
	
	public CastleRushThread(int pGameId) {
		mGameId = pGameId;
	}
	
	@Override
	public void run() {
		
		Location lCenterTeam1 = DATA.getCastleRush(mGameId).getBase(0);
		Location lCenterTeam2 = DATA.getCastleRush(mGameId).getBase(1);
		
		Player lPlayer1 = DATA.getCastleRush(mGameId).getPlayer1();
		Player lPlayer2 = DATA.getCastleRush(mGameId).getPlayer2();
		
		if (lPlayer1.getLocation().distance(lCenterTeam1) >= 47) {
			Vector lPlayer1Movement = lPlayer1.getVelocity();
			lPlayer1.setVelocity(lPlayer1Movement.multiply(-2));
		}
		if (lPlayer2.getLocation().distance(lCenterTeam2) >= 47) {
			Vector lPlayer2Movement = lPlayer2.getVelocity();
			lPlayer2.setVelocity(lPlayer2Movement.multiply(-2));
		}
	}

}
