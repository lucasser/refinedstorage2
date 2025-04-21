package com.refinedmods.refinedstorage.common.grid.crafting;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractMatrixMenu;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.DisabledSlot;
import com.refinedmods.refinedstorage.common.support.containermenu.FilterSlot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;

public class CraftingGridMenu extends AbstractMatrixMenu {
    private static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT = 85;
    public Container craftingMatrix;
    public Container craftingResult;

    public CraftingGridMenu(
        final MenuType<? extends AbstractGridContainerMenu> menuType,
        final int syncid) {
        super(menuType, syncid, "minecraft:crafting");
        this.craftingMatrix = new RecipeMatrixContainer(null, 3, 3);
        this.craftingResult = new ResultContainer();
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
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                final int slotX = 13 + ((x % 3) * 18);
                final int slotY = playerInventoryY
                    - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT
                    + ((y % 3) * 18);
                currentMenu.addSlot(new FilterSlot(craftingMatrix, x + y * 3, slotX, slotY) {
//                    @Override
//                    public boolean isActive() {
//                        return ((PatternGridContainerMenu) currentMenu).activeTab.matrix.getPatternType() == PatternType.CRAFTING;
//                    }
                });
            }
        }
        currentMenu.addSlot(new DisabledSlot(
            craftingResult,
            0,
            117 + 4,
            playerInventoryY - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT + 18
        ) {
//            @Override
//            public boolean isActive() {
//                return getPatternType() == PatternType.CRAFTING;
//            }
        });
    }

//    @Override
//    public MatrixRenderer createRenderer(final CraftingGridMenu menu, final int leftPos, final int topPos,
//                                         final int x, final int y) {
//        return new CraftingPatternGridRenderer(menu, leftPos, x, y);
//    }

    public void resized(final AbstractGridContainerMenu currentMenu, final int playerInventoryY, final int topYStart, final int topYEnd) {
        addMatrixSlots(currentMenu, playerInventoryY);
    }

    public boolean isLargeSlot(final Slot slot) {
        return slot.container == craftingResult;
    }
}
