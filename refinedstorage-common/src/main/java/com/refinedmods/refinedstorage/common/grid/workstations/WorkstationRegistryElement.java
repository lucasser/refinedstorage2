package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.api.grid.workstations.IWorkstationRegistryElement;
import com.refinedmods.refinedstorage.common.api.grid.workstations.MatrixMenuFactory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;


public class WorkstationRegistryElement implements IWorkstationRegistryElement<AbstractCraftingMatrix> {
    public final ResourceLocation workstationId;
    public final MatrixMenuFactory<? extends AbstractCraftingMatrix> craftingMatrix;
    public final ItemStack icon;
    //public final MutableComponent translatedName;

    public WorkstationRegistryElement(final ResourceLocation workstationId,
                                      final MatrixMenuFactory<? extends AbstractCraftingMatrix> craftingMatrix,
                                      final ItemStack icon) {
        this.workstationId = workstationId;
        this.craftingMatrix = craftingMatrix;
        this.icon = icon;
        //this.translatedName = createTranslation("misc", "pattern." + workstationName);
    }

    @Override
    public String getId() {
        return workstationId.toString();
    }

    @Override
    public AbstractCraftingMatrix getCraftingMatrix() {
        return craftingMatrix.create();
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }
}
