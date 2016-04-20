package de.lkrause.bukkit.model;

public class PlayerCounts {

	private int mMax;
	private int mActual;
	
	public PlayerCounts(int pMax) {
		mMax = pMax;
	}
	
	public void setMax(int pMax) {
		mMax = pMax;
	}
	
	public void addPlayer() {
		mActual++;
	}
	
	public void removePlayer() {
		mActual--;
	}
	
	public int getMax() {
		return mMax;
	}
	
	public int getActual() {
		return mActual;
	}
	
	public boolean getMapVoll() {
		return mMax == mActual;
	}
}
