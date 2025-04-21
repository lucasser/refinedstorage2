package com.refinedmods.refinedstorage.common.grid.crafting;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractCraftingMatrix;
import com.refinedmods.refinedstorage.common.grid.CraftingGridContainerMenu;
import com.refinedmods.refinedstorage.common.grid.crafting.renderer.CraftingCraftingGridRenderer;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class CraftingCraftingGridMenu extends CraftingGridMenu implements AbstractCraftingMatrix<CraftingGridMenu> {

    private CraftingCraftingGridRenderer renderer;

    public CraftingCraftingGridMenu(final Inventory inventory, final MenuType<CraftingGridContainerMenu> menuType, final Integer syncid) {
        super(menuType, syncid);
    }

    @Override
    public CraftingCraftingGridRenderer createRenderer(final CraftingGridMenu menu, final int leftPos, final int topPos,
                                                       final int x, final int y) {
        renderer = new CraftingCraftingGridRenderer(menu, leftPos, x, y);
        return renderer;
    }

    @Override
    @Nullable
    public CraftingCraftingGridRenderer getRenderer() {
        return renderer;
    }
}
