package com.seenk.practice.ladders;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.player.IKit;

import net.md_5.bungee.api.ChatColor;

public class Sumo extends MainLadders implements IKit {
	
    @Override
    public String name() {
        return "sumo";
    }

    @Override
    public String displayName() {
        return ChatColor.WHITE + "Sumo";
    }

    @Override
    public Material material() {
        return Material.LEASH;
    }

    @Override
    public byte data() {
        return (byte)0;
    }

    @Override
    public ArenaType arenaType() {
        return ArenaType.SUMO;
    }

    @Override
    public boolean isAlterable() {
        return false;
    }

    @Override
    public boolean isPlayingFFA() {
        return false;
    }
    
    @Override
    public boolean additionalInventory() {
        return true;
    }

    @Override
    public int id() {
        return 2;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {
        };
        return Armor;
    }

    @Override
    public ItemStack[] content() {
        ItemStack[] Contents = {

        };
        return Contents;
    }
    
    int queue = 0;
    int fight = 0;

	@Override
	public int setQueueSize(int size) {
		this.queue = size;
		return 0;
	}

	@Override
	public int setFightSize(int size) {
		this.fight = size;
		return 0;
	}

	@Override
	public int queueSize() {
		return queue;
	}

	@Override
	public int fightSize() {
		return fight;
	}
}