package com.refinedmods.refinedstorage.common.api.grid.workstations;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

public class WorkstationRegistryData {
    public final Integer workstationId;
    public final String workstationName;
    public final MatrixMenuFactory<? extends AbstractCraftingMatrix<?>> craftingMatrix;
    public final MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternMatrix;
    public final ItemStack icon;
    public final MutableComponent translatedName;

    public WorkstationRegistryData(final String workstationName,
                                      final MatrixMenuFactory<? extends AbstractCraftingMatrix<?>> craftingMatrix,
                                      final MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternMatrix,
                                      final ItemStack icon) {
        this.workstationId = workstationName.hashCode();
        this.workstationName = workstationName;
        this.craftingMatrix = craftingMatrix;
        this.patternMatrix = patternMatrix;
        this.icon = icon;
        this.translatedName = createTranslation("misc", "pattern." + workstationName);
    }
}
