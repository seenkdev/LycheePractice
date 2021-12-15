package com.seenk.practice.board;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;
import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.MatchManager;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.match.MatchType;
import com.seenk.practice.managers.party.PartyFightType;
import com.seenk.practice.state.MatchStatus;
import com.seenk.practice.state.ProfileState;

public class ScoreboardRunnable extends BukkitRunnable {

    private static final Map<UUID, ScoreboardHelper> boardMap = Maps.newConcurrentMap();
    
    @Override
    public void run() {
        for (Map.Entry<UUID, ScoreboardHelper> entry : boardMap.entrySet()){
            UUID uuid = entry.getKey();
            if (Bukkit.getPlayer(uuid) == null)boardMap.remove(uuid);
        }
        
        int inqueue = Main.getInstance().getQueueManager().getQueue(MatchType.UNRANKED);

        int players = Bukkit.getOnlinePlayers().size();

        for (Player player : Bukkit.getOnlinePlayers()){	
            final UUID uuid = player.getUniqueId();
            ScoreboardHelper board = boardMap.get(uuid);

            if (board == null){
                board = new ScoreboardHelper(player, ScoreboardUtils.SCOREBOARD_TITLE);
                boardMap.put(uuid, board);
            }
           
            PlayerManager pm = PlayerManager.getPlayerManager().get(player.getUniqueId());
            ProfileState status = pm.getProfileState();
            if (pm.getSettingsManager().isScoreboard() == true) {
                for (int i = 0; i<MainList.getInstance().ladders.size();i++) {
                    final MainLadders ladder = MainList.getInstance().ladders.get(i);
                    board.clear();
                    if (status == ProfileState.LOBBY || status == ProfileState.QUEUE || status == ProfileState.PARTY || status == ProfileState.MOD) {
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    	board.add(ChatColor.DARK_RED + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + players);
                    	board.add(ChatColor.DARK_RED + "Queueing" + ChatColor.GRAY + ": " + ChatColor.WHITE + inqueue);
                    	board.add(ChatColor.DARK_RED + "Fighting" + ChatColor.GRAY + ": " + ChatColor.WHITE + ladder.fightSize());
                    	if (pm.getProfileState() == ProfileState.MOD) {
                    		board.add(ChatColor.DARK_RED + "Staff Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + MainList.getInstance().getStaffOnline().size());
						}
                    	board.add("");
                    	if (status == ProfileState.QUEUE) {
                    		board.add(ChatColor.DARK_RED + "Queue:");
            				board.add(ChatColor.RED + "Duration: " + ChatColor.WHITE + "Unabled");
                    		board.add("");
                    	}
                    	if (status == ProfileState.PARTY) {
                    		board.add(ChatColor.DARK_RED + "Party:");
                    		board.add(ChatColor.RED + "Leader: " + ChatColor.WHITE + Bukkit.getPlayer(pm.getPartyManager().getLeader()).getName());
                    		int currentSize = pm.getPartyManager().getCurrentSize();
                    		board.add(ChatColor.RED + "In Party: " + ChatColor.WHITE + currentSize);
                    		board.add("");
                    	}
                    	board.add(ScoreboardUtils.SCOREBOARD_IP);
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    }
                    if(pm.getProfileState() == ProfileState.FFA) {
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    	board.add(ChatColor.DARK_RED + "FFA:");
                    	board.add(ChatColor.DARK_RED + "Ladder: " + ChatColor.WHITE + "Progress in dev");
                    	board.add(ChatColor.DARK_RED + "Kill: " + ChatColor.WHITE + pm.getKill());
                    	board.add(ChatColor.DARK_RED + "Death: " + ChatColor.WHITE + pm.getDeath());
                    	board.add(ChatColor.DARK_RED + "Status: " + ChatColor.WHITE + pm.getFfaStatus().toString());
                    	board.add("");
                    	board.add(ScoreboardUtils.SCOREBOARD_IP);
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    }
                    if (pm.getProfileState() == ProfileState.FIGHT) {
                    	final MatchManager match = pm.getMatch();
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    	if (match.getMatchStatus() == MatchStatus.STARTING) {
                    		board.add(ChatColor.DARK_RED + "Starting...");
                    	}
                    	if (match.getMatchStatus() == MatchStatus.PLAYING) {
                    		if (match.getName1() == player.getName()) {
                        		board.add(ChatColor.DARK_RED + "Opponent: " + ChatColor.WHITE + match.getName2());	
                    		}
                    		else {
                    			board.add(ChatColor.DARK_RED + "Opponent: " + ChatColor.WHITE + match.getName1());
                    		}
                    		if (pm.getNumberRound() > 1) {
                        		board.add(ChatColor.DARK_RED + "Round Win: " + ChatColor.WHITE + pm.getWonRound());	
                    		}
                    		board.add(ChatColor.DARK_RED + "Duration: " + ChatColor.WHITE + pm.getMatch().getTimer().getTime().toString());
                    	}
                    	if (match.getMatchStatus() == MatchStatus.FINISHED) {
                    		board.add(ChatColor.RED + "Game Finished");
                    	}
                		board.add("");
                    	board.add(ScoreboardUtils.SCOREBOARD_IP);
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    }
                    if (pm.getProfileState() == ProfileState.PARTY_FIGHT) {
                    	final MatchManager match = pm.getMatch();
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    	if (match.getMatchStatus() == MatchStatus.STARTING) {
                    		board.add(ChatColor.DARK_RED + "Starting...");
                    	}
                    	if (match.getMatchStatus() == MatchStatus.PLAYING) {
                    		if (pm.getPartyManager().getFightType() == PartyFightType.FFA) {
                    			board.add(ChatColor.DARK_RED + "Alived: " + ChatColor.WHITE + match.getParty1().size() + match.getParty2().size());
                    		}
                    		else {
                        		if (match.getName1() == player.getName()) {
                        			board.add(ChatColor.DARK_RED + "Color:" + ChatColor.WHITE + " Red");
                            		board.add(ChatColor.DARK_RED + "Opponent Alived: " + ChatColor.WHITE + match.getParty2().size());
                            		board.add(ChatColor.DARK_RED + "Alived: " + ChatColor.WHITE + match.getParty1().size());
                        		}
                        		else {
                        			board.add(ChatColor.DARK_RED + "Color:" + ChatColor.WHITE + " Blue");
                        			board.add(ChatColor.DARK_RED + "Opponent Alived: " + ChatColor.WHITE + match.getParty1().size());
                        			board.add(ChatColor.DARK_RED + "Alived: " + ChatColor.WHITE + match.getParty2().size());
                        		}
                    		}
                    	}
                    	if (match.getMatchStatus() == MatchStatus.FINISHED) {
                    		board.add(ChatColor.RED + "Game Finished");
                    	}
                		board.add("");
                    	board.add(ScoreboardUtils.SCOREBOARD_IP);
                    	board.add(ScoreboardUtils.SCOREBOARD_LINE);
                    }
                }
                board.update();
            }
            else {
            	board.clear();
            	board.update();
            }
        }
    }
}
