package com.seenk.practice.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.seenk.practice.Main;
import com.seenk.practice.MainList;
import com.seenk.practice.ladders.MainLadders;
import com.seenk.practice.utils.ItemBuilder;

public class LeaderboardInventory implements Runnable {

    @Override
    public void run() {
        Main.getInstance().leaderboardInventory = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Leaderboard:");
        for(MainLadders ladder : MainList.getInstance().ladders)
        {
            ItemStack item = new ItemBuilder(ladder.material(), 1, ladder.data()).setName(ladder.displayName()).toItemStack();
            Main.getInstance().leaderboardInventory.setItem(ladder.id()+9 ,item);
        }
        ItemStack item = new ItemBuilder(Material.NETHER_STAR, 1, (byte)0).setName(ChatColor.RED + "Top " + ChatColor.DARK_RED + "Global").toItemStack();
        Main.getInstance().leaderboardInventory.setItem(4 ,item);

        while(true) {
            if (Bukkit.getOnlinePlayers().size() >= 1) {
                Main.getInstance().leaderboard.refresh();
                Top[] top = Main.getInstance().leaderboard.getTop();
                Top global_top = Main.getInstance().leaderboard.getGlobal();
                for (MainLadders ladder : MainList.getInstance().ladders) {
                    ItemStack current = Main.getInstance().leaderboardInventory.getItem(ladder.id() + 9);
                    ItemMeta meta = current.getItemMeta();

                    meta.setLore(top[ladder.id()].getLore());
                    //current.setAmount(Practice.getInstance().fight.get(ladder.displayName()).getFight(FightType.NORMAL));
                    current.setItemMeta(meta);
                }

                ItemStack current = Main.getInstance().leaderboardInventory.getItem(4);
                ItemMeta meta = current.getItemMeta();

                meta.setLore(global_top.getLore());
                //current.setAmount(Practice.getInstance().fight.get(ladder.displayName()).getFight(FightType.NORMAL));
                current.setItemMeta(meta);

                try {
                    Thread.sleep(1000 * 15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
  