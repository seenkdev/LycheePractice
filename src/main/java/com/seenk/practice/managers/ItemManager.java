package com.seenk.practice.managers;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.seenk.practice.utils.ItemBuilder;

public class ItemManager {
	
	public void spawnItems(final Player player) {
		player.getInventory().clear();
		player.getInventory().setItem(0, new ItemBuilder(Material.IRON_SWORD).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Unranked" + ChatColor.WHITE + " Queue").toItemStack());
		player.getInventory().setItem(1, new ItemBuilder(Material.DIAMOND_SWORD).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Ranked" + ChatColor.WHITE + " Queue").toItemStack());
		player.getInventory().setItem(5, new ItemBuilder(Material.GOLD_AXE).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Play" + ChatColor.WHITE + " FFA").toItemStack());
		player.getInventory().setItem(8, new ItemBuilder(Material.DAYLIGHT_DETECTOR).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.WHITE + ChatColor.BOLD + "Options").toItemStack());
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getPermissions().contains("mod") || pm.getPermissions().contains("manager")) {
			player.getInventory().setItem(4, new ItemBuilder(Material.ENCHANTED_BOOK).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + ChatColor.BOLD + "Mod" + ChatColor.WHITE + " Mode").toItemStack());
		}
		player.updateInventory();
	}

	public void queueItems(final Player player) {
		player.getInventory().clear();
		player.getInventory().setItem(6, new ItemBuilder(Material.REDSTONE_TORCH_ON).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Leave " + ChatColor.WHITE + "Queue").toItemStack());
		player.updateInventory();
	}

	public void staffItems(final Player player) {
		player.getInventory().clear();
		player.getInventory().setItem(0, new ItemBuilder(Material.ANVIL).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Manage" + ChatColor.WHITE + " Player").toItemStack());
		player.getInventory().setItem(1, new ItemBuilder(Material.SNOW_BALL).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Random" + ChatColor.WHITE + " Teleport").toItemStack());
		player.getInventory().setItem(8, new ItemBuilder(Material.REDSTONE_TORCH_ON).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.WHITE + ChatColor.BOLD + "Leave " + ChatColor.BOLD + "Mod" + ChatColor.WHITE + " Mode").toItemStack());
		player.getInventory().setItem(3, new ItemBuilder(Material.PACKED_ICE).setUnBreakable().setName(ChatColor.RED + "» " + ChatColor.WHITE + "Freeze").toItemStack());
		player.updateInventory();
	}

	public void partyItems(final Player player) {
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getPartyManager().getLeader() == player.getUniqueId()) {
			player.getInventory().clear();
			player.getInventory().setItem(0, new ItemBuilder(Material.GOLD_AXE).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Party " + ChatColor.WHITE + "Events").toItemStack());
			player.getInventory().setItem(1, new ItemBuilder(Material.CHEST).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Party " + ChatColor.WHITE + "Invite").toItemStack());
			player.getInventory().setItem(8, new ItemBuilder(Material.REDSTONE_TORCH_ON).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Party " + ChatColor.WHITE + "Disband").toItemStack());
			player.updateInventory();
		}
		else {
			player.getInventory().clear();
			player.getInventory().setItem(8, new ItemBuilder(Material.REDSTONE_TORCH_ON).setUnBreakable().setName(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "Party " + ChatColor.WHITE + "Leave").toItemStack());
			player.updateInventory();
		}
	}
	
}
