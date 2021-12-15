package com.seenk.practice.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.party.PartyManager;
import com.seenk.practice.state.ProfileState;

public class JoinPartyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getProfileState() != ProfileState.LOBBY) {
			player.sendMessage(ChatColor.DARK_RED + "You cannot perform this command at your state!");
			return false;
		}
		if (args.length > 0) {
			if (pm.isAskedToJoinParty() == true) {
				final Player target = Bukkit.getPlayer(args[0]);
				final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
				final PartyManager party = tm.getPartyManager();
				if (party == null) {
					player.sendMessage(ChatColor.DARK_RED + "Sorry but this party have expired!");
					return false;
				}
				party.addMember(player.getUniqueId());
				pm.setPartyInvite(false);
			}
			else {
				player.sendMessage(ChatColor.DARK_RED + "You don't have invite!");
			}
		}
		else {
			player.sendMessage(ChatColor.DARK_RED + "Please provide a target!");
			player.sendMessage(ChatColor.DARK_RED + "/jp <player>");
		}
		return false;
	}

}
