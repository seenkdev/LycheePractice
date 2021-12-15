package com.seenk.practice.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffectType;

import com.seenk.practice.Main;
import com.seenk.practice.arena.ArenaManager;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatListener implements Listener {
	
	@EventHandler
	public void aSyncChat(AsyncPlayerChatEvent event) {
		final PlayerManager pm = PlayerManager.getPlayerManager().get(event.getPlayer().getUniqueId());
		final Player player = event.getPlayer();
		if (pm.isPartyInvite() == true) {
			final Player target = Bukkit.getPlayer(event.getMessage());
			if (target == null) {
				player.sendMessage(ChatColor.DARK_RED + "Sorry but this player is not online or doesn't exist!");
				pm.setPartyInvite(false);
				event.setCancelled(true);
				return;
			}
			if (target == player) {
				player.sendMessage(ChatColor.DARK_RED + "You cannot invite yourself!");
				pm.setPartyInvite(false);
				event.setCancelled(true);
				return;
			}
			player.sendMessage(ChatColor.DARK_RED + "You've been invited: " + ChatColor.WHITE + target.getName() + ChatColor.DARK_RED + " to your party!");
			final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
			tm.setAskedToJoinParty(true);
			pm.setPartyInvite(false);
			TextComponent verifMsg = new TextComponent(ChatColor.WHITE.toString() + ChatColor.BOLD + player.getName() + ChatColor.DARK_RED + " have invited you to him party!");
			verifMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click here for join the party!").create()));
			verifMsg.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/jp " + player.getName()));
			target.spigot().sendMessage(verifMsg);
			event.setCancelled(true);
			return;
		}
		if (pm.isCheckingRobot() == false) {
			if (event.getMessage().contains("yes")) {
				if (pm.getBan().contains("yes")) {
					player.sendMessage(ChatColor.RED + "You are banned for reason: " + ChatColor.WHITE + "Identified to bot try to connect is higher");
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						public void run() {
							player.kickPlayer(ChatColor.DARK_RED + "Xenki " + ChatColor.GRAY + "-" + ChatColor.WHITE + " PvP" + ChatColor.GRAY + " �\n"
									+ "\n" +
									ChatColor.RED + "Your account is suspended forever because your try to connect is higher!\n" +
									ChatColor.RED + "If you think your ban is injustified made a appeal to staff\n" +
									"\n" +
									"\n\n" + ChatColor.DARK_RED + "If you want to appeal you can here: discord.xenki.red");
				          		}
							}, 20L);
					event.setCancelled(true);
					return;
				}
				pm.setCheckingRobot(true);
				pm.setTryToConnect(0);
				pm.setProfileState(ProfileState.LOBBY);
        		player.sendMessage(ChatColor.GREEN.toString() + ChatColor.ITALIC + "Your account as been verified.");
        		player.sendMessage(ChatColor.GREEN.toString() + ChatColor.ITALIC + "All of your player data as been loaded.");
        		player.sendMessage(" ");
        		player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "----------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
        		player.sendMessage(ChatColor.GRAY + " » " + ChatColor.DARK_RED + player.getName() + ChatColor.WHITE + " welcome on Xenki!");
        		player.sendMessage(ChatColor.DARK_RED + "Online: " + ChatColor.GREEN + "Practice" + ChatColor.GRAY + ", " + ChatColor.RED + "Tower of God.");
        		player.sendMessage(ChatColor.DARK_RED + "Actual Rank: " + pm.getPrefix() + pm.getRank());
        		player.sendMessage(ChatColor.DARK_RED + "Total Player: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size() + ChatColor.GRAY + "/" + ChatColor.WHITE + Bukkit.getMaxPlayers());
        		player.sendMessage(ChatColor.DARK_RED + "Status: " + ChatColor.WHITE + pm.getProfileState().toString());
        		player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.GRAY + "»" + ChatColor.STRIKETHROUGH + "----------------------" + ChatColor.GRAY + "«" + ChatColor.STRIKETHROUGH + "--");
        		player.getInventory().clear();
				Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
					public void run() {
				        player.setWalkSpeed(0.2f);
				        player.setFlySpeed(0.25f);
				        player.setFoodLevel(20);
				        player.setSprinting(true);
				        player.removePotionEffect(PotionEffectType.JUMP);
						for (Player players : Bukkit.getOnlinePlayers()) {
							players.showPlayer(player);
							player.showPlayer(players);
						}
			          		}
						});
        		pm.sendToSpawn(player);
        		event.setCancelled(true);
			}
			else {
				if (pm.getTryToConnect() < 3) {
					pm.setTryToConnect(pm.getTryToConnect() + 1);
				}
				if (pm.getTryToConnect() >= 3) {
					pm.setBan("yes");;
				}
				Bukkit.getScheduler().runTask(Main.getInstance(), new Runnable() {
					public void run() {
						player.kickPlayer(ChatColor.DARK_RED + "Xenki " + ChatColor.GRAY + "-" + ChatColor.WHITE + " PvP" + ChatColor.GRAY + " �\n"
								+ "\n" +
								ChatColor.RED + "You did not accept the account verification, after three refusals your account will be banned!\n" +
								"\n" +
								"\n\n" + ChatColor.DARK_RED + "If you have need any help, come here -> discord.xenki.red");
			          		}
						});
    			event.setCancelled(true);
			}
		}
		if (pm.isCreateArenaChat() == true) {
			event.setCancelled(true);
			new ArenaManager(event.getMessage(), pm.getArenaType());
			pm.setArenaNameCreate(event.getMessage());
			player.sendMessage(ChatColor.RED + "You have been created " + ChatColor.WHITE + event.getMessage() + ChatColor.RED + " arena.");
			player.sendMessage(ChatColor.YELLOW + "ArenaType -> " + pm.getArenaType().toString());
			pm.setCreateArenaChat(false);
			return;
		}
		if (pm.isAskedToJoinParty() == true) {
			if (event.getMessage().contains("yes")) {
				event.setCancelled(true);
				pm.getPartyManager().addMember(pm.getTarget().getUniqueId());
				player.sendMessage(ChatColor.DARK_RED + "You have accepted his request!");
				pm.setAskedToJoinParty(false);
			}
			else {
				player.sendMessage(ChatColor.DARK_RED + "You have deny his request!");
				pm.setAskedToJoinParty(false);
			}
		}
		if (pm.getMute().contains("yes")) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "You are muted !");
		}
		if (pm.getRank().contains("default")) {
			event.setFormat(ChatColor.GREEN + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s");
		}
		else {
			event.setFormat(ChatColor.GRAY + "[" + pm.getPrefix() + pm.getRank() + ChatColor.GRAY +"] " + pm.getPrefix()  + "%1$s" + ChatColor.GRAY + ": " + ChatColor.WHITE + "%2$s");
		}
	}

}
