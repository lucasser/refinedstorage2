package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.common.support.AbstractBaseContainerMenu;

import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMatrixMenu extends AbstractBaseContainerMenu {

    @Nullable
    PatternType patternType = null;

    public AbstractMatrixMenu(@Nullable final MenuType<?> type,
                              final int syncId) {
        super(type, syncId);
    }

    public void addMatrixSlots(final int playerInventoryY) {
    }

    public PatternType getPatternType() {
        return patternType;
    }
}
