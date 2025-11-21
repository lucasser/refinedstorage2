package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractMatrix;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;

public interface AbstractCraftingMatrix extends AbstractMatrix {

    void setCraftingGrid(CraftingGrid craftingGrid);

    NonNullList<ItemStack> getRemainingItems(Player player, CraftingInput input);

    ExtractTransaction startExtractTransaction(Player player, boolean b);

    boolean clearToPlayerInventory(Player player);

    boolean clearIntoStorage(RootStorage storage, Player player);

    void transferRecipe(Player player,
                        @Nullable RootStorage rootStorage,
                        List<List<ItemResource>> recipe);
}
