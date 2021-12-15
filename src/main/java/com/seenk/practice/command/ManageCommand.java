package com.seenk.practice.command;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.seenk.practice.managers.*;
import org.bukkit.*;
import com.seenk.practice.*;

public class ManageCommand implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error - 0");
            return false;
        }
        final Player player = (Player)sender;
        final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
        if (!pm.getPermissions().contains("manager")) {
            player.sendMessage(ChatColor.DARK_RED + "You don't have required permissions!");
            return false;
        }
        if (args.length < 0 || args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Please include player /manage <player>");
            return true;
        }
        final Player targetOnline = Bukkit.getPlayer(args[0]);
        if (targetOnline == player) {
            player.sendMessage(ChatColor.DARK_RED + "You can't manage yoursel!");
            return false;
        }
        if (targetOnline == null) {
            player.sendMessage(ChatColor.DARK_RED + "This player has disconnected!");
            return false;
        }
        pm.setTarget(targetOnline);
        Main.getInstance().getInventoryManager().loadManagePanel(args[0]);
        player.openInventory(Main.getInstance().getInventoryManager().getManageInventory());
        return false;
    }
}