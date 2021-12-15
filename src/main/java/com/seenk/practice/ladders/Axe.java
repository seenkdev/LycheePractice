package com.seenk.practice.ladders;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.seenk.practice.arena.ArenaType;
import com.seenk.practice.player.IKit;
import com.seenk.practice.utils.ItemBuilder;

import net.md_5.bungee.api.ChatColor;

public class Axe extends MainLadders implements IKit {
	
    @Override
    public String name() {
        return "axe";
    }

    @Override
    public String displayName() {
        return ChatColor.WHITE + "Axe";
    }

    @Override
    public Material material() {
        return Material.IRON_AXE;
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
    public int id() {
        return 3;
    }

    @Override
    public ItemStack[] armor() {
        ItemStack[] Armor = {new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.PROTECTION_FALL, 4).toItemStack(),
                new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).addEnchant(Enchantment.DURABILITY, 3).toItemStack()
        };
        return Armor;
    }

    @Override
    public ItemStack[] content() {
        ItemStack[] Contents = {new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.DAMAGE_ALL, 2).addEnchant(Enchantment.DURABILITY, 3).toItemStack(),
                new ItemBuilder(Material.GOLDEN_APPLE, 16, (short)0).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)16421).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)16421).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)16421).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)16421).toItemStack(),
                new ItemBuilder(Material.POTION, 1, (short)16421).toItemStack(),
                new ItemBuilder(Material.COOKED_BEEF, 64, (short)0).toItemStack(),

                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),

                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),

                new ItemBuilder(Material.POTION, 1, (short)8226).toItemStack(),

        };
        return Contents;
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