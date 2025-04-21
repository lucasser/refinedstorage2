package com.refinedmods.refinedstorage.common.api.grid.workstations;

import java.util.List;

public interface AbstractPatternMatrix<T extends AbstractMatrixMenu> extends AbstractBaseMatrix {

    default boolean canCreatePattern() {
        return false;
    }

    void transferRecipe(List<List<ItemResource>> recipe);

    <K extends MatrixRenderer> K createRenderer(
        T menu,
        int leftPos,
        int topPos,
        int x,
        int y);
}
