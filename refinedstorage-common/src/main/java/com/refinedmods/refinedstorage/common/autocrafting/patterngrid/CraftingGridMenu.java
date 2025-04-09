package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.DisabledSlot;
import com.refinedmods.refinedstorage.common.support.containermenu.FilterSlot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;

import static com.refinedmods.refinedstorage.common.autocrafting.patterngrid.PatternTypeSettings.getPatternType;

public class CraftingGridMenu extends AbstractMatrixMenu {
    private static final PatternType patternType = PatternType.CRAFTING;
    private final PatternGridContainerMenu menu;
    private static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT = 85;
    protected final Container craftingMatrix;
    protected final Container craftingResult;

    public CraftingGridMenu(
        final PatternGridContainerMenu menu,
        final MenuType<PatternGridContainerMenu> menuType,
        final int syncid) {
        super(menuType, syncid);
        this.menu = menu;
        this.craftingMatrix = new RecipeMatrixContainer(null, 3, 3);
        this.craftingResult = new ResultContainer();

        this.craftingMatrix = grid.getCraftingMatrix();
        this.craftingResult = grid.getCraftingResult();
    }

    public void createRenderer() {
        final PatternGridRenderer typeRenderer = patternType.createRenderer(
            menu,
            leftPos,
            topPos,
            getInsetX(),
            getInsetY()
        );
        if (type == getMenu().getPatternType()) {
            this.renderer = typeRenderer;
        }
        typeRenderer.addWidgets(this::addWidget, this::addRenderableWidget);
        renderers.put(type, typeRenderer);
    }

    @Override
    public void addMatrixSlots(final int playerInventoryY) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                final int slotX = 13 + ((x % 3) * 18);
                final int slotY = playerInventoryY
                    - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT
                    + ((y % 3) * 18);
                addSlot(new FilterSlot(craftingMatrix, x + y * 3, slotX, slotY) {
                    @Override
                    public boolean isActive() {
                        return getPatternType() == PatternType.CRAFTING;
                    }
                });
            }
        }
        addSlot(new DisabledSlot(
            craftingResult,
            0,
            117 + 4,
            playerInventoryY - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT + 18
        ) {
            @Override
            public boolean isActive() {
                return getPatternType() == PatternType.CRAFTING;
            }
        });
    }

    public void resized(final int playerInventoryY, final int topYStart, final int topYEnd) {
        addMatrixSlots(playerInventoryY);
    }

    public boolean isLargeSlot(final Slot slot) {
        return slot.container == craftingResult;
    }
}
