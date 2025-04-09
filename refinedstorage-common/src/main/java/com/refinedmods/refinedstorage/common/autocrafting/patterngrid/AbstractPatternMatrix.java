package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

public abstract class AbstractPatternMatrix {

    public AbstractMatrixMenu matrix;

    AbstractPatternMatrix(final AbstractMatrixMenu matrix) {
        this.matrix = matrix;
    }

    boolean canCreatePattern() {
        return false;
    }
}
