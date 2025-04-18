package com.refinedmods.refinedstorage.common.api.grid.workstations;

import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;

import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMatrixMenu extends AbstractBaseContainerMenu {

    @Nullable
    String recipeType;

    public AbstractMatrixMenu(@Nullable final MenuType<?> type,
                              final int syncId, final String patternType) {
        super(type, syncId);
        this.recipeType = patternType;
    }

    public void addMatrixSlots(final AbstractGridContainerMenu menu, final int playerInventoryY) {
    }

    public String getRecipeType() {
        return (recipeType == null) ? "" : recipeType;
    }
}
