package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public class SmithingGridResultSlot extends AbstractGridResultSlot {

    private final CraftingSmithingMatrix inputMatrix;

    SmithingGridResultSlot(final Player player, final CraftingSmithingMatrix matrix,
                           final int x, final int y) {
        super(player, matrix, x, y);
        this.inputMatrix = matrix;
    }

    @Override
    protected void doTake(final Player player, final ExtractTransaction transaction, final ItemStack stack) {
        fireCraftingEvents(player, stack.copy());
        final CraftingInput.Positioned positioned = getInputMatrix().getMatrix().asPositionedCraftInput();
        final CraftingInput input = positioned.input();
        final int left = positioned.left();
        final int top = positioned.top();
        final NonNullList<ItemStack> remainingItems = getInputMatrix().getRemainingItems(player, input);
        for (int y = 0; y < input.height(); ++y) {
            for (int x = 0; x < input.width(); ++x) {
                final int index = x + left + (y + top) * getInputMatrix().getMatrix().getWidth();
                final ItemStack matrixStack = getInputMatrix().getMatrix().getItem(index);
                final int recipeIndex = x + y * input.width();
                final ItemStack remainingItem = remainingItems.get(recipeIndex);
                if (!remainingItem.isEmpty()) {
                    useIngredientWithRemainingItem(player, index, remainingItem);
                } else if (!matrixStack.isEmpty()) {
                    useIngredient(player, transaction, index, matrixStack);
                }
            }
        }
        getInputMatrix().changed();
    }

    //TODO: needs to be different depending on recipe type
    @Override
    protected void fireCraftingEvents(final Player player, final ItemStack crafted) {
        // reimplementation of checkTakeAchievements
        crafted.onCraftedBy(player.level(), player, crafted.getCount());
        Platform.INSTANCE.onItemCrafted(player, crafted, getInputMatrix().getMatrix());
        if (container instanceof RecipeCraftingHolder recipeHolder) {
            recipeHolder.awardUsedRecipes(player, getRelevantItems());
        }
    }

    private List<ItemStack> getRelevantItems() {
        return List.of(inputMatrix.getMatrix().getItem(0),
            inputMatrix.getMatrix().getItem(1),
            inputMatrix.getMatrix().getItem(2)
        );
    }

    @Override
    protected CraftingSmithingMatrix getInputMatrix() {
        return inputMatrix;
    }
}
