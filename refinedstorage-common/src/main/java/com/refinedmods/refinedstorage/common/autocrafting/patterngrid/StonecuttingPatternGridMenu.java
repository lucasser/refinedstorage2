package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractPatternMatrix;
import com.refinedmods.refinedstorage.common.grid.crafting.StonecuttingGridMenu;
import com.refinedmods.refinedstorage.common.api.grid.workstations.MatrixRenderer;
import com.refinedmods.refinedstorage.common.grid.crafting.renderer.StonecutterGridRenderer;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class StonecuttingPatternGridMenu extends AbstractPatternMatrix<StonecuttingGridMenu> {

    private final StonecuttingGridMenu matrix;
    private StonecutterPatternGridRenderer renderer;

    public StonecuttingPatternGridMenu(final Inventory inventory,
                                   final MenuType<PatternGridContainerMenu> menuType,
                                   final int syncid) {
        matrix = new StonecuttingGridMenu(inventory, menuType, syncid);
    }

    @Override
    boolean canCreatePattern() {
        return !matrix.stonecutterInput.getItem(0).isEmpty() && matrix.getStonecutterSelectedRecipe() >= 0;
    }

    @Override
    public StonecuttingGridMenu getMatrix() {
        return matrix;
    }

    @Override
    public MatrixRenderer createRenderer(final StonecuttingGridMenu menu, final int leftPos, final int topPos,
                                         final int x, final int y) {
        return new StonecutterGridRenderer(menu, leftPos, x, y);
    }

    @Override
    @Nullable
    public StonecutterPatternGridRenderer getRenderer() {
        return renderer;
    }
}
