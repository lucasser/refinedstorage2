package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.common.api.grid.workstations.IWorkstationRegistryElement;
import com.refinedmods.refinedstorage.common.api.grid.workstations.MatrixMenuFactory;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class CraftingWorkstationRegistryElement implements
             IWorkstationRegistryElement<AbstractCraftingMatrix, CraftingGrid> {

    public final ResourceLocation workstationId;
    public final MatrixMenuFactory<? extends AbstractCraftingMatrix, CraftingGrid> craftingMatrixFactory;
    public final ItemStack icon;
    //public final MutableComponent translatedName;

    public CraftingWorkstationRegistryElement(final ResourceLocation workstationId,
                                              final MatrixMenuFactory<? extends AbstractCraftingMatrix, CraftingGrid> craftingMatrixFactory,
                                              final ItemStack icon) {
        this.workstationId = workstationId;
        this.craftingMatrixFactory = craftingMatrixFactory;
        this.icon = icon;
        //this.translatedName = createTranslation("misc", "pattern." + workstationName);
    }

    @Override
    public String getId() {
        return workstationId.toString();
    }

    public AbstractCraftingMatrix getCraftingMatrix(@Nullable final Runnable listener,
                                                    final Supplier<@NullableType Level> levelSupplier,
                                                    final CraftingGrid craftingGrid) {
        return craftingMatrixFactory.create(listener, levelSupplier, craftingGrid);
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }
}
