package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractGridResultSlot extends Slot {

    private int removeCount;
    private Player gridPlayer;

    AbstractGridResultSlot(final Player player,
                           final AbstractCraftingMatrix matrix,
                           final int x,
                           final int y) {
        super(matrix.getResult().get(), 0, x, y);
        this.gridPlayer = player;
    }

    public ItemStack onQuickCraft(final Player player) {
        final ItemStack singleResultStack = getItem().copy();
        final int maxCrafted = singleResultStack.getMaxStackSize();
        int crafted = 0;
        try (ExtractTransaction transaction = getInputMatrix().startExtractTransaction(player, false)) {
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
        try (ExtractTransaction transaction = getInputMatrix().startExtractTransaction(player, true)) {
            doTake(player, transaction, stack);
        }
    }

    protected abstract void doTake(Player player, ExtractTransaction transaction, ItemStack stack);

    protected void useIngredientWithRemainingItem(final Player player,
                                                final int index,
                                                final ItemStack remainingItem) {
        getInputMatrix().updateMatrixAndNotifyListenerLater(() -> {
            final ItemStack matrixStack = decrementMatrixSlot(index);
            if (matrixStack.isEmpty()) {
                getInputMatrix().getMatrix().setItem(index, remainingItem);
            } else if (ItemStack.isSameItemSameComponents(matrixStack, remainingItem)) {
                remainingItem.grow(matrixStack.getCount());
                getInputMatrix().getMatrix().setItem(index, remainingItem);
            } else if (!player.getInventory().add(remainingItem)) {
                player.drop(remainingItem, false);
            }
        });
    }

    protected void useIngredient(final Player player,
                               final ExtractTransaction transaction,
                               final int index,
                               final ItemStack matrixStack) {
        if (matrixStack.getCount() > 1 || !transaction.extract(ItemResource.ofItemStack(matrixStack), player)) {
            decrementMatrixSlot(index);
        }
    }

    protected ItemStack decrementMatrixSlot(final int index) {
        final Container matrix = getInputMatrix().getMatrix();
        matrix.removeItem(index, 1);
        return matrix.getItem(index);
    }

    protected abstract void fireCraftingEvents(Player player, ItemStack crafted);
    
    protected abstract AbstractCraftingMatrix getInputMatrix();

    public boolean isFake() {
        return true;
    }

    public boolean mayPlace(final ItemStack stack) {
        return false;
    }

    public ItemStack remove(final int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }
}
