package com.refinedmods.refinedstorage.common.api.grid.workstations;

public interface AbstractCraftingMatrix<T extends AbstractMatrixMenu> extends AbstractBaseMatrix {

    <K extends MatrixRenderer> K createRenderer(
        T menu, int leftPos, int topPos,
        int x, int y);
}
