package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public class GridResultSlot extends ResultSlot {
    private final CraftingCraftingMatrix inputMatrix;

    GridResultSlot(final Player player,
                             final CraftingCraftingMatrix matrix,
                             final int x,
                             final int y) {
        super(player, matrix.getMatrix(), matrix.getResult().get(), 0, x, y);
        this.inputMatrix = matrix;
    }

    public ItemStack onQuickCraft(final Player player) {
        final ItemStack singleResultStack = getItem().copy();
        final int maxCrafted = singleResultStack.getMaxStackSize();
        int crafted = 0;
        try (ExtractTransaction transaction = inputMatrix.startExtractTransaction(player, false)) {
            while (ItemStack.isSameItemSameComponents(singleResultStack, getItem()) && crafted < maxCrafted) {
                doTake(player, transaction, singleResultStack);
                crafted += singleResultStack.getCount();
            }
        }
        return singleResultStack.copyWithCount(crafted);
    }

    @Override
    @SuppressWarnings("resource")
    public void onTake(final Player player, final ItemStack stack) {
        if (player.level().isClientSide()) {
            return;
        }
        try (ExtractTransaction transaction = inputMatrix.startExtractTransaction(player, true)) {
            doTake(player, transaction, stack);
        }
    }

    private void doTake(final Player player, final ExtractTransaction transaction, final ItemStack stack) {
        fireCraftingEvents(player, stack.copy());
        final CraftingInput.Positioned positioned = inputMatrix.getMatrix().asPositionedCraftInput();
        final CraftingInput input = positioned.input();
        final int left = positioned.left();
        final int top = positioned.top();
        final NonNullList<ItemStack> remainingItems = inputMatrix.getRemainingItems(player, input);
        for (int y = 0; y < input.height(); ++y) {
            for (int x = 0; x < input.width(); ++x) {
                final int index = x + left + (y + top) * inputMatrix.getMatrix().getWidth();
                final ItemStack matrixStack = inputMatrix.getMatrix().getItem(index);
                final int recipeIndex = x + y * input.width();
                final ItemStack remainingItem = remainingItems.get(recipeIndex);
                if (!remainingItem.isEmpty()) {
                    useIngredientWithRemainingItem(player, index, remainingItem);
                } else if (!matrixStack.isEmpty()) {
                    useIngredient(player, transaction, index, matrixStack);
                }
            }
        }
        inputMatrix.changed();
    }

    private void useIngredientWithRemainingItem(final Player player,
                                                final int index,
                                                final ItemStack remainingItem) {
        inputMatrix.updateMatrixAndNotifyListenerLater(() -> {
            final ItemStack matrixStack = decrementMatrixSlot(index);
            if (matrixStack.isEmpty()) {
                inputMatrix.getMatrix().setItem(index, remainingItem);
            } else if (ItemStack.isSameItemSameComponents(matrixStack, remainingItem)) {
                remainingItem.grow(matrixStack.getCount());
                inputMatrix.getMatrix().setItem(index, remainingItem);
            } else if (!player.getInventory().add(remainingItem)) {
                player.drop(remainingItem, false);
            }
        });
    }

    private void useIngredient(final Player player,
                               final ExtractTransaction transaction,
                               final int index,
                               final ItemStack matrixStack) {
        if (matrixStack.getCount() > 1 || !transaction.extract(ItemResource.ofItemStack(matrixStack), player)) {
            decrementMatrixSlot(index);
        }
    }

    private ItemStack decrementMatrixSlot(final int index) {
        final Container matrix = inputMatrix.getMatrix();
        matrix.removeItem(index, 1);
        return matrix.getItem(index);
    }

    //TODO: needs to be different depending on recipe type
    private void fireCraftingEvents(final Player player, final ItemStack crafted) {
        // reimplementation of checkTakeAchievements
        crafted.onCraftedBy(player.level(), player, crafted.getCount());
        Platform.INSTANCE.onItemCrafted(player, crafted, inputMatrix.getMatrix());
        if (container instanceof RecipeCraftingHolder recipeHolder) {
            recipeHolder.awardUsedRecipes(player, List.of(crafted));
        }
    }
}
