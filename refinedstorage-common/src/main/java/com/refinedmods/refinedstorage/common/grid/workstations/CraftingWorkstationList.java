package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class CraftingWorkstationList extends ArrayList<AbstractCraftingMatrix> {

    public AbstractCraftingMatrix getById(final ResourceLocation id) {
        return this.stream()
            .filter(matrix -> matrix.getWorkstationType().equals(id))
            .findFirst()
            .orElseThrow(() ->
                new NoSuchElementException("Workstation '" + id + "' not found"));
    }

    @Override
    public boolean add(final AbstractCraftingMatrix t) {
        return super.add(t);
    }

    public AbstractCraftingMatrix getByIdOrMakeNew(final ResourceLocation id,
                              @Nullable final Runnable listener,
                              final Supplier<Level> levelSupplier) {
        return this.stream()
            .filter(matrix -> matrix.getWorkstationType().equals(id))
            .findFirst()
            .orElseGet(() -> {
                final AbstractCraftingMatrix matrix = (AbstractCraftingMatrix) RefinedStorageApi.INSTANCE
                    .getCraftingWorkstationRegistry()
                    .get(id)
                    .map(factory -> factory.create(listener, levelSupplier))
                    .orElseThrow(() ->
                        new NoSuchElementException("Workstation '" + id + "' not found"));
                this.add(matrix);
                return matrix;
            });
    }

    public void initialize(@Nullable final Runnable listener,
                           final Supplier<Level> levelSupplier) {
        RefinedStorageApi.INSTANCE.getCraftingWorkstationRegistry().getAllIds().forEach(id -> {
            getByIdOrMakeNew(id, listener, levelSupplier);
        });
    }
}
