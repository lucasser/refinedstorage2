package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import net.minecraft.world.inventory.MenuType;

public class CraftingPatternGridMenu extends AbstractPatternMatrix {

    public CraftingPatternGridMenu(final PatternGridContainerMenu menu,
                                   final MenuType<PatternGridContainerMenu> menuType,
                                   final int syncid) {
        super(new CraftingGridMenu(menu, menuType, syncid));
    }

    @Override
    public boolean canCreatePattern() {
        return !((CraftingGridMenu) matrix).craftingResult.getItem(0).isEmpty();
    }
}
