package com.refinedmods.refinedstorage.common.grid.crafting;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractCraftingMatrix;
import com.refinedmods.refinedstorage.common.grid.CraftingGridContainerMenu;
import com.refinedmods.refinedstorage.common.grid.crafting.renderer.StonecutterCraftingGridRenderer;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class StonecuttingCraftingGridMenu extends AbstractCraftingMatrix<StonecuttingGridMenu> {

    private final StonecuttingGridMenu matrix;
    private StonecutterCraftingGridRenderer renderer;

    public StonecuttingCraftingGridMenu(final Inventory inventory, final MenuType<CraftingGridContainerMenu> menuType, final Integer syncid) {
        matrix = new StonecuttingGridMenu(inventory, menuType, syncid);
    }

    @Override
    public StonecuttingGridMenu getMatrix() {
        return matrix;
    }

    @Override
    public StonecutterCraftingGridRenderer getRenderer() {
        return renderer;
    }

    @Override
    public StonecutterCraftingGridRenderer createRenderer(final StonecuttingGridMenu menu, final int leftPos,
                                                          final int topPos, final int x,
                                                          final int y) {
        renderer = new StonecutterCraftingGridRenderer(menu, leftPos, x, y);
        return renderer;
    }
}
