package com.refinedmods.refinedstorage.common.api.grid.workstations;

public abstract class AbstractCraftingMatrix<T extends AbstractMatrixMenu> extends AbstractBaseMatrix {

    protected AbstractCraftingMatrix() {
    }

    public abstract <K extends MatrixRenderer> K createRenderer(
        T menu, int leftPos, int topPos,
        int x, int y);
}
