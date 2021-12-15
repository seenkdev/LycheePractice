package com.seenk.practice.listener;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import com.seenk.practice.ffa.DelayerState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.arena.ArenaManager;
import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.ffa.FFAStatus;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.MatchManager;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.QueueManager;
import com.seenk.practice.managers.match.MatchType;
import com.seenk.practice.managers.party.PartyFightType;
import com.seenk.practice.managers.party.PartyManager;
import com.seenk.practice.party.PartyGroup;
import com.seenk.practice.player.IKit;
import com.seenk.practice.state.ProfileState;
import com.seenk.practice.utils.LocationHelper;

import net.md_5.bungee.api.ChatColor;

public class InventoryListener implements Listener {

	@EventHandler
	public void PlayerInventoryInteract(InventoryClickEvent event) {
        ItemStack current = event.getCurrentItem();
        if (current == null) return;
		final Player player = (Player) event.getWhoClicked();
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getProfileState() == ProfileState.LOBBY) {
			event.setCancelled(true);
			if (current.hasItemMeta()) {
				if (event.getClickedInventory().getName().contains("Settings:")) {
					if (event.getCurrentItem().getType() == Material.ITEM_FRAME) {
						if (pm.getSettingsManager().isScoreboard()) {
							player.closeInventory();
							player.sendMessage(ChatColor.DARK_RED + "You've been edited your scoreboard settings to false.");
							pm.getSettingsManager().setScoreboard(false);
						}
						else {
							player.closeInventory();
							player.sendMessage(ChatColor.DARK_RED + "You've been edited your scoreboard settings to true.");
							pm.getSettingsManager().setScoreboard(true);
						}
					}
					if (event.getCurrentItem().getType() == Material.GOLD_SWORD) {
						if (pm.getSettingsManager().isDuel()) {
							player.closeInventory();
							player.sendMessage(ChatColor.DARK_RED + "You've been edited your duel request settings to false.");
							pm.getSettingsManager().setDuel(false);
						}
						else {
							player.closeInventory();
							player.sendMessage(ChatColor.DARK_RED + "You've been edited your duel request settings to true.");
							pm.getSettingsManager().setDuel(true);
						}
					}
					if (event.getCurrentItem().getType() == Material.PAPER) {
						if (pm.getSettingsManager().isChat()) {
							player.closeInventory();
							player.sendMessage(ChatColor.DARK_RED + "You've been edited your chat settings to false.");
							pm.getSettingsManager().setChat(false);
						}
						else {
							player.closeInventory();
							player.sendMessage(ChatColor.DARK_RED + "You've been edited your chat settings to true.");
							pm.getSettingsManager().setChat(true);
						}
					}
				}
				if (event.getClickedInventory().getName().contains("Best Of:")) {
					if (event.getCurrentItem().getType() == Material.GOLDEN_CARROT) {
						Main.getInstance().getInventoryManager().loadDuelLadder();
						player.openInventory(Main.getInstance().getInventoryManager().getDuelInventory());
                        pm.setNumberRound(1);
                        final Player target = pm.getTarget();
                        final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
                        tm.setNumberRound(1);
						MainList.getInstance().boselected.add(player.getUniqueId());
					}
					if (event.getCurrentItem().getType() == Material.APPLE) {
						Main.getInstance().getInventoryManager().loadDuelLadder();
						player.openInventory(Main.getInstance().getInventoryManager().getDuelInventory());
						pm.setNumberRound(3);
                        final Player target = pm.getTarget();
                        final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
                        tm.setNumberRound(3);
						MainList.getInstance().boselected.add(player.getUniqueId());
					}
					if (event.getCurrentItem().getType() == Material.COOKED_FISH) {
						Main.getInstance().getInventoryManager().loadDuelLadder();
						player.openInventory(Main.getInstance().getInventoryManager().getDuelInventory());
						pm.setNumberRound(5);
                        final Player target = pm.getTarget();
                        final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
                        tm.setNumberRound(5);
						MainList.getInstance().boselected.add(player.getUniqueId());
					}
				}
				if (event.getClickedInventory().getName().contains("Duel:")) {
					player.sendMessage(ChatColor.DARK_RED + "You have been sent a duel request to " + ChatColor.WHITE + pm.getTarget().getName() + ChatColor.DARK_RED + " into " + current.getItemMeta().getDisplayName());
					player.closeInventory();
					final PlayerManager tm = PlayerManager.getPlayerManager().get(pm.getTarget().getUniqueId());
					tm.setDuelLadder(current.getItemMeta().getDisplayName());
					Main.getInstance().getInventoryManager().acceptDuelPanel();
					pm.getTarget().openInventory(Main.getInstance().getInventoryManager().getAcceptDuelInventory());
				}
				if (event.getClickedInventory().getName().contains("Rank:")) {
					final PlayerManager tm = PlayerManager.getPlayerManager().get(pm.getTarget().getUniqueId());
					if (event.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
						player.closeInventory();
						tm.setRank("Founder");
						tm.setPrefix("§9");
						tm.setPermissions("manager");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.BLUE + "Founder");
					}
					if (event.getCurrentItem().getType() == Material.PAPER) {
						player.closeInventory();
						tm.setRank("Manager");
						tm.setPrefix("§4");
						tm.setPermissions("manager");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.DARK_RED + "Manager");
					}
					if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
						player.closeInventory();
						tm.setRank("Animator");
						tm.setPrefix("§5");
						tm.setPermissions("animator");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.DARK_PURPLE + "Animator");
					}
					if (event.getCurrentItem().getType() == Material.IRON_BARDING) {
						player.closeInventory();
						tm.setRank("Mod");
						tm.setPrefix("§2");
						tm.setPermissions("moderator");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.DARK_GREEN + "Mod");
					}
					if (event.getCurrentItem().getType() == Material.GRASS) {
						player.closeInventory();
						tm.setRank("Builder");
						tm.setPrefix("§b");
						tm.setPermissions("builder");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.AQUA + "Builder");
					}
					if (event.getCurrentItem().getType() == Material.ITEM_FRAME) {
						player.closeInventory();
						tm.setRank("Media");
						tm.setPrefix("§d");
						tm.setPermissions("media");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.LIGHT_PURPLE + "Media");
					}
					if (event.getCurrentItem().getType() == Material.DIAMOND) {
						player.closeInventory();
						tm.setRank("Customers");
						tm.setPrefix("§6");
						tm.setPermissions("customers");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to " + ChatColor.GOLD + "Customers");
					}
					if (event.getCurrentItem().getType() == Material.LEASH) {
						player.closeInventory();
						tm.setRank("default");
						tm.setPrefix("§a");
						tm.setPermissions("default");
						player.sendMessage(ChatColor.DARK_GREEN + "Request accepted.");
						pm.getTarget().sendMessage(ChatColor.GREEN + "Rank as been set to default");
					}
				}
				if (event.getClickedInventory().getName().contains("Manage:")) {
					if (event.getCurrentItem().getType() == Material.IRON_BARDING) {
						final PlayerManager tm = PlayerManager.getPlayerManager().get(pm.getTarget().getUniqueId());
						tm.setBan("yes");
						player.closeInventory();
						pm.getTarget().kickPlayer(ChatColor.DARK_RED + "Xenki" + ChatColor.GRAY + "-" + ChatColor.WHITE + "PvP" + ChatColor.GRAY + "» \n\n" + ChatColor.DARK_RED + "Your account got suspended!");
						player.sendMessage(ChatColor.DARK_RED + "This player as been banned!");
					}
					if (event.getClickedInventory().getName().contains("Manage:")) {
						if (event.getCurrentItem().getType() == Material.PAPER) {
							final PlayerManager tm = PlayerManager.getPlayerManager().get(pm.getTarget().getUniqueId());
							tm.setMute("yes");
							player.closeInventory();
							player.sendMessage(ChatColor.GREEN + "You have muted " + ChatColor.RED + pm.getTarget().getName());
							pm.getTarget().sendMessage(ChatColor.DARK_RED + "You have been muted");
						}
					}
					if (event.getCurrentItem().getType() == Material.SKULL_ITEM) {
						Main.getInstance().getInventoryManager().loadRankPanel();
						player.openInventory(Main.getInstance().getInventoryManager().getRankInventory());
					}
				}
				if (event.getClickedInventory().getName().contains("Request:")) {
					if (event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
						player.closeInventory();
						MainList.getInstance().boselected.add(player.getUniqueId());
						MainList.getInstance().answerduelselected.add(player.getUniqueId());
    	            	MainList.getInstance().duelRequest.remove(player.getUniqueId(), pm.getTarget().getUniqueId());
						new MatchManager(MatchType.UNRANKED, true, pm.getNumberRound(),pm.getTarget().getUniqueId(), player.getUniqueId(), pm.getDuelLadder());
						pm.setTarget(null);
    	            	return;
					}
					if (event.getCurrentItem().getType() == Material.INK_SACK && event.getCurrentItem().getItemMeta().getDisplayName() == ChatColor.DARK_RED + "Deny Duel Request"){
						player.closeInventory();
						pm.setNumberRound(1);
						pm.getTarget().sendMessage(ChatColor.DARK_RED + "Your duel request was deny!");
						pm.setTarget(null);
						MainList.getInstance().answerduelselected.add(player.getUniqueId());
    	            	MainList.getInstance().duelRequest.remove(player.getUniqueId(), pm.getTarget().getUniqueId());
    	            	return;
					}
				}
	        	if (event.getClickedInventory().getName().contains("Unranked Queue:")) {
	        		final int arena = ArenaManager.getAll().size();
	        		if (arena == 0) {
	        			player.closeInventory();
	        			player.sendMessage(ChatColor.RED + "» Sorry but no arena is set. Please set them and retry!");
	        			return;
	        		}
	                player.closeInventory();
	                QueueManager fightManager = Main.getInstance().getQueueManager();
                    MainLadders ladder = MainLadders.getLadder(current.getItemMeta().getDisplayName());
	                if (fightManager.getUnrankedQueue().containsValue(fightManager.getNormalQueuePlayer()) && fightManager.getUnrankedQueue().containsKey(ladder.displayName())) {
	                	for (Entry<String, UUID> uuid : fightManager.getUnrankedQueue().entrySet()) {
	                		ladder.setQueueSize(ladder.queueSize() - 1);
	                		ladder.setFightSize(ladder.fightSize() + 2);
	                		new MatchManager(MatchType.UNRANKED, false, 1, uuid.getValue(), player.getUniqueId(), current.getItemMeta().getDisplayName());
	                		fightManager.setNormalQueuePlayer(null);
	                	}
	                	return;
	                }
	                else {
	                	fightManager.getUnrankedQueue().put(current.getItemMeta().getDisplayName(), player.getUniqueId());
	                    fightManager.setNormalQueuePlayer(player.getUniqueId());
	                    fightManager.setNameLadder(ladder.displayName());
	                    ladder.setQueueSize(ladder.queueSize() + 1);
	                    pm.setProfileState(ProfileState.QUEUE);
	                    Main.getInstance().getItemManager().queueItems(player);
	                    player.sendMessage(ChatColor.DARK_RED + "You have been join the " + ChatColor.WHITE + "Unranked Queue" + ChatColor.DARK_RED + ".");
	                }
	            }
            	if (event.getClickedInventory().getName().contains("Ranked Queue:")) {
            		final int arena = ArenaManager.getAll().size();
            		if (arena == 0) {
            			player.closeInventory();
            			player.sendMessage(ChatColor.RED + "» Sorry but no arenas was set. Please set them and retry!");
            			return;
            		}
            		QueueManager fightManager = Main.getInstance().getQueueManager();
            		 MainLadders ladder = MainLadders.getLadder(current.getItemMeta().getDisplayName());
            		 final PlayerManager qm = PlayerManager.getPlayerManager().get(fightManager.getClasseQueuePlayer());
            		 if (ladder.queueSize() == 1) {
						 if (fightManager.getRankedQueuePlayer().containsValue(qm) && fightManager.getRankedQueuePlayer().containsKey(ladder.displayName())) {
							 for (Entry<String, PlayerManager> uuid : fightManager.getRankedQueuePlayer().entrySet()) {
								 ladder.setQueueSize(ladder.queueSize() - 1);
								 ladder.setFightSize(ladder.fightSize() + 2);
								 new MatchManager(MatchType.RANKED, false ,1,fightManager.getClasseQueuePlayer(), player.getUniqueId(), current.getItemMeta().getDisplayName());
								 fightManager.setClasseQueuePlayer(null);
							 }
							 return;
						 }
					 }
            		else {
                        player.closeInventory();
                        ladder.setQueueSize(ladder.queueSize() + 1);
                        Main.getInstance().getQueueManager().getRanked().add(pm);
                        Main.getInstance().getQueueManager().getRankedQueuePlayer().put(current.getItemMeta().getDisplayName(), pm);
                        Main.getInstance().getQueueManager().setClasseQueuePlayer(player.getUniqueId());
                        pm.setProfileState(ProfileState.QUEUE);
                        Main.getInstance().getItemManager().queueItems(player);
                        player.sendMessage(ChatColor.RED + "You were added to the" + ChatColor.DARK_RED + " Ranked " + ChatColor.stripColor(current.getItemMeta().getDisplayName()) + ChatColor.RED + " queue.");
                        pm.getRanked().start(pm, player);
            		}

                }
				if (event.getClickedInventory().getName().contains("Options:")) {
					if (event.getCurrentItem().getType() == Material.ANVIL) {
						Main.getInstance().getInventoryManager().loadPartyInvite();
						player.openInventory(Main.getInstance().getInventoryManager().getPartyInventory());
					}
					if (event.getCurrentItem().getType() == Material.NAME_TAG) {
						Main.getInstance().getInventoryManager().loadSettings();
						player.openInventory(Main.getInstance().getInventoryManager().getSettingsInventory());
					}
					if (event.getCurrentItem().getType() == Material.EMERALD) {
						player.openInventory(Main.getInstance().leaderboardInventory);
					}
				}
				if (event.getClickedInventory().getName().contains("Party:")) {
					if (event.getCurrentItem().getType() == Material.ENCHANTMENT_TABLE) {
						new PartyManager(pm);
						player.closeInventory();
					}
					if (event.getCurrentItem().getType() == Material.NETHER_STAR) {
						Main.getInstance().getInventoryManager().loadOtherParty();
						player.openInventory(Main.getInstance().getInventoryManager().getOtherPartyInventory());
					}
				}
				if (event.getClickedInventory().getName().contains("Other Party List:")) {
					player.closeInventory();
					final Player target = Bukkit.getPlayer(current.getItemMeta().getDisplayName());;
					final PlayerManager tm = PlayerManager.getPlayerManager().get(target.getUniqueId());
					tm.getPartyManager().sendAskToJoinParty(player, target);
				}
				if (event.getClickedInventory().getName().contains("Arena:")) {
					if (event.getCurrentItem().getType() == Material.WOOL && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED.toString() + "First Location")) {
						player.closeInventory();
						if (pm.isCreateArena() != true) {
							player.sendMessage(ChatColor.DARK_RED + "Please create arena before wanting to put a location");
							return;
						}
						ArenaManager.getArena(pm.getArenaNameCreate()).setLoc1(player.getLocation());
						player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "1st" + ChatColor.DARK_RED + " location.");
						player.sendMessage(ChatColor.RED + "Please set the second and save.");
					}
					if (event.getCurrentItem().getType() == Material.WOOL && event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.BLUE.toString() + "Second Location")) {
						player.closeInventory();
						if (pm.isCreateArena() != true) {
							player.sendMessage(ChatColor.DARK_RED + "Please create arena before wanting to put a location");
							return;
						}
						ArenaManager.getArena(pm.getArenaNameCreate()).setLoc2(player.getLocation());
						player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "2snd" + ChatColor.DARK_RED + " location.");
						pm.setCreateArena(false);
						pm.setArenaNameCreate("");
					}
					if (event.getCurrentItem().getType() == Material.ARROW) {
						Main.getInstance().getInventoryManager().arenaTypeConfig();
						player.openInventory(Main.getInstance().getInventoryManager().getArenaTypeConfigInventory());
						player.updateInventory();
					}
					if (event.getCurrentItem().getType() == Material.PAPER) {
						player.closeInventory();
						try {
							Main.getInstance().getArenaConfig().save("arenas.yml");
						} catch (IOException e) {
							e.printStackTrace();
						};
						player.sendMessage(ChatColor.GREEN + "All arenas.yml data was saved.");
					}
				}
				if (event.getClickedInventory().getName().contains("Location:")) {
					if (current.getType() == Material.FEATHER) {
						player.closeInventory();
						if (LocationHelper.getLocationHelper("spawn") != null) {
							LocationHelper.getLocationHelper("spawn").setLocation(player.getLocation());
							player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "Spawn" + ChatColor.DARK_RED + " location.");
							return;
						}
						else {
							player.sendMessage(ChatColor.RED + "The spawn location already exist!");
						}
					}
					if (current.getType() == Material.IRON_BARDING) {
						player.closeInventory();
						if (LocationHelper.getLocationHelper("checking") != null) {
							LocationHelper.getLocationHelper("checking").setLocation(player.getLocation());
							player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "Checking" + ChatColor.DARK_RED + " location.");
							return;
						}
						else {
							player.sendMessage(ChatColor.RED + "The checking location already exist!");
						}
					}
					if (current.getType() == Material.DIAMOND_AXE) {
						Main.getInstance().getInventoryManager().locationFFAConfigPanel(player);
						player.openInventory(Main.getInstance().getInventoryManager().getLocationFFAConfigInventory());
					}
				}
				if (event.getClickedInventory().getName().contains("Location FFA:")) {
					if (current.getType() == Material.DIAMOND_SWORD) {
						player.closeInventory();
						if (LocationHelper.getLocationHelper("ffa-1") != null) {
							LocationHelper.getLocationHelper("ffa-1").setLocation(player.getLocation());
							player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "FFA First" + ChatColor.DARK_RED + " location.");
							return;
						}
						else {
							player.sendMessage(ChatColor.RED + "The ffa-1 location already exist!");
						}
					}
					if (current.getType() == Material.GOLD_SWORD) {
						player.closeInventory();
						if (LocationHelper.getLocationHelper("ffa-2") != null) {
							LocationHelper.getLocationHelper("ffa-2").setLocation(player.getLocation());
							player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "FFA Second" + ChatColor.DARK_RED + " location.");
							return;
						}
						else {
							player.sendMessage(ChatColor.RED + "The ffa-2 location already exist!");
						}
					}
					if (current.getType() == Material.STONE_SWORD) {
						player.closeInventory();
						if (LocationHelper.getLocationHelper("ffa-3") != null) {
							LocationHelper.getLocationHelper("ffa-3").setLocation(player.getLocation());
							player.sendMessage(ChatColor.DARK_RED + "You have been set the " + ChatColor.WHITE + "FFA Third" + ChatColor.DARK_RED + " location.");
							return;
						}
						else {
							player.sendMessage(ChatColor.RED + "The ffa-3 location already exist!");
						}
					}
				}
				if (event.getClickedInventory().getName().contains("Arena Type:")) {
					if (event.getCurrentItem().getType() == Material.POTION) {
						pm.setArenaType(ArenaType.NORMAL);
						player.closeInventory();
						pm.setCreateArena(true);
						pm.setCreateArenaChat(true);
						player.sendMessage(ChatColor.GREEN + "Enter the name of the map in the chat please.");
					}
					if (event.getCurrentItem().getType() == Material.LEASH) {
						pm.setArenaType(ArenaType.SUMO);
						player.closeInventory();
						pm.setCreateArena(true);
						pm.setCreateArenaChat(true);
						player.sendMessage(ChatColor.GREEN + "Enter the name of the map in the chat please.");
					}
				}	
				if (event.getClickedInventory().getName().contains("Play to FFA:")) {
					pm.reset(player, GameMode.SURVIVAL);
                	pm.setProfileState(ProfileState.FFA);
                	pm.setFfaStatus(FFAStatus.FREE);
                	MainList.getInstance().getInFFA().add(player.getUniqueId());
                	for (Player players : Bukkit.getOnlinePlayers()) {
                		player.hidePlayer(players);
                		players.hidePlayer(player);
                	}
                    final IKit kit = (IKit) MainLadders.getLadder(current.getItemMeta().getDisplayName());
                	pm.sendKit(kit);
                	Collections.shuffle(Main.getInstance().spawnFFA);
                	for (int i = 0; i<Main.getInstance().spawnFFA.size();i++) {
                		final Location spawn = Main.getInstance().spawnFFA.get(i);
                		player.teleport(spawn);
                	}
                	player.sendMessage(ChatColor.RED + "You have been teleported to the ffa.");
                  	final int in = MainList.getInstance().getInFFA().size();
                	DelayerState delayerState = in <= 3 ? DelayerState.LOW : in <= 4 ? DelayerState.NORMAL: DelayerState.HIGHER;
            		player.sendMessage(ChatColor.DARK_RED + "Delayer level: " + ChatColor.WHITE + delayerState.toString());
                	if (delayerState == DelayerState.LOW) {
                    	player.sendMessage(ChatColor.WHITE + "You can attack another player in 2seconds");
            	        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            	            @Override
            	            public void run() {
            	                if (player.getUniqueId() != null && PlayerManager.getPlayerManager().get(player.getUniqueId()).getProfileState() == ProfileState.FFA) {
            	                	for (Player players : Bukkit.getOnlinePlayers()) {
            	                		player.showPlayer(players);
            	                		players.showPlayer(player);
            	                	}
            	                	player.sendMessage(ChatColor.DARK_RED + "Good " + ChatColor.WHITE + "Luck!");
            	                	pm.setFfaStatus(FFAStatus.FREE);
            	                }
            	            }
            	        }, 40L);
                    	return;
                	}
                	if (delayerState == DelayerState.NORMAL) {
                    	player.sendMessage(ChatColor.WHITE + "You can attack another player in 4seconds");
            	        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            	            @Override
            	            public void run() {
            	                if (player.getUniqueId() != null && PlayerManager.getPlayerManager().get(player.getUniqueId()).getProfileState() == ProfileState.FFA) {
            	                	for (Player players : Bukkit.getOnlinePlayers()) {
            	                		player.showPlayer(players);
            	                		players.showPlayer(player);
            	                	}
            	                	player.sendMessage(ChatColor.DARK_RED + "Good " + ChatColor.WHITE + "Luck!");
            	                	pm.setFfaStatus(FFAStatus.FREE);
            	                }
            	            }
            	        }, 80L);
                    	return;
                	}
                	if (delayerState == DelayerState.HIGHER) {
                    	player.sendMessage(ChatColor.WHITE + "You can attack another player in 6seconds");
            	        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            	            @Override
            	            public void run() {
            	                if (player.getUniqueId() != null && PlayerManager.getPlayerManager().get(player.getUniqueId()).getProfileState() == ProfileState.FFA) {
            	                	for (Player players : Bukkit.getOnlinePlayers()) {
            	                		player.showPlayer(players);
            	                		players.showPlayer(player);
            	                	}
            	                	player.sendMessage(ChatColor.DARK_RED + "Good " + ChatColor.WHITE + "Luck!");
            	                	pm.setFfaStatus(FFAStatus.FREE);
            	                }
            	            }
            	        }, 100L);
                    	return;
                	}
				}
			}
		}
		if (pm.getProfileState() == ProfileState.MOD) {
			event.setCancelled(true);
		}
		if (pm.getProfileState() == ProfileState.PARTY) {
			event.setCancelled(true);
			if (event.getClickedInventory().getName().contains("Party Events:")) {
				if (event.getCurrentItem().getType() == Material.SHEARS) {
					Main.getInstance().getInventoryManager().loadPartyFight();
					player.openInventory(Main.getInstance().getInventoryManager().getPartyEventSplit());
				}
				if (event.getCurrentItem().getType() == Material.COMPASS) {
					Main.getInstance().getInventoryManager().loadPartyFight();
					player.openInventory(Main.getInstance().getInventoryManager().getPartyEventFFA());
				}
			}
            if (event.getClickedInventory().getName().contains("Party FFA:")) {
            	if (pm.getPartyManager().getAllMembers().size() > 2) {
            		pm.getPartyManager().setFightType(PartyFightType.FFA);
                	for (int i = 0; i < pm.getPartyManager().getAllMembers().size();i++) {
                    	List<UUID> shuffle = Lists.newArrayList(pm.getPartyManager().getAllMembers());
                        Collections.shuffle(shuffle);
                        
                        List<UUID> firstTeam = shuffle.subList(0, (int)(shuffle.size() / 2.0));
                        List<UUID> secondTeam = shuffle.subList((int)(shuffle.size() / 2.0), shuffle.size());
                        final UUID firstTeamUUID = firstTeam.get(i);
                        final UUID secondTeamUUID = secondTeam.get(i);
                        new MatchManager(MatchType.PARTY_EVENT,false, 1, firstTeamUUID, secondTeamUUID, current.getItemMeta().getDisplayName());	
                	}	
            	}
            	else {
            		pm.getPartyManager().setFightType(PartyFightType.FFA);
                	for (int i = 0; i < pm.getPartyManager().getAllMembers().size();i++) {
                		final UUID member = pm.getPartyManager().getAllMembers().get(i);
                		new MatchManager(MatchType.PARTY_EVENT,false, 1,player.getUniqueId(), member, current.getItemMeta().getDisplayName());
                	}
            	}
            }
            if (event.getClickedInventory().getName().contains("Party Split:")) {
            	if (pm.getPartyManager().getAllMembers().size() > 2) {
            		pm.getPartyManager().setFightType(PartyFightType.SPLIT);
                	for (int i = 0; i < pm.getPartyManager().getAllMembers().size();i++) {
                    	List<UUID> shuffle = Lists.newArrayList(pm.getPartyManager().getAllMembers());
                        Collections.shuffle(shuffle);
                        
                        List<UUID> firstTeam = shuffle.subList(0, (int)(shuffle.size() / 2.0));
                        List<UUID> secondTeam = shuffle.subList((int)(shuffle.size() / 2.0), shuffle.size());
                        final UUID firstTeamUUID = firstTeam.get(i);
                        final UUID secondTeamUUID = secondTeam.get(i);
                        final PlayerManager fistTeamAlivedManager = PlayerManager.getPlayerManager().get(firstTeamUUID);
                        final PlayerManager secondTeamAlivedManager = PlayerManager.getPlayerManager().get(secondTeamUUID);
                        fistTeamAlivedManager.setPartyGroup(PartyGroup.RED);
                        secondTeamAlivedManager.setPartyGroup(PartyGroup.BLUE);
                        new MatchManager(MatchType.PARTY_EVENT,false, 1, firstTeamUUID, secondTeamUUID, current.getItemMeta().getDisplayName());	
                	}	
            	}
            	else {
                	for (int i = 0; i < pm.getPartyManager().getAllMembers().size();i++) {
                		pm.getPartyManager().setFightType(PartyFightType.SPLIT);
                		pm.getPartyManager().getAllMembers().remove(player.getUniqueId());
                		final UUID member = pm.getPartyManager().getAllMembers().get(i);
                		final PlayerManager memberM = PlayerManager.getPlayerManager().get(member);
                        pm.setPartyGroup(PartyGroup.RED);
                        memberM.setPartyGroup(PartyGroup.BLUE);
                		new MatchManager(MatchType.PARTY_EVENT,false, 1,player.getUniqueId(), member, current.getItemMeta().getDisplayName());
                	}
            	}
            }
		}
	}

	@EventHandler
	public void CloseInventory(InventoryCloseEvent event){
		if (event.getInventory().getName().contains("Duel:")){
			final Player player = (Player) event.getPlayer();
			if (MainList.getInstance().boselected.contains(player.getUniqueId())){
                final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
                pm.setNumberRound(1);
				MainList.getInstance().boselected.remove(player.getUniqueId());
				MainList.getInstance().boselected.remove(pm.getTarget().getUniqueId());
				final PlayerManager tm = PlayerManager.getPlayerManager().get(pm.getTarget().getUniqueId());
				tm.setNumberRound(1);
			}
		}
		if (event.getInventory().getName().contains("Request:")){
			final Player player = (Player) event.getPlayer();
			if (!MainList.getInstance().answerduelselected.contains(player.getUniqueId())){
				if (MainList.getInstance().boselected.contains(player.getUniqueId())){
	                final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
	                pm.setNumberRound(1);
					MainList.getInstance().boselected.remove(player.getUniqueId());
					MainList.getInstance().boselected.remove(pm.getTarget().getUniqueId());
					final PlayerManager tm = PlayerManager.getPlayerManager().get(pm.getTarget().getUniqueId());
					tm.setNumberRound(1);
				}
				MainList.getInstance().duelRequest.remove(player.getUniqueId(), PlayerManager.getPlayerManager().get(player.getUniqueId()).getTarget().getUniqueId());
			}
		}
	}
}