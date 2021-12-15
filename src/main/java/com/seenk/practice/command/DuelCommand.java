package com.seenk.practice.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

public class DuelCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.out.println("Error - 0");
			return false;
		}
		if (args.length < 0 || args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Please include player /duel <player>");
			return true;
		}
		final Player player = (Player)sender;
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		final Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(ChatColor.DARK_RED + "This target is offline!");
			return false;
		}
		if (target == player) {
			player.sendMessage(ChatColor.DARK_RED + "You can't duel yourself!");
			return false;
		}
		pm.setTarget(target);
		final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
		tm.setTarget(player);
		if (tm.getProfileState() != ProfileState.LOBBY) {
			player.sendMessage(ChatColor.DARK_RED + "This player is in fight!");
			return false;
		}
		if (tm.getSettingsManager().isDuel() != false) {
			tm.setTarget(player);
			MainList.getInstance().getDuelRequest().put(player.getUniqueId(), target.getUniqueId());
			Main.getInstance().getInventoryManager().loadBestOf();
			player.openInventory(Main.getInstance().getInventoryManager().getBoInventory());
		}
		else {
			player.sendMessage(ChatColor.DARK_RED + "Sorry but this player have disabled his duel request!");
		}
		return false;
	}

}
