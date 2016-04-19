package de.lkrause.bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.lkrause.bukkit.model.DataModel;

public class EventListener implements Listener {

	private Map<String, String> mGameModes = new HashMap<String, String>();
	private Map<String, String> mMaps = new HashMap<String, String>();
	private Map<String, SignChangeEvent> mSigns = new HashMap<String, SignChangeEvent>();
	private CommunityServerPlugin mPlugin;
	
	public EventListener(CommunityServerPlugin pPlugin) {
		
		mPlugin = pPlugin;
		mPlugin.getServer().getPluginManager().registerEvents(this, mPlugin);
		
		mGameModes.put("CastleRush", "Castle Rush");
		mGameModes.put("BedWars", "Bed Wars");
		mGameModes.put("KitPvP", "Kit PvP");
		mGameModes.put("Craftattack", "Craftattack");
		mGameModes.put("SkyPvP", "Sky PvP");
		mGameModes.put("JumpPvP", "Jump PvP");

		List<World> lWorlds = new ArrayList<World>();
		lWorlds.addAll(mPlugin.getServer().getWorlds());
		
		for (World lWorld : lWorlds) {
			mMaps.put(lWorld.getName(), mPlugin.getConfig().getString("Maps." + lWorld.getName()));
		}
	}
	
	@EventHandler
	public void onPlayerAttemptJoin(PlayerInteractEvent pEvent) {
		if (pEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (pEvent.getClickedBlock().getState() instanceof Sign) {
				Sign lClickedSign = (Sign) pEvent.getClickedBlock().getState();
				String[] lLines = lClickedSign.getLines();
				String lSpielerzeile = lLines[2];
				int lMaxSpieler = 0;
				int lAnzahlSpieler = 0;
				int lLengthSpielerzeile = lSpielerzeile.length();
				
				int lLengthZahl1 = lSpielerzeile.indexOf('/') - 1;
				int lLengthZahl2 = lLengthSpielerzeile - (lSpielerzeile.indexOf('/') + 2);
				
				lAnzahlSpieler = Integer.parseInt(lSpielerzeile.substring(0, lLengthZahl1));
				lMaxSpieler = Integer.parseInt(lSpielerzeile.substring(lLengthSpielerzeile - lLengthZahl2));
				pEvent.getPlayer().sendMessage("Anzahl Spieler: " + lAnzahlSpieler + " Maximale Spieler: " + lMaxSpieler);
				
				if (lAnzahlSpieler != lMaxSpieler) {
					lClickedSign.setLine(2, Integer.toString(lAnzahlSpieler + 1) + " / " + Integer.toString(lMaxSpieler));
					pEvent.getPlayer().teleport(DataModel.getInstance().loadSpawn(lLines[1]));
					
				}
				lClickedSign.update();
			}
		}
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent pSign) {
		
		String[] lLines = pSign.getLines();
		
		if (validSign(lLines)) {
			String lMapId = lLines[1];
			DataModel.getInstance().addWorld(DataModel.getInstance().getPlugin().getServer().getWorld(lLines[1]));
			
			pSign.setLine(0, ChatColor.RED + mGameModes.get(lLines[0]));
			pSign.setLine(1, ChatColor.RED + lLines[1]);
			pSign.setLine(2, ChatColor.RED + "0 / " + mGameModes.get(lLines[2]));
			pSign.setLine(3, ChatColor.RED + "");
			
			mSigns.put(lMapId, pSign);
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent pEvent) {
		
		pEvent.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Herzlich Willkommen auf dem Salted Players Community Server");
	}
	
	private boolean validSign(String[] pLines) {
		
		return mGameModes.containsKey(pLines[0]) && mMaps.containsKey(pLines[1]) && (pLines[2].length() != 0) && (pLines[3].equalsIgnoreCase("[teleport]") || pLines[3].equalsIgnoreCase("[join]"));
	}
}
