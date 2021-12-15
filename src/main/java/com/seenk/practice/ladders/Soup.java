package com.seenk.practice.ladders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.player.IKit;
import com.seenk.practice.utils.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class Soup extends MainLadders implements IKit{

    @Override
    public String name() {
        return "Soup";
    }

    @Override
    public String displayName() {
        return ChatColor.WHITE + "Soup";
    }

    @Override
    public Material material() {
        return Material.MUSHROOM_SOUP;
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
        return true;
    }

    @Override
    public boolean additionalInventory() {
        return true;
    }   

    @Override
    public int id() {
        return 6;
    }

	@Override
    public ItemStack[] content() {
        ItemStack[] Contents = {new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),
                new ItemBuilder(Material.COOKED_BEEF, 64, (short)0).toItemStack(),
                new ItemBuilder(Material.MUSHROOM_SOUP, 1, (short)0).toItemStack(),
                

        };
        return Contents;
    }


	@Override
	public ItemStack[] armor() {
        ItemStack[] Armor = {new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
                new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
                new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack(),
                new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack()
        };
        return Armor;
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
