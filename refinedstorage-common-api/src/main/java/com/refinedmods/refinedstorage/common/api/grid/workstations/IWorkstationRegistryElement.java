package com.refinedmods.refinedstorage.common.api.grid.workstations;

import net.minecraft.world.item.ItemStack;

//TODO: add pattern as well
public interface IWorkstationRegistryElement<T> {
    String getId();

    T getCraftingMatrix();

    ItemStack getIcon();
}
