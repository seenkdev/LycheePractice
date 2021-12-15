package com.seenk.practice.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.seenk.practice.Main;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

public class LocationCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.out.println("Error code 0 see the list error");
			return true;
		}
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
        if (!pm.getPermissions().contains("manager")) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have required permissions!");
            return false;
        }
		if (pm.getProfileState() != ProfileState.LOBBY) {
			player.sendMessage(ChatColor.RED + "You cannot perform your command in current state!");
			return true;
		}
		Main.getInstance().getInventoryManager().locationConfigPanel(player);
		player.openInventory(Main.getInstance().getInventoryManager().getLocationConfigInventory());
		return true;
	}

}
