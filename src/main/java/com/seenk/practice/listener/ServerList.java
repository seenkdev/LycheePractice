package com.seenk.practice.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerList implements Listener {
	
	@EventHandler
	public void ServerPingList(ServerListPingEvent event) {
		if (Bukkit.getServer().hasWhitelist() == true) {
			event.setMaxPlayers(0);
			event.setMotd(ChatColor.DARK_RED + "                      Xenki " + ChatColor.GRAY + "❘" + ChatColor.WHITE + " PvP\n" + ChatColor.GRAY + " » " + ChatColor.WHITE + "Network currently under developement.");
		}
		else {
			event.setMaxPlayers(Bukkit.getServer().getOnlinePlayers().size() + 1);
			event.setMotd(ChatColor.DARK_RED + "                      Xenki " + ChatColor.GRAY + "❘" + ChatColor.WHITE + " PvP\n" + ChatColor.GRAY + " » " + ChatColor.WHITE + "Summon your fighting style.");	
		}
	}

}
