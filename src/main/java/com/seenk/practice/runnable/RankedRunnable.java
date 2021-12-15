package com.seenk.practice.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.managers.MatchManager;
import com.seenk.practice.managers.PlayerManager;
import com.seenk.practice.managers.QueueManager;
import com.seenk.practice.managers.match.MatchType;

import net.md_5.bungee.api.ChatColor;

public class RankedRunnable {

    private int min_range;
    private int  max_range;

    private int ranked_time;

    public static boolean active = false;

    public int getMin_range()
    {
        return this.min_range;
    }

    public void setMin_range(int new_range)
    {
        this.min_range = new_range;
    }

    public int getMax_range()
    {
        return this.max_range;
    }

    public void setMax_range(int new_range)
    {
        this.max_range = new_range;
    }

    public void setNewRange(int new_min, int new_max)
    {
        this.min_range = new_min;
        this.max_range = new_max;
    }

    public void setRanked_time(int ranked_time) {
        this.ranked_time = ranked_time;
    }

    public int getRanked_time()
    {
        return this.ranked_time;
    }

    public static boolean checkCompatibilityElo(PlayerManager player1, PlayerManager player2, MainLadders ladder) // 900 - 1000 | 800 - 2000
    {

        if(player2 == Main.getInstance().getQueueManager().getRankedQueuePlayer().get(Main.getInstance().getQueueManager().getClasseQueuePlayer())) return false;

		@SuppressWarnings("unused")
		int elo1 = PlayerManager.getPlayerManager().get(player1).getElos()[ladder.id()];
        int elo2 = PlayerManager.getPlayerManager().get(player2).getElos()[ladder.id()];

        return player1.getRanked().getMin_range() <= elo2 && player1.getRanked().getMax_range() >= elo2;
    }

    public void start(PlayerManager playerManager, Player player)
    {

        // 3 sec = 3*2 = 6

        active = true;
        setNewRange(1000-100, 1000+100);
        player.sendMessage(ChatColor.WHITE + "Searching in elo range " + ChatColor.GRAY + "[" + ChatColor.RED + min_range + ChatColor.GRAY + " - " + ChatColor.DARK_RED + max_range + ChatColor.GRAY + "]");
        ranked_time = 0;
        new BukkitRunnable() {
            public void run() {
                if(!active)
                {
                    this.cancel();
                    return;
                }
                ranked_time++;
                if((ranked_time % 5) != 0) return;
                if(ranked_time == 5)
                {
                    setMax_range(max_range+40);
                    if(min_range-40 >= 0) setMin_range(min_range-40);
                }else{
                    setMax_range(max_range+20);
                    if(min_range-20 >= 0) setMin_range(min_range-20);
                }
                player.sendMessage(ChatColor.WHITE + "Searching in elo range " + ChatColor.GRAY + "[" + ChatColor.RED + min_range + ChatColor.GRAY + " - " + ChatColor.DARK_RED + max_range + ChatColor.GRAY + "]");
            }
        }.runTaskTimer(Main.getInstance(), 50L, 50L);
    }

    public void stop()
    {
        active = false;
    }

    public void run() {
        while(true)
        {
            for(MainLadders ladder : MainList.getInstance().ladders)
            {
                QueueManager fightManager = MainList.getInstance().fight.get(ladder.displayName());
                if(fightManager != null && fightManager.getRankedQueuePlayer().values().size() >= 2)
                {
                    PlayerManager playerManager0 = fightManager.getRankedQueuePlayer().get(0);
                    for(PlayerManager playerManager : fightManager.getRankedQueuePlayer().values())
                    {
                        if(checkCompatibilityElo(playerManager0, playerManager, ladder))
                        {
                            new MatchManager(MatchType.RANKED,false, 1, playerManager0.getUuid(), playerManager.getUuid(), ladder.displayName());
                            break;
                        }
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        PlayerManager playerManager = PlayerManager.getPlayerManager().get(player.getUniqueId());
                        if(checkCompatibilityElo(playerManager0, playerManager, ladder))
                        {
                            Bukkit.broadcastMessage("Match found: elo1 =" + playerManager0.getElos()[ladder.id()] +" | elo2 = " + playerManager.getElos()[ladder.id()]);
                        }
                    }
                }
            }
        }
    }
}