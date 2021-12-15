 package com.seenk.practice.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.seenk.practice.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.arena.ArenaManager;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.match.MatchEndCause;
import com.seenk.practice.managers.match.MatchType;
import com.seenk.practice.managers.party.PartyFightType;
import com.seenk.practice.player.IKit;
import com.seenk.practice.state.MatchStatus;
import com.seenk.practice.state.ProfileState;
import com.seenk.practice.utils.Timer;

import co.aikar.idb.DB;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MatchManager
{
    static List<MatchManager> all;
    private UUID uuid1;
    private UUID uuid2;
    private Integer numberOfRound;
    private boolean round;
    private String name1;
    private String name2;
    final Player player1;
    final Player player2;
    private MatchStatus matchStatus;
    private List<UUID> specs;
    private List<UUID> mod;
    private List<UUID> party1;
    private List<UUID> party2;
    private MatchType fightType;
    private ArenaManager arena;
    private MainLadders ladder;
    private Timer timer;
    
    public MatchManager(final MatchType matchType, boolean round, Integer numberOfRound, UUID uuid1, UUID uuid2, final String ladderDisplayName) {
		this.matchStatus = MatchStatus.STARTING;
        this.specs = new ArrayList<UUID>();    
        this.timer = new Timer(Main.getInstance(), 5, true);
        this.uuid1 = uuid1;
        this.uuid2 = uuid2;
        this.round = round;
        if (this.round == true) {
        	this.numberOfRound = numberOfRound;
        }
        this.fightType = matchType;
        this.name1 = Bukkit.getPlayer(uuid1).getName();
        this.name2 = Bukkit.getPlayer(uuid2).getName();
        this.player1 = PlayerManager.getPlayerManager().get(uuid1).getPlayer();
        this.player2 = PlayerManager.getPlayerManager().get(uuid2).getPlayer();
        final PlayerManager playerManager1 = PlayerManager.getPlayerManager().get(uuid1);
        final PlayerManager playerManager2 = PlayerManager.getPlayerManager().get(uuid2);
        if (matchType == MatchType.RANKED) {
        	playerManager1.getRanked().stop();
        	playerManager2.getRanked().stop();
        	Main.getInstance().getQueueManager().getRankedQueuePlayer().remove(playerManager1);
        	Main.getInstance().getQueueManager().getRankedQueuePlayer().remove(playerManager2);
        	MainList.haveRanked.add(playerManager1);
            MainList.haveRanked.add(playerManager2);
       }
       else {
           Main.getInstance().getQueueManager().setNormalQueuePlayer(null);	
       }
        playerManager1.setMatch(this);
        playerManager2.setMatch(this);
        final MainLadders ladder = MainLadders.getLadder(ladderDisplayName);
        this.ladder = ladder;
        final ArenaManager arena = ArenaManager.getRandomArena(ladder.arenaType());
        this.arena = arena;
        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
            player1.hidePlayer(all);
            player2.hidePlayer(all);
            all.hidePlayer(player1);
            all.hidePlayer(player2);
        }
        if (fightType == MatchType.PARTY_EVENT) {
            playerManager1.setProfileState(ProfileState.PARTY_FIGHT);
            playerManager2.setProfileState(ProfileState.PARTY_FIGHT);
            this.party1 = new ArrayList<UUID>();
            this.party2 = new ArrayList<UUID>();
            party1.add(uuid1);
            party2.add(uuid2);
			for (int i = 0; i<party1.size();i++) {
				for (int i2 = 0; i<party2.size();i++) {
	            	final UUID partyuuid1 = party1.get(i);
	            	final UUID partyuuid2 = party2.get(i2);
	            	final Player player1 = Bukkit.getPlayer(partyuuid1);
	            	final Player player2 = Bukkit.getPlayer(partyuuid2);
	                player1.teleport(arena.getLoc1());
	                player2.teleport(arena.getLoc2());
	            	player1.showPlayer(player2);
	            	player2.showPlayer(player1);
	            	player1.showPlayer(player1);
	            	player2.showPlayer(player2);
				}
            }
        }
        else {
            playerManager1.setProfileState(ProfileState.FIGHT);
            playerManager2.setProfileState(ProfileState.FIGHT);
            player1.teleport(arena.getLoc1());
            player2.teleport(arena.getLoc2());
            player1.showPlayer(player2);
            player2.showPlayer(player1);
        }
        player1.setNoDamageTicks(20);
        player1.setMaximumNoDamageTicks(20);
        player2.setNoDamageTicks(20);
        player2.setMaximumNoDamageTicks(20);
        if (ladder instanceof IKit) {
            final IKit kit = (IKit)ladder;
            if (ladderDisplayName.equals(ChatColor.WHITE + "Sumo")) {
            	player1.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 255));
            	player2.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 255));
                player1.setNoDamageTicks(20);
                player1.setMaximumNoDamageTicks(20);
                player2.setNoDamageTicks(20);
                player2.setMaximumNoDamageTicks(20);
            }
            if (ladderDisplayName.equals(ChatColor.WHITE + "Combo-Fly")) {
                player1.setNoDamageTicks(0);
                player1.setMaximumNoDamageTicks(1);
                player2.setNoDamageTicks(0);
                player2.setMaximumNoDamageTicks(1);
            }
            else {
                player1.setNoDamageTicks(19);
                player1.setMaximumNoDamageTicks(19);
                player2.setNoDamageTicks(19);
                player2.setMaximumNoDamageTicks(19);
            }
            playerManager1.setCurrentFightLadder(kit);
            playerManager2.setCurrentFightLadder(kit);
            playerManager1.sendKit(kit);
            playerManager2.sendKit(kit);
        }
        new BukkitRunnable() {
            int i = 5;

            @Override
            public void run() {
                if (matchStatus == MatchStatus.FINISHED) {
                    this.cancel();
                } else {
                	MatchManager.this.sendGlobalMessage(ChatColor.RED + "Match starting in " + ChatColor.WHITE + i, player1, player2);
                    player1.playSound(player1.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
                    player2.playSound(player2.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0f, 1.0f);
                    i -= 1;
                    if (i <= 0) {
                    	if (player1.getItemInHand().getType() == Material.BOOK) {
                    		final IKit kit = (IKit)ladder;
                    		playerManager1.sendKit(kit);
                    	}
                    	if (player2.getItemInHand().getType() == Material.BOOK) {
                    		final IKit kit = (IKit)ladder;
                    		playerManager2.sendKit(kit);
                    	}
                        matchStatus = MatchStatus.PLAYING;
                        MatchManager.this.sendGlobalMessage(ChatColor.DARK_RED + "Match started, good luck!", player1, player2);
                        MatchManager.this.sendGlobalMessage("  ", player1, player2);
                        MatchManager.this.sendGlobalMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.RESET + "»" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "--------------------------" + ChatColor.RESET + ChatColor.GRAY + "«" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "--", player1, player2);
                        MatchManager.this.sendGlobalMessage(ChatColor.GRAY + "» " + ChatColor.DARK_RED + "DISCLAIMER!", player1, player2);
                        MatchManager.this.sendGlobalMessage(ChatColor.GRAY + "» " + ChatColor.WHITE + " The jitter click or another method for up them cps possibly result to a ban! Good luck!", player1, player2);
                        MatchManager.this.sendGlobalMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--" + ChatColor.RESET + "»" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "--------------------------" + ChatColor.RESET + ChatColor.GRAY + "«" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "--", player1, player2);
                        MatchManager.this.sendGlobalMessage("  ", player1, player2);
                        this.cancel();
                        timer = new Timer(Main.getInstance(), -1, false);
                        timer.start();
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 20L, 20L);
        MatchManager.all.add(this);			
    }

    public void setNumberOfRound(Integer numberOfRound) {
		this.numberOfRound = numberOfRound;
	}
    
    public void setRound(boolean round) {
		this.round = round;
	}
    
    public Integer getNumberOfRound() {
		return numberOfRound;
	}
    
    public boolean isRound() {
		return round;
	}

    public List<UUID> getParty1() {
		return party1;
	}
    
    public List<UUID> getParty2() {
		return party2;
	}
    
    public void setArena(ArenaManager arena) {
		this.arena = arena;
	}
    
    public static void setAll(List<MatchManager> all) {
		MatchManager.all = all;
	}
    
    public void setFightType(MatchType fightType) {
		this.fightType = fightType;
	}
    
    public void setLadder(MainLadders ladder) {
		this.ladder = ladder;
	}
    
    public void setMod(List<UUID> mod) {
		this.mod = mod;
	}
    
    public void setName1(String name1) {
		this.name1 = name1;
	}
    
    public void setName2(String name2) {
		this.name2 = name2;
	}
    
    public void setSpecs(List<UUID> specs) {
		this.specs = specs;
	}
    
    public void setUuid1(UUID uuid1) {
		this.uuid1 = uuid1;
	}
    
    public void setUuid2(UUID uuid2) {
		this.uuid2 = uuid2;
	}
    
    public String formatTimer(final int time) {
        final int minute = time / 60;
        final int second = time % 60;
        return ((minute < 10) ? "0" : "") + minute + ":" + ((second < 10) ? "0" : "") + second;
    }
    
    public void sendGlobalMessage(final String message, final Player... players) {
        for (final Player player : players) {
            player.sendMessage(message);
        }
    }
    
    public void sendGlobalMessage(final String[] message, final Player... players) {
        for (final Player player : players) {
            player.sendMessage(message);
        }
    }
    
    public void sendGlobalMessage(final TextComponent message, final Player... players) {
        for (final Player player : players) {
            player.spigot().sendMessage(message);
        }
    }
    
    public static MatchManager getMatchManager(final UUID uuid) {
        return MatchManager.all.stream().filter(matchManager -> matchManager.getUuid1() == uuid || matchManager.getUuid2() == uuid).findFirst().orElse(null);
    }
    
    public void addPartyPlayerToSpectate(final Player player) {
    	player.spigot().respawn();
    	final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
    	pm.reset(player, GameMode.ADVENTURE);
    	pm.getMatch().getArena().addSpectator(player.getUniqueId());
    	player.teleport(pm.getMatch().getArena().getLoc1());
    	if (getParty1().contains(player.getUniqueId())) {
    		getParty1().remove(player.getUniqueId());
    	}
    	if (getParty2().contains(player.getUniqueId())) {
    		getParty2().remove(player.getUniqueId());
    	}
    }
    
    public UUID getUuid1() {
        return this.uuid1;
    }
    
    public UUID getUuid2() {
        return this.uuid2;
    }
    
    public String getName1() {
        return this.name1;
    }
    
    public String getName2() {
        return this.name2;
    }
    
    public MatchStatus getMatchStatus() {
        return this.matchStatus;
    }
    
    public List<UUID> getSpecs() {
        return this.specs;
    }
    
    public List<UUID> getMod() {
        return this.mod;
    }
    
    public MatchType getFightType() {
        return this.fightType;
    }
    
    public ArenaManager getArena() {
        return this.arena;
    }
    
    public MainLadders getLadder() {
        return this.ladder;
    }
    
    public Timer getTimer() {
		return timer;
	}
    
    public void setTimer(Timer timer) {
		this.timer = timer;
	}
    
    public static List<MatchManager> getAll() {
        return MatchManager.all;
    }
    
    public void setMatchStatus(final MatchStatus matchStatus) {
        this.matchStatus = matchStatus;
    }
    
    static {
        MatchManager.all = new ArrayList<MatchManager>();
    }
    
    public void partyDeathMatch(final UUID uuid1, final UUID uuid2) {
    	System.out.println("Test de la mort qui tue");
    	final Player player = Bukkit.getPlayer(uuid1);
    	final Player killer = Bukkit.getPlayer(uuid2);
        this.setMatchStatus(MatchStatus.FINISHED);
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
    			if (getParty1().contains(player.getUniqueId())) {
    				if (getParty1().size() == 0) {
    			        timer.stop();
    					if (PlayerManager.getPlayerManager().get(player.getUniqueId()).getPartyManager().getFightType() == PartyFightType.SPLIT) {
            				sendGlobalMessage(ChatColor.BLUE + "Blue Team" + ChatColor.WHITE + " have won the game!", Bukkit.getPlayer(getUuid1()), Bukkit.getPlayer(getUuid2()));	
    					}
    					else {
    						sendGlobalMessage(ChatColor.DARK_RED + killer.getName() + ChatColor.WHITE + " have won the game!", Bukkit.getPlayer(getUuid1()), Bukkit.getPlayer(getUuid2()));	
    					}
        				final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
        				final UUID leaderUuid = pm.getPartyManager().getLeader();
        				pm.getPartyManager().getAllMembers().add(leaderUuid);
        				final PlayerManager leaderM = PlayerManager.getPlayerManager().get(leaderUuid);
        				for (int i = 0; i<leaderM.getPartyManager().getAllMembers().size();i++) {
                            UUID uuidMember = pm.getPartyManager().getAllMembers().get(i);
                            final PlayerManager partyPM = PlayerManager.getPlayerManager().get(uuidMember);
                            Bukkit.getPlayer(uuidMember).getInventory().clear();
                            Bukkit.getPlayer(leaderUuid).getInventory().clear();
                            Bukkit.getPlayer(uuidMember).getInventory().setBoots(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(uuidMember).getInventory().setChestplate(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(uuidMember).getInventory().setHelmet(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(uuidMember).getInventory().setLeggings(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setBoots(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setChestplate(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setHelmet(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setLeggings(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().clear();
                            Bukkit.getPlayer(leaderUuid).getActivePotionEffects().clear();
                            Bukkit.getPlayer(uuidMember).getActivePotionEffects().clear();
                            Bukkit.getPlayer(uuidMember).setHealth(Bukkit.getPlayer(leaderUuid).getMaxHealth());
                            Bukkit.getPlayer(leaderUuid).setHealth(Bukkit.getPlayer(leaderUuid).getMaxHealth());
        					partyPM.teleport(Bukkit.getPlayer(uuidMember), Main.getInstance().getSpawn());
        					Main.getInstance().getItemManager().partyItems(Bukkit.getPlayer(uuidMember));
         					partyPM.setProfileState(ProfileState.PARTY);
        				}
    				}
    				else {
                        getParty1().remove(player.getUniqueId());
                        addPartyPlayerToSpectate(player);
                        sendGlobalMessage(ChatColor.WHITE + killer.getName() + ChatColor.DARK_RED + " have killed " + ChatColor.WHITE + player.getName(), Bukkit.getPlayer(getUuid1()), Bukkit.getPlayer(getUuid2()));
                    }
    			}
    			else {
    				if (getParty2().size() == 0) {
    					if (PlayerManager.getPlayerManager().get(player.getUniqueId()).getPartyManager().getFightType() == PartyFightType.SPLIT) {
            				sendGlobalMessage(ChatColor.DARK_RED + "Red Team" + ChatColor.WHITE + " have won the game!", Bukkit.getPlayer(getUuid1()), Bukkit.getPlayer(getUuid2()));	
    					}
    					else {
    						sendGlobalMessage(ChatColor.DARK_RED + killer.getName() + ChatColor.WHITE + " have won the game!", Bukkit.getPlayer(getUuid1()), Bukkit.getPlayer(getUuid2()));	
    					}
        				final PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
        				final UUID leaderUuid = pm.getPartyManager().getLeader();
        				pm.getPartyManager().getAllMembers().add(leaderUuid);
        				for (int i = 0; i<pm.getPartyManager().getAllMembers().size();i++) {
                            UUID uuidMember = pm.getPartyManager().getAllMembers().get(i);
                            final PlayerManager partyPM = PlayerManager.getPlayerManager().get(uuidMember);
                            Bukkit.getPlayer(uuidMember).getInventory().clear();
                            Bukkit.getPlayer(leaderUuid).getInventory().clear();
                            Bukkit.getPlayer(uuidMember).getInventory().setBoots(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(uuidMember).getInventory().setChestplate(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(uuidMember).getInventory().setHelmet(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(uuidMember).getInventory().setLeggings(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setBoots(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setChestplate(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setHelmet(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().setLeggings(new ItemBuilder(Material.AIR).toItemStack());
                            Bukkit.getPlayer(leaderUuid).getInventory().clear();
                            Bukkit.getPlayer(leaderUuid).getActivePotionEffects().clear();
                            Bukkit.getPlayer(uuidMember).getActivePotionEffects().clear();
                            Bukkit.getPlayer(uuidMember).setHealth(Bukkit.getPlayer(leaderUuid).getMaxHealth());
                            Bukkit.getPlayer(leaderUuid).setHealth(Bukkit.getPlayer(leaderUuid).getMaxHealth());
                            partyPM.teleport(Bukkit.getPlayer(uuidMember), Main.getInstance().getSpawn());
                            Main.getInstance().getItemManager().partyItems(Bukkit.getPlayer(uuidMember));
                            partyPM.setProfileState(ProfileState.PARTY);
        				}
    				}
    				else {
                        getParty2().remove(player.getUniqueId());
                        addPartyPlayerToSpectate(player);
                        sendGlobalMessage(ChatColor.WHITE + killer.getName() + ChatColor.DARK_RED + " have killed " + ChatColor.WHITE + player.getName(), Bukkit.getPlayer(getUuid1()), Bukkit.getPlayer(getUuid2()));
                    }
    			}
            }
        }, 20L);
    }
    
    private int getEloDifference(final PlayerManager winner, final PlayerManager loser, final MainLadders ladder) {
        final int difference = loser.getElos()[ladder.id()] - winner.getElos()[ladder.id()];
        if (difference >= 16) {
            return 16;
        }
        if (difference <= 5) {
            return 5;
        }
        return difference;
    }

	public void endMatch(final MatchEndCause cause, final UUID uuid1, final PlayerManager playerManager, final Location deathLocation) {
        timer.stop();
        final Player player = Bukkit.getPlayer(uuid1);
        final UUID uuid = (this.getUuid2() == player.getUniqueId()) ? this.getUuid1() : this.getUuid2();
        final Player player2 = Bukkit.getPlayer(uuid);
        final PlayerManager playerManager2 = PlayerManager.getPlayerManager().get(uuid);
        player.setNoDamageTicks(20);
        player.setMaximumNoDamageTicks(20);
        player2.setNoDamageTicks(20);
        player2.setMaximumNoDamageTicks(20);
        PlayerManager.saveInventory(player, player2);
        player2.getInventory().clear();
        if (this.fightType == MatchType.RANKED) {
            final int eloChange = MatchManager.this.getEloDifference(playerManager2, playerManager, MatchManager.this.ladder);
            final String eloChangeMessage = ChatColor.GOLD + "New elo change " + ChatColor.GRAY + "» " + ChatColor.YELLOW + player2.getName() + ChatColor.GRAY + " (" + ChatColor.GOLD + playerManager2.getElos()[MatchManager.this.ladder.id()] + ChatColor.GRAY + " + " + ChatColor.GREEN + eloChange + ChatColor.GRAY + "), " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " (" + ChatColor.YELLOW + playerManager.getElos()[MatchManager.this.ladder.id()] + ChatColor.GRAY + "-" + ChatColor.RED + eloChange + ChatColor.GRAY +  ")";
            player.sendMessage(eloChangeMessage);
            player2.sendMessage(eloChangeMessage);
            playerManager2.getElos()[MatchManager.this.ladder.id()] += eloChange;
            playerManager.getElos()[MatchManager.this.ladder.id()] -= eloChange;
            try {
                final String elos1 = playerManager.getStringValue(playerManager.getElos(), ":");
                DB.executeUpdate("UPDATE playersdata SET elos=? WHERE name=?", elos1, this.name1);
                final String elos2 = playerManager.getStringValue(playerManager2.getElos(), ":");
                DB.executeUpdate("UPDATE playersdata SET elos=? WHERE name=?", elos2, this.name2);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.setMatchStatus(MatchStatus.FINISHED);
        if (cause == MatchEndCause.KILL) {
            player2.hidePlayer(player);
            if (player.getKiller() != null) {
                this.sendGlobalMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has been slain by " + ChatColor.GREEN + player.getKiller().getName(), player, player2);
            }
        }
        else {
            player2.sendMessage(ChatColor.DARK_RED + "Your opponent has leave the server!");
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
            	for (Player players : Bukkit.getOnlinePlayers()) {
                    players.showPlayer(player2);
                    player2.showPlayer(players);
            	}
                final String[] resultMessage = { ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--»------------------------«--", ChatColor.DARK_RED + "Winner: " + ChatColor.WHITE + player2.getName(), ChatColor.DARK_RED + "Loser: " + ChatColor.WHITE + player.getName(), ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--»------------------------«--" };
                final TextComponent inventoriesMessage = new TextComponent(ChatColor.DARK_RED + "Inventories" + ChatColor.GRAY + "(click to view)" + ChatColor.GRAY + ": " + ChatColor.WHITE);
                final TextComponent name1 = new TextComponent(ChatColor.GREEN + player2.getName());
                name1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + player2.getName()));
                name1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to view this inventory.").create()));
                inventoriesMessage.addExtra(name1);
                inventoriesMessage.addExtra(ChatColor.GRAY + ", " + ChatColor.YELLOW);
                final TextComponent name2 = new TextComponent(ChatColor.RED + player.getName());
                name2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/inventory " + player.getName()));
                name2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "Click to view this inventory.").create()));
                inventoriesMessage.addExtra(name2);
                MatchManager.this.sendGlobalMessage(inventoriesMessage, player2);
                playerManager2.reset(player2, GameMode.SURVIVAL);
                playerManager.reset(player, GameMode.SURVIVAL);
                if (cause == MatchEndCause.KILL) {
                    MatchManager.this.sendGlobalMessage(resultMessage, player);
                    MatchManager.this.sendGlobalMessage(inventoriesMessage, player);
                    if (MatchManager.this.round == true) {
                    	if (MatchManager.this.numberOfRound == 3) {
                    		if (playerManager.getWonRound() == 2 || playerManager2.getWonRound() == 2) {
                                playerManager.sendToSpawn(player);
                                playerManager2.sendToSpawn(player2);
                    		}
                    		if (playerManager.getWonRound() == 4 || playerManager2.getWonRound() == 4) {
                                playerManager.sendToSpawn(player);
                                playerManager2.sendToSpawn(player2);
                    		}
                    		else {
                    			new MatchManager(MatchType.UNRANKED, true, playerManager.getNumberRound(), player.getUniqueId(), player2.getUniqueId(), playerManager.getDuelLadder());
                    		}
                    	}
                    }
                    else {
                        playerManager.sendToSpawn(player);
                        playerManager2.sendToSpawn(player2);	
                    }
                }
                for (final UUID spectators : MatchManager.this.specs) {
                    final Player spec = Bukkit.getPlayer(spectators);
                    final PlayerManager specm = PlayerManager.getPlayerManager().get(spectators);
                    specm.reset(spec, GameMode.SURVIVAL);
                    specm.sendToSpawn(spec);
                }
            }
        }, 60L);
    }
}