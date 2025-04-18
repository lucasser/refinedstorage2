package com.refinedmods.refinedstorage.common.api.grid.workstations;

import java.util.List;

public abstract class AbstractPatternMatrix<T extends AbstractMatrixMenu> extends AbstractBaseMatrix {

    protected AbstractPatternMatrix() {
    }

    boolean canCreatePattern() {
        return false;
    }

    public void transferRecipe(final List<List<ItemResource>> recipe) {
    }

    public abstract <K extends MatrixRenderer> K createRenderer(
        T menu,
        int leftPos,
        int topPos,
        int x,
        int y);
}
