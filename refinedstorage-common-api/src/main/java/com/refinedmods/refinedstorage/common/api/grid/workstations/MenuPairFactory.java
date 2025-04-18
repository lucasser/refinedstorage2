package com.refinedmods.refinedstorage.common.api.grid.workstations;


public record MenuPairFactory(
    MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternFactory,
    MatrixMenuFactory<? extends AbstractCraftingMatrix<?>> craftingFactory
) { }
