package com.seenk.practice.player;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IKit {

    ItemStack[] content();

    ItemStack[] armor();

    ItemStack air = new ItemStack(Material.AIR);

}
