package com.refinedmods.refinedstorage.common.api.grid.workstations;

public abstract class AbstractBaseMatrix {
    //public T matrix;

    protected AbstractBaseMatrix() {
        //this.matrix = matrix;
    }

    public abstract <T extends AbstractMatrixMenu> T getMatrix();

//    public abstract MatrixRenderer createRenderer(
//        <? extends AbstractGridContainerMenu> menu,
//        int leftPos,
//        int topPos,
//        int x,
//        int y);

    public abstract <T extends MatrixRenderer> T getRenderer();
}
