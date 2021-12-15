package com.seenk.practice.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.seenk.practice.Main;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

public class ArenaCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.out.println("Error - 0");
			return false;
		}
		final Player player = (Player) sender;
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
        if (!pm.getPermissions().contains("manager")) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have required permissions!");
            return false;
        }
		if (pm.getProfileState() != ProfileState.LOBBY) {
			player.sendMessage(ChatColor.DARK_RED + "Your state isnt alright for this commands!");
			return false;
		}
		Main.getInstance().getInventoryManager().arenaConfigPanel(player);
		player.openInventory(Main.getInstance().getInventoryManager().getArenaConfigInventory());
		return false;
	}

}
