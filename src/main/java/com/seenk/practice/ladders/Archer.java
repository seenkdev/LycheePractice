package com.seenk.practice.ladders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.player.IKit;
import com.seenk.practice.utils.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class Archer extends MainLadders implements IKit {

    @Override
    public String name() {
        return "Archer";
    }

    @Override
    public String displayName() {
        return ChatColor.WHITE + "Bow";
    }

    @Override
    public Material material() {
        return Material.BOW;
    }

    @Override
    public byte data() {
        return (byte)0;
    }

    @Override
    public ArenaType arenaType() {
        return ArenaType.NORMAL;
    }

    @Override
    public boolean isAlterable() {
        return true;
    }
    
    @Override
    public boolean isPlayingFFA() {
        return false;
    }

    @Override
    public boolean additionalInventory() {
        return true;
    }
    
    int queue = 0;
    int fight = 0;
    
	@Override
	public int setQueueSize(int size) {
		this.queue = size;
		return size;
	}

	@Override
	public int setFightSize(int size) {
		this.fight = size;
		return size;
	}
	

	@Override
	public int queueSize() {
		return queue;
	}

	@Override
	public int fightSize() {
		return fight;
	}

    @Override
    public int id() {
        return 5;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {new ItemBuilder(Material.LEATHER_BOOTS).addEnchant(Enchantment.DURABILITY, 1).addEnchant(Enchantment.PROTECTION_FALL, 4).toItemStack(),
                new ItemBuilder(Material.LEATHER_LEGGINGS).addEnchant(Enchantment.DURABILITY, 1).toItemStack(),
                new ItemBuilder(Material.LEATHER_CHESTPLATE).addEnchant(Enchantment.DURABILITY, 1).toItemStack(),
                new ItemBuilder(Material.LEATHER_HELMET).addEnchant(Enchantment.DURABILITY, 1).toItemStack()
        };
        return Armor;
    }

    @Override
    public ItemStack[] content() {
        ItemStack[] Contents = {new ItemBuilder(Material.BOW).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.ARROW_INFINITE, 1).toItemStack(),
                new ItemBuilder(Material.ENDER_PEARL, 16, (short)0).toItemStack(),
                air,
                air,
                air,
                air,
                air,
                air,
                new ItemBuilder(Material.COOKED_BEEF, 64, (short)0).toItemStack(),
                
                new ItemBuilder(Material.ARROW, 1, (short)0).toItemStack(),
                

        };
        return Contents;
    }
}
