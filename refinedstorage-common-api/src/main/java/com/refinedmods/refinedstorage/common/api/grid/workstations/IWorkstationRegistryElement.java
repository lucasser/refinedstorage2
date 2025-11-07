package com.refinedmods.refinedstorage.common.api.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

//TODO: add pattern as well
public interface IWorkstationRegistryElement<T, E> {
    String getId();

    T getCraftingMatrix(@Nullable Runnable listener,
                        Supplier<@NullableType Level> levelSupplier,
                        E parent);

    ItemStack getIcon();
}
