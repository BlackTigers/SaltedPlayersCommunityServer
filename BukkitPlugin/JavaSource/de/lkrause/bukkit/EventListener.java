package de.lkrause.bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.lkrause.bukkit.dao.ConfigDao;
import de.lkrause.bukkit.model.DataModel;
import de.lkrause.bukkit.model.GameMode;
import de.lkrause.bukkit.model.PlayerCounts;

public class EventListener implements Listener {

	private Map<String, String> mGameModes = new HashMap<String, String>();
	private Map<String, String> mMaps = new HashMap<String, String>();
	private Map<Location, String> mSignIdsByLocation = new HashMap<Location, String>();
	private Map<String, PlayerCounts> mMapToPlayerCount = new HashMap<String, PlayerCounts>();
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
				
				PlayerCounts lPlayerCounts = mMapToPlayerCount.get(lLines[1]);
				
				if (!lPlayerCounts.getMapVoll()) {
					lClickedSign.setLine(2, lPlayerCounts.getActual() + " / " + lPlayerCounts.getMax());
					pEvent.getPlayer().teleport(DataModel.getInstance().loadSpawn(lLines[1]));
					
				}
				lClickedSign.update();
			}
		}
	}
	
	
	public void onSignBreak(BlockBreakEvent pEvent) {
		if (pEvent.getBlock().getType() == Material.SIGN) {
			Sign lSign = (Sign) pEvent.getBlock();
			
			String[] lLines = lSign.getLines();
			
			if (mMaps.containsKey(lLines[1])) {
				if (mMapToPlayerCount.get(lLines[1]).getActual() != 0) {
					for (Player lPlayer: mPlugin.getServer().getOnlinePlayers()) {
						if (lPlayer.getWorld().equals(mPlugin.getServer().getWorld(lLines[1]))) {
							lPlayer.teleport(DataModel.getInstance().getLobbySpawn());
						}
					}
				}
				mMapToPlayerCount.remove(lLines[1]);
				mSignIdsByLocation.remove(lSign.getBlock().getLocation());
			}
		}
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent pSign) {
		
		String[] lLines = pSign.getLines();
		
		if (validSign(lLines)) {
			DataModel.getInstance().addWorld(DataModel.getInstance().getPlugin().getServer().getWorld(lLines[1]));

			mMapToPlayerCount.put(lLines[1], new PlayerCounts(Integer.parseInt(lLines[2])));
			mSignIdsByLocation.put(pSign.getBlock().getLocation(), Integer.toString(pSign.getBlock().hashCode()));
			
			int lGameMode;
			switch (lLines[0]) {
			case "CastleRush":
				lGameMode = GameMode.CASTLERUSH;
				break;
			case "BedWars":
				lGameMode = GameMode.BEDWARS;
				break;
			case "SkyPvP":
				lGameMode = GameMode.SKYPVP;
				break;
			case "KitPvP":
				lGameMode = GameMode.KITPVP;
				break;
			case "Craftattack":
				lGameMode = GameMode.CRAFTATTACK;
				break;
			}
			
			ConfigDao.writeSignToConfig(pSign.getBlock().getLocation(), pGameMode, pMaxPlayers, pSignMode, pSignId)

			
			pSign.setLine(0, ChatColor.GREEN + mGameModes.get(lLines[0]));
			pSign.setLine(1, ChatColor.GREEN + lLines[1]);
			pSign.setLine(2, ChatColor.GREEN + "0 / " + lLines[2]);
			pSign.setLine(3, ChatColor.GREEN + "");
		}
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent pEvent) {
		
		pEvent.getPlayer().sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Herzlich Willkommen auf dem Salted Players Community Server");
	}
	
	private boolean validSign(String[] pLines) {
		
		for (int i = 0; i < pLines[2].length(); i++) {
			if (!Character.isDigit(pLines[2].charAt(i))) {
				return false;
			}
		}
		
		return mGameModes.containsKey(pLines[0]) && mMaps.containsKey(pLines[1]) && (pLines[2].length() != 0) && (pLines[3].equalsIgnoreCase("[teleport]") || pLines[3].equalsIgnoreCase("[join]"));
	}
}
