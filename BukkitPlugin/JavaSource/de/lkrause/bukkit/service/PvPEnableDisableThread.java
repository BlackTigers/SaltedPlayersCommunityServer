package de.lkrause.bukkit.service;

import org.bukkit.World;

public class PvPEnableDisableThread implements Runnable {

	private boolean mEnable;
	private World mWorld;
	
	public PvPEnableDisableThread(World pWorld, boolean pEnable) {
		mWorld = pWorld;
		mEnable = pEnable;
	}
	
	@Override
	public void run() {
		mWorld.setPVP(mEnable);
	}

}
