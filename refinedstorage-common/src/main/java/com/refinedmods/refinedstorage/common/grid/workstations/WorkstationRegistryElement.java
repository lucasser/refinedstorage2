package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.api.grid.workstations.IWorkstationRegistryElement;
import com.refinedmods.refinedstorage.common.api.grid.workstations.MatrixMenuFactory;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

public class WorkstationRegistryElement implements IWorkstationRegistryElement {
    public final ResourceLocation workstationId;
    public final MatrixMenuFactory<? extends AbstractCraftingMatrix> craftingMatrix;
    //public final MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternMatrix;
    public final ItemStack icon;
    //public final MutableComponent translatedName;

    public WorkstationRegistryElement(final ResourceLocation workstationId,
                                      final MatrixMenuFactory<? extends AbstractCraftingMatrix> craftingMatrix,
                                   /*final MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternMatrix,*/
                                   final ItemStack icon) {
        this.workstationId = workstationId;
        this.craftingMatrix = craftingMatrix;
        this.icon = icon;
        //this.translatedName = createTranslation("misc", "pattern." + workstationName);
    }

    @Override
    public String getID() {
        return "";
    }

    @Override
    public Object getCraftingMatrix() {
        return null;
    }

    @Override
    public Object getPatternMatrix() {
        return null;
    }
}
