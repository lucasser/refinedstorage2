package com.refinedmods.refinedstorage.common.api.grid.workstations;

public interface IWorkstationRegistryElement<T, E> {
    String getID();

    T getCraftingMatrix();

    E getPatternMatrix();


}
