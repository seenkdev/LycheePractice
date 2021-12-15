package com.seenk.practice.ladders;

import org.bukkit.Material;

import com.seenk.practice.MainList;
import com.seenk.practice.arena.ArenaType;

public abstract class MainLadders {

    public abstract String name();
    public abstract String displayName();
    public abstract Material material();
    public abstract byte data();
    public abstract ArenaType arenaType();
    public abstract int id();
    public abstract int setQueueSize(int size);
    public abstract int setFightSize(int size);
    public abstract int queueSize();
    public abstract int fightSize();

    // Kit editor
    public abstract boolean isAlterable();
    public abstract boolean additionalInventory();

    public static MainLadders getLadder(String displayName)
    {
        return MainList.getInstance().ladders.stream().filter(ladder -> ladder.displayName().equals(displayName)).findFirst().orElse(null);
    }
	public abstract boolean isPlayingFFA();

}
