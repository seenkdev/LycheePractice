package com.seenk.practice.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.seenk.practice.Main;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.state.ProfileState;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class ItemInteractListener implements Listener {

	@EventHandler
	public void PlayerInteractItems(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
        ItemStack current = event.getItem();
        if(current == null) return;
        Action action = event.getAction();
        if(current.hasItemMeta()) {
            if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            	if (pm.getProfileState() == ProfileState.LOBBY) {
            		if (event.getItem().getType() == Material.IRON_SWORD) {
            			Main.getInstance().getInventoryManager().loadUnrankedLadder();
            			player.openInventory(Main.getInstance().getInventoryManager().getUnrankedInventory());
            		}
					if (event.getItem().getType() == Material.ENCHANTED_BOOK) {
						player.getInventory().clear();
						pm.setProfileState(ProfileState.MOD);
						player.setGameMode(GameMode.CREATIVE);
						Main.getInstance().getItemManager().staffItems(player);
						player.sendMessage(ChatColor.DARK_RED + "You have been enabled staff mode");
					}
            		if (event.getItem().getType() == Material.DIAMOND_SWORD) {
            			player.sendMessage(ChatColor.DARK_RED + "Ranked system is in developement, progress: 65%");
            		}
            		if (event.getItem().getType() == Material.GOLD_AXE) {
            			Main.getInstance().getInventoryManager().loadFFALadder();
            			player.openInventory(Main.getInstance().getInventoryManager().getFfaInventory());
            		}
            		if (event.getItem().getType() == Material.DAYLIGHT_DETECTOR) {
            			Main.getInstance().getInventoryManager().loadOptionsInventory();;
            			player.openInventory(Main.getInstance().getInventoryManager().getOptionsInventory());
            		}
            	}
            	if (pm.getProfileState() == ProfileState.MOD){
					if (event.getItem().getType() == Material.SNOW_BALL) {
						event.setCancelled(true);
						final ArrayList<UUID> uuidOnline = new ArrayList<UUID>();
						for (Player players : Bukkit.getOnlinePlayers()){
							uuidOnline.add(players.getUniqueId());
							Collections.shuffle(uuidOnline);
							final UUID playerSolo = uuidOnline.get(1);
							player.teleport(Bukkit.getPlayer(playerSolo).getLocation());
							player.sendMessage(ChatColor.DARK_RED + "You've been teleported to " + ChatColor.WHITE + Bukkit.getPlayer(playerSolo).getName());
						}
					}
					if (event.getItem().getType() == Material.REDSTONE_TORCH_ON) {
						player.getInventory().clear();
						pm.setProfileState(ProfileState.LOBBY);
						player.setGameMode(GameMode.SURVIVAL);
						Main.getInstance().getItemManager().spawnItems(player);
						player.sendMessage(ChatColor.DARK_RED + "You have been disabled staff mode");
					}
				}
            	if (pm.getProfileState() == ProfileState.QUEUE) {
            		if (event.getItem().getType() == Material.REDSTONE_TORCH_ON) {
            			String ladderName = Main.getInstance().getQueueManager().getNameLadder();
            			if (pm.getRanked().active) {
            				pm.getRanked().stop();
            				Main.getInstance().getQueueManager().getRankedQueuePlayer().remove(pm);
            				player.sendMessage(ChatColor.DARK_RED + "You've been left the " + ChatColor.WHITE + "Ranked queue" + ChatColor.DARK_RED + ".");
            			}
            			else {
                			final MainLadders ladder = MainLadders.getLadder(ladderName);
                			ladder.setQueueSize(ladder.queueSize() - 1);
            				player.sendMessage(ChatColor.DARK_RED + "You've been left the " + ChatColor.WHITE + "Unranked queue" + ChatColor.DARK_RED + ".");
            			}
    					Main.getInstance().getQueueManager().setNormalQueuePlayer(null);
            			pm.sendToSpawn(player);
            		}
            	}
            	if (pm.getProfileState() == ProfileState.PARTY) {
            		if (event.getItem().getType() == Material.GOLD_AXE) {
            			if (pm.getPartyManager().getAllMembers().size() < 2) {
            				player.sendMessage(ChatColor.DARK_RED + "You need one player more for access here!");
            				return;
            			}
            			else {
                			Main.getInstance().getInventoryManager().loadPartyEvent();
                			player.openInventory(Main.getInstance().getInventoryManager().getPartyEvent());
            			}
            		}
            		if (event.getItem().getType() == Material.CHEST) {
            			if (pm.isPartyInvite() != true) {
            				pm.setPartyInvite(true);
            				player.sendMessage(ChatColor.DARK_RED + "Please type the nickname of the player in chat!");
            				return;
            			}
            			else {
            				player.sendMessage(ChatColor.DARK_RED + "Sorry but you are in party invite mod please tell player in chat!");
            			}
            		}
            		if (event.getItem().getType() == Material.REDSTONE_TORCH_ON) {
            			if (pm.getPartyManager().getLeader() == player.getUniqueId()) {
            				pm.getPartyManager().leaveParty(player);
            			}
            		}
            	}
            }

        }
	}

}
