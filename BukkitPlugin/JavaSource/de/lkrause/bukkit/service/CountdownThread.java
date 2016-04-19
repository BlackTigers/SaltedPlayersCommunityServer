package de.lkrause.bukkit.service;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CountdownThread implements Runnable {

	private int mCounter;
	private List<Player> mPlayers;
	private boolean mSekunden;
	
	public CountdownThread(int pCounter, List<Player> pPlayers, boolean pSekunden) {
		
		mCounter = pCounter;
		mPlayers = pPlayers;
		mSekunden = pSekunden;
	}
	
	@Override
	public void run() {
		
		if (!mSekunden) {
			for (Player lPlayer : mPlayers) {
				lPlayer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[Castle Rush]" + ChatColor.RESET + "" + ChatColor.GREEN + " Die Bauzeit endet in " + mCounter + " Minuten.");
			}
		} else {
			for (Player lPlayer : mPlayers) {
				lPlayer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[Castle Rush]" + ChatColor.RESET + "" + ChatColor.GREEN + " Die Bauzeit endet in " + mCounter + " Sekunden.");
			}
		}
	}

}
