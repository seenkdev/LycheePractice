package com.seenk.practice.ladders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.player.IKit;
import com.seenk.practice.utils.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class Gapple extends MainLadders implements IKit {

    @Override
    public String name() {
        return "gapple";
    }

    @Override
    public String displayName() {
        return ChatColor.WHITE + "Gapple";
    }

    @Override
    public Material material() {
        return Material.GOLDEN_APPLE;
    }

    @Override
    public byte data() {
        return (byte)1;
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

    @Override
    public int id() {
        return 4;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_FALL, 4).toItemStack(),
                new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).toItemStack()
        };
        return Armor;
    }

    @Override
    public ItemStack[] content() {
        ItemStack[] Contents = {new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.DURABILITY, 5).addEnchant(Enchantment.FIRE_ASPECT, 1).toItemStack(),
                new ItemBuilder(Material.GOLDEN_APPLE, 64, (short)1).toItemStack(),
                air,
                air,
                air,
                air,
                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)8233).toItemStack(),
                new ItemBuilder(Material.COOKED_BEEF, 64, (short)0).toItemStack(),

                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)8233).toItemStack(),

                
                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)8233).toItemStack(),


                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)8233).toItemStack(),

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
		// TODO Auto-generated method stub
		return queue;
	}

	@Override
	public int fightSize() {
		// TODO Auto-generated method stub
		return fight;
	}
}
