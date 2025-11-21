package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.api.storage.PlayerActor;
import com.refinedmods.refinedstorage.common.support.network.ResourceSorters;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;
import com.refinedmods.refinedstorage.common.util.ContainerUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WorkstationRecipeContainer<T extends Container, E extends Container> {
    private static final String TAG_INPUT = "input_container";
    private static final String TAG_OUTPUT = "output_container";

    @Nullable
    private final Runnable listener;
    private final T inputContainer;
    @Nullable
    private final E outputContainer;
    private boolean muted;

    public WorkstationRecipeContainer(@Nullable final Runnable listener,
                                      final T inputContainer,
                                      @Nullable final E outputContainer) {
        this.listener = listener;
        this.inputContainer = inputContainer;
        this.outputContainer = outputContainer;
    }


    public T getInput() {
        return inputContainer;
    }

    public Optional<E> getOutput() {
        return Optional.ofNullable(outputContainer);
    }

    public void changed() {
        if (listener != null) {
            listener.run();
        }
    }

    public boolean clearToPlayerInventory(final Player player) {
        boolean clearedAll = true;
        for (int i = 0; i < getInput().getContainerSize(); ++i) {
            final ItemStack matrixStack = getInput().getItem(i);
            if (matrixStack.isEmpty()) {
                continue;
            }
            if (player.getInventory().add(matrixStack)) {
                getInput().setItem(i, ItemStack.EMPTY);
            } else {
                clearedAll = false;
            }
        }
        return clearedAll;
    }

    public boolean clearIntoStorage(final RootStorage rootStorage, final Player player) {
        boolean clearedAll = true;
        for (int i = 0; i < getInput().getContainerSize(); ++i) {
            final ItemStack matrixStack = getInput().getItem(i);
            if (matrixStack.isEmpty()) {
                continue;
            }
            final ItemStack remainder = doInsert(matrixStack, player, rootStorage);
            if (!remainder.isEmpty()) {
                clearedAll = false;
            }
            getInput().setItem(i, remainder);
        }
        return clearedAll;
    }

    private ItemStack doInsert(final ItemStack stack,
                               final Player player,
                               final RootStorage rootStorage) {
        final long inserted = rootStorage.insert(
            ItemResource.ofItemStack(stack),
            stack.getCount(),
            Action.EXECUTE,
            new PlayerActor(player)
        );
        final long remainder = stack.getCount() - inserted;
        if (remainder == 0) {
            return ItemStack.EMPTY;
        }
        return stack.copyWithCount((int) remainder);
    }

    public void transferRecipe(final Player player,
                               @Nullable final RootStorage rootStorage,
                               final List<List<ItemResource>> recipe) {
        final boolean cleared = rootStorage == null
            ? clearToPlayerInventory(player)
            : clearIntoStorage(rootStorage, player);
        if (!cleared) {
            return;
        }
        final Comparator<ResourceKey> sorter = ResourceSorters.create(rootStorage, player.getInventory());
        for (int i = 0; i < getInput().getContainerSize(); ++i) {
            if (i >= recipe.size()) {
                break;
            }
            final List<ItemResource> possibilities = recipe.get(i);
            possibilities.sort(sorter);
            doTransferRecipe(i, possibilities, player, rootStorage);
        }
    }

    private void doTransferRecipe(final int index,
                                  final List<ItemResource> sortedPossibilities,
                                  final Player player,
                                  @Nullable final RootStorage rootStorage) {
        for (final ItemResource possibility : sortedPossibilities) {
            boolean extracted = rootStorage != null
                && rootStorage.extract(possibility, 1, Action.EXECUTE, new PlayerActor(player)) == 1;
            if (!extracted) {
                extracted = extractSingleItemFromPlayerInventory(player, possibility);
            }
            if (extracted) {
                getInput().setItem(index, possibility.toItemStack());
                return;
            }
        }
    }

    private boolean extractSingleItemFromPlayerInventory(final Player player, final ItemResource possibility) {
        final ItemStack possibilityStack = possibility.toItemStack();
        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            final ItemStack playerStack = player.getInventory().getItem(i);
            if (ItemStack.isSameItemSameComponents(playerStack, possibilityStack)) {
                player.getInventory().removeItem(i, 1);
                return true;
            }
        }
        return false;
    }

    public void updateMatrixAndNotifyListenerLater(final Runnable callback) {
        muted = true;
        try {
            callback.run();
        } finally {
            muted = false;
            changed();
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public CompoundTag writeToTag(final HolderLookup.Provider provider) {
        final CompoundTag tag = new CompoundTag();
        tag.put(TAG_INPUT, ContainerUtil.write(inputContainer, provider));
        if (outputContainer != null) {
            tag.put(TAG_OUTPUT, ContainerUtil.write(outputContainer, provider));
        }
        return tag;
    }

    public void readFromTag(final CompoundTag tag, final HolderLookup.Provider provider) {
        if (tag.contains(TAG_INPUT)) {
            ContainerUtil.read(tag.getCompound(TAG_INPUT), inputContainer, provider);
        }
        if (tag.contains(TAG_OUTPUT) && outputContainer != null /*Should never happen*/) {
            ContainerUtil.read(tag.getCompound(TAG_OUTPUT), outputContainer, provider);
        }
    }
}
