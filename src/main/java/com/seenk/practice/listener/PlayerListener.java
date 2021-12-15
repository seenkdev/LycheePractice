package com.seenk.practice.listener;

import com.seenk.practice.MainList;
import com.seenk.practice.managers.match.MatchType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.seenk.practice.Main;
import com.seenk.practice.managers.MatchManager;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.match.MatchEndCause;
import com.seenk.practice.state.MatchStatus;
import com.seenk.practice.state.ProfileState;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage("");
		final Player player = event.getPlayer();
		for (Player players : Bukkit.getOnlinePlayers()) {
			player.hidePlayer(players);
			players.hidePlayer(player);
		}
		new PlayerManager(player.getUniqueId());
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getPermissions().contains("manager")) {MainList.getInstance().getStaffOnline().add(player.getUniqueId());}
		if (pm.getPermissions().contains("mod")) {MainList.getInstance().getStaffOnline().add(player.getUniqueId());}
		pm.teleport(player, Main.getInstance().getCheckingSpawn());
		pm.reset(player, GameMode.SURVIVAL);
        player.setWalkSpeed(0.0f);
        player.setFlySpeed(0.0f);
        player.setFoodLevel(0);
        player.setSprinting(false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));
		pm.setProfileState(ProfileState.CHECKING);
		if (!pm.isCheckingRobot()) {
			TextComponent verifMsg = new TextComponent(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Please enter yes in the chat to validate your account.");
			verifMsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Say yes in chat!").create()));
			verifMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "yes"));
			player.spigot().sendMessage(verifMsg);
		}
	}
	
	@EventHandler
	public void PlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage("");
		final Player player = event.getPlayer();
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getPermissions().contains("mod") || pm.getPermissions().contains("manager")) {
			MainList.getInstance().getStaffOnline().remove(player.getUniqueId());
		}
		PlayerManager.getPlayerManager().get(player.getUniqueId()).disconnect(player);
	}
	
	
	//------------------------------------------[ Event sur le joueur ]---------------------------------------------//
	
	@EventHandler
	public void PlayerFood(FoodLevelChangeEvent event) {
		final Player player = (Player) event.getEntity();
		final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (pm.getProfileState() == ProfileState.LOBBY || pm.getProfileState() == ProfileState.CHECKING || pm.getProfileState() == ProfileState.QUEUE || pm.getProfileState() == ProfileState.PARTY) {
			event.setCancelled(true);
		}
	}
	
	@SuppressWarnings({ "deprecation" })
	@EventHandler
	public void PlayerConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        PlayerManager playerData = PlayerManager.getPlayerManager().get(player.getUniqueId());
        Material drop = event.getItem().getType();
        if (playerData.getProfileState() == ProfileState.FIGHT || playerData.getProfileState() == ProfileState.PARTY_FIGHT || playerData.getProfileState() == ProfileState.FFA) {
    		if (drop.getId() != 373) return;
        		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(), () -> {
        			player.setItemInHand(new ItemStack(Material.AIR));
        			player.updateInventory();
        		}, 1L);
        	}
        else {
        	event.setCancelled(true);
        }
	}
	
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        PlayerManager playerManager = PlayerManager.getPlayerManager().get(e.getPlayer().getUniqueId());
        if(playerManager.getProfileState() == ProfileState.FIGHT || playerManager.getProfileState() == ProfileState.PARTY_FIGHT || playerManager.getProfileState() == ProfileState.FFA) {
            if(e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD
                    || e.getItemDrop().getItemStack().getType() == Material.IRON_AXE
                    || e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD
                    || e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD){ e.setCancelled(true);
                    return; }
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                public void run() {
                    e.getItemDrop().remove();
                }
            }, 50);
        }
        else{
            e.setCancelled(true);
        }
    }
	
    @EventHandler
    public void onEnder(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerManager pm = PlayerManager.getPlayerManager().get(p.getUniqueId());
        ItemStack current = e.getItem();

        if (current == null) return;
        if (current.getType() == Material.ENDER_PEARL) {
            if(pm.getProfileState() == ProfileState.FIGHT && MatchManager.getMatchManager(p.getUniqueId()) != null && MatchManager.getMatchManager(p.getUniqueId()).getMatchStatus() == MatchStatus.PLAYING || pm.getProfileState() == ProfileState.PARTY_FIGHT && MatchManager.getMatchManager(p.getUniqueId()) != null && MatchManager.getMatchManager(p.getUniqueId()).getMatchStatus() == MatchStatus.PLAYING || pm.getProfileState() == ProfileState.PARTY_FIGHT || pm.getProfileState() == ProfileState.FFA || MainList.getInstance().boselected.contains(p.getUniqueId())) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if(pm.getEnderpearl() == 0) {
                        pm.setEnderpearl(15);
                        p.setLevel(15);
                        new BukkitRunnable() {
                            public void run() {
                                    pm.setEnderpearl(pm.getEnderpearl()-1);
                                    p.setLevel(pm.getEnderpearl());
                                    if(pm.getEnderpearl() == 0)
                                    {
                                        this.cancel();
                                    }
                            }
                        }.runTaskTimer(Main.getInstance(), 20L, 20L);

                    }else {
                        e.setCancelled(true);
                        p.updateInventory();
                        p.sendMessage(ChatColor.DARK_RED + "You can use enderpearl in " + ChatColor.WHITE + pm.getEnderpearl() + ChatColor.DARK_RED + " second(s)");
                    }
                }
            }
            else {
                e.setCancelled(true);
                p.updateInventory();
            }
        }

    }
	
	@EventHandler
	public void PlayerPlaceBlock(BlockPlaceEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void PlayerBrokeBlock(BlockBreakEvent event) {
		event.setCancelled(true);
	}
	
	//-----------------------------------------------------------------------------------------------------------//
	
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent event) {
		event.getDrops().clear();
		event.setDeathMessage("");
		final Player player = event.getEntity().getPlayer();
		final Player killer = event.getEntity().getKiller();
		PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
		if (killer != null) {
			PlayerManager km = PlayerManager.getPlayerManager().get(killer.getUniqueId());
			if (km.getProfileState() == ProfileState.FIGHT || pm.getProfileState() == ProfileState.LOBBY) {
				player.spigot().respawn();
				if (pm.getNumberRound() > 1) {
					pm.getMatch().endMatch(MatchEndCause.KILL, player.getUniqueId(), km, player.getLocation());
					pm.getMatch().getLadder().setFightSize(pm.getMatch().getLadder().fightSize() - 2);
					return;
				}
				else {
					pm.getMatch().endMatch(MatchEndCause.KILL, player.getUniqueId(), km, player.getLocation());
					pm.getMatch().getLadder().setFightSize(pm.getMatch().getLadder().fightSize() - 2);
					pm.reset(player, GameMode.SURVIVAL);
					pm.sendToSpawn(player);
				}
			}
			if (pm.getProfileState() == ProfileState.PARTY_FIGHT) {
				System.out.println("Test of party death event.");
				final MatchManager match = pm.getMatch();
				match.partyDeathMatch(player.getUniqueId(), killer.getUniqueId());
			}
			else if (pm.getProfileState() == ProfileState.FFA) {
				pm.setDeath(pm.getDeath() + 1);
				km.setKill(km.getKill() + 1);
				player.spigot().respawn();
				MainList.getInstance().inFFA.remove(player.getUniqueId());
				player.sendMessage(ChatColor.DARK_RED + "You are dead! Killed by: " + ChatColor.WHITE + killer.getName());
				killer.sendMessage(ChatColor.DARK_RED + "You have been killed: " + ChatColor.WHITE + player.getName());
				pm.reset(player, GameMode.SURVIVAL);
	    		Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
	    			pm.setProfileState(ProfileState.LOBBY);
	    			pm.teleport(player, Main.getInstance().getSpawn());
	    			Main.getInstance().getItemManager().spawnItems(player);
	    		}, 1L);
			}	
		}
	}
}
