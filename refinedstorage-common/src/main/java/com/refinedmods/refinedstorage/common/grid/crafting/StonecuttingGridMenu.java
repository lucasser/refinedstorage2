package com.refinedmods.refinedstorage.common.grid.crafting;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractMatrixMenu;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.grid.crafting.helpers.StonecutterInputContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.FilterSlot;

import java.util.List;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;

public class StonecuttingGridMenu extends AbstractMatrixMenu {
    private static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_STONECUTTER_SLOT = 63;
    public final StonecutterInputContainer stonecutterInput;

    public StonecuttingGridMenu(
        final Inventory playerInventory,
        final MenuType<? extends AbstractGridContainerMenu> menuType,
        final int syncid) {
        super(menuType, syncid, "minecraft:stonecutter");
        this.stonecutterInput = new StonecutterInputContainer(playerInventory.player::level);
//
//        this.craftingMatrix = grid.getCraftingMatrix();
//        this.craftingResult = grid.getCraftingResult();
    }

//    public void createRenderer() {
//        final MatrixRenderer typeRenderer = patternType.createRenderer(
//            menu,
//            leftPos,
//            topPos,
//            getInsetX(),
//            getInsetY()
//        );
//        if (type == getMenu().getPatternType()) {
//            this.renderer = typeRenderer;
//        }
//        typeRenderer.addWidgets(this::addWidget, this::addRenderableWidget);
//        renderers.put(type, typeRenderer);
//    }

    @Override
    public void addMatrixSlots(final AbstractGridContainerMenu currentMenu, final int playerInventoryY) {
        final int slotY = playerInventoryY - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_STONECUTTER_SLOT;
        currentMenu.addSlot(new FilterSlot(stonecutterInput, 0, 13, slotY) {
//            @Override
//            public boolean isActive() {
//                return getPatternType() == PatternType.STONECUTTER;
//            }
        });
    }

    public void resized(final AbstractGridContainerMenu currentMenu, final int playerInventoryY, final int topYStart, final int topYEnd) {
        addMatrixSlots(currentMenu, playerInventoryY);
    }

    public boolean isLargeSlot(final Slot slot) {
        return false;
    }

    public List<RecipeHolder<StonecutterRecipe>> getStonecutterRecipes() {
        return stonecutterInput.getRecipes();
    }

    public int getStonecutterSelectedRecipe() {
        return 0;
        //return getProperty(PatternGridPropertyTypes.STONECUTTER_SELECTED_RECIPE).getValue();
    }

    public void setStonecutterSelectedRecipe(final int idx) {
        //getProperty(PatternGridPropertyTypes.STONECUTTER_SELECTED_RECIPE).setValue(idx);
    }
}
