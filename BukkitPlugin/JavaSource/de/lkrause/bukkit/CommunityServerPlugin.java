package de.lkrause.bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.lkrause.bukkit.model.CastleRush;
import de.lkrause.bukkit.model.DataModel;
import de.lkrause.bukkit.service.CastleRushStart;
import de.lkrause.bukkit.service.CastleRushThread;
import de.lkrause.bukkit.service.CountdownThread;

public class CommunityServerPlugin extends JavaPlugin {
	
	protected static DataModel mData;


	@Override
	public void onEnable() {
		new EventListener(this);
		saveDefaultConfig();
		mData = DataModel.getInstance();
		mData.setPlugin(this);
		mData.init();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender pSender, Command pCommand,
			String pLabel, String[] pArgs) {

		Player lPlayer = null;
		// Umwandlung Sender -> Player
		if (pSender instanceof Player) {

			lPlayer = (Player) pSender;
		}

		// Startcommand Castle Rush
		if (pCommand.getName().equalsIgnoreCase("castlerush")) {
			if (pArgs.length == 2) {
				if (pArgs[0].equalsIgnoreCase("start")) {
					getServer().dispatchCommand(getServer().getConsoleSender(), "say Start");
					
					World lWorld = getServer().getWorld(pArgs[1]);
					if (lWorld != null) {

						getServer().dispatchCommand(getServer().getConsoleSender(), "say " + pArgs[1]);
						
						int lCastleRushId = mData.addCastleRushGame(lWorld);
						
						List<Player> lPlayers = new ArrayList<Player>();
						lPlayers.addAll(getServer().getOnlinePlayers());

						lWorld.setPVP(false);
						
						for (Player lSinglePlayer : lPlayers) {
							if (lSinglePlayer.getWorld().equals(lWorld)) {
								mData.addPlayerToCastleRush(lCastleRushId, lSinglePlayer);
								CastleRush lCastleRush = mData.getCastleRush(lCastleRushId);
								lSinglePlayer.teleport(lCastleRush.getBase(lCastleRush.getTeam(lSinglePlayer)));
							}
						}
						
//						if (lWorld.getPlayers().size() != 2) {
//							for (Player lSinglePlayer : mData.getCastleRush(lCastleRushId).getPlayers()) {
//								lSinglePlayer.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "[Castle Rush]" + ChatColor.RESET + "" + ChatColor.RED + " Falsche Anzahl von Spielern, Teleport zurück in die Lobby!");
//								lSinglePlayer.teleport(mData.getLobbySpawn());
//							}
//							return false;
//						}
						
						int lSchedulerId = keepPlayers(lCastleRushId);
						
						List<Player> lCastleRushPlayers = mData.getCastleRush(lCastleRushId).getPlayers();
/*						
						Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(15, lCastleRushPlayers, false), 20*60*0);
						Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(10, lCastleRushPlayers, false), 20*60*5);
						Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(5, lCastleRushPlayers, false), 20*60*10);
						for (int i = 4; i >= 1; i--) {
							Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(i, lCastleRushPlayers, false), 20*60*10 + 20*60*(5-i));
						}
						Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(30, lCastleRushPlayers, true), 20*60*14 + 20*30);
						Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(15, lCastleRushPlayers, false), 20*60*14 + 20*45);
						for (int i = 10; i >= 1; i--) {
							Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CountdownThread(i, lCastleRushPlayers, false), 20*60*14 + 20*(60-i));
						}
						*///20*60*15
						Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new CastleRushStart(lCastleRushId, lSchedulerId), 20*15);
					}
				}
			}
		}

		// Testcommand
		if (pCommand.getName().equalsIgnoreCase("test")) {
			String lVersion = getDescription().getVersion();
			String lAutor = getDescription().getAuthors().get(0);
			if (lPlayer != null) {

				lPlayer.sendMessage(ChatColor.GREEN + "Hallo " + ChatColor.RED
						+ lPlayer.getDisplayName() + ChatColor.GREEN
						+ ", dein Testbefehl war erfolgreich.");
				lPlayer.sendMessage(ChatColor.RED
						+ "Du spielst auf der Plugin Version " + lVersion + ".");
				lPlayer.sendMessage(ChatColor.RED
						+ "Das Plugin wurde erstellt von " + lAutor + ".");
				return true;
			} else {
				pSender.sendMessage("Du bist aber kein Spieler!");
				return true;
			}
		}

		// Kill Befehl
		if (pCommand.getName().equalsIgnoreCase("kill")) {

			if (lPlayer != null) {
				if (lPlayer.hasPermission("plugin.kill")) {
					if (pArgs.length != 1) {
						return false;
					}
					Player lTargetPlayer = getServer().getPlayer(pArgs[0]);
					lTargetPlayer.setHealth(0);
				}
			}
		}

		return false;
	}
	
	
	// --------------------------------------------
	//                   Methoden
	// --------------------------------------------
	
	
	private int keepPlayers(int pGameId) {
		
		int lSchedulerId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new CastleRushThread(pGameId), 20 / 2, 20 / 2);
		
		return lSchedulerId;
	}
}
