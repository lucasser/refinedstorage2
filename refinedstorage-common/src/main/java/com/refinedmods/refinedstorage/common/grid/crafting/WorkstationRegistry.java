package com.refinedmods.refinedstorage.common.grid.crafting;

import com.refinedmods.refinedstorage.common.api.grid.workstations.MenuPairFactory;
import com.refinedmods.refinedstorage.common.autocrafting.patterngrid.CraftingPatternGridMenu;
import com.refinedmods.refinedstorage.common.autocrafting.patterngrid.StonecuttingPatternGridMenu;

import net.minecraft.world.item.Items;

public class WorkstationRegistry {
    public void register() {
        WorkstationMenuRegistry.register("minecraft:crafting", new MenuPairFactory(
            CraftingPatternGridMenu::new,
            CraftingCraftingGridMenu::new),
            Items.CRAFTING_TABLE.getDefaultInstance());
        WorkstationMenuRegistry.register("minecraft:stonecutting", new MenuPairFactory(
            StonecuttingPatternGridMenu::new,
            StonecuttingCraftingGridMenu::new),
            Items.STONECUTTER.getDefaultInstance());
    }
}
