package com.seenk.practice.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.ffa.FFAStatus;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

public class SpawnCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		final Player player = (Player)sender;
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getFfaStatus() == FFAStatus.FIGHT) {
			player.sendMessage("You aren in fight you can't execute this command.");
			return false;
		}
		if (pm.getProfileState() != ProfileState.FFA) {
			player.sendMessage(ChatColor.RED + "Sorry but this command is just allowed for FFA!");
			return false;
		}
		else {
			player.sendMessage("");
			player.sendMessage(ChatColor.RED + "Your request was sended!");
			player.sendMessage("");
			player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------------------------" + ChatColor.GRAY + "«" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "--");
			player.sendMessage(ChatColor.GRAY + "► " + ChatColor.DARK_RED + "DISCLAIMER:");
			player.sendMessage(ChatColor.WHITE + "This request take 15seconds to be accepted!");
			player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "----------------------------" + ChatColor.GRAY + "«" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "--");
			player.sendMessage("");
	        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	                if (player.getUniqueId() != null && PlayerManager.getPlayerManager().get(player.getUniqueId()).getProfileState() == ProfileState.FFA) {
	                    player.sendMessage(ChatColor.RED + "Your request was accepted!");
	                    pm.reset(player, GameMode.SURVIVAL);
	                    pm.setProfileState(ProfileState.LOBBY);
	                    pm.teleport(player, Main.getInstance().spawn);
	                    Main.getInstance().getItemManager().spawnItems(player);
	                    MainList.getInstance().inFFA.remove(player.getUniqueId());
	                }
	            }
	        }, 300L);
		}
		return false;
	}

}
