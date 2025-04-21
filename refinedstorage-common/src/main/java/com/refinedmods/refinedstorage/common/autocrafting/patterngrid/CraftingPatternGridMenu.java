package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractPatternMatrix;
import com.refinedmods.refinedstorage.common.grid.crafting.CraftingGridMenu;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;

public class CraftingPatternGridMenu extends CraftingGridMenu implements AbstractPatternMatrix<CraftingGridMenu> {

    @Nullable
    private CraftingPatternGridRenderer renderer;

    public CraftingPatternGridMenu(final Inventory inventory, final MenuType<PatternGridContainerMenu> menuType, final Integer syncid) {
        super(menuType, syncid);
    }

    @Override
    boolean canCreatePattern() {
        return this.craftingResult.getItem(0).isEmpty();
    }

    @Override
    public void transferRecipe(final List<List<ItemResource>> recipe) {

    }

    @Override
    public CraftingPatternGridRenderer createRenderer(final CraftingPatternGridMenu menu, final int leftPos, final int topPos,
                                         final int x, final int y) {
        renderer = new CraftingPatternGridRenderer(menu, leftPos, x, y);
        return renderer;
    }

    @Override
    @Nullable
    public CraftingPatternGridRenderer getRenderer() {
        return renderer;
    }
}
