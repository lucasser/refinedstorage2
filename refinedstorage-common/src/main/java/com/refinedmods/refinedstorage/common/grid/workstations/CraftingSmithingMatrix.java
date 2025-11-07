package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;
import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;
import com.refinedmods.refinedstorage.common.support.containermenu.FilterSlot;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

public class CraftingSmithingMatrix extends SmithingMatrix implements AbstractCraftingMatrix {

    private static final ResourceLocation INSERT_TEXTURE =
        createIdentifier("textures/gui/workstations/smithing_matrix.png");

    @Nullable
    private SmithingGridResultSlot resultSlot;

    private final CraftingGrid craftingGrid;

    public CraftingSmithingMatrix(@Nullable final Runnable listener,
                                  final Supplier<@NullableType Level> levelSupplier,
                                  final CraftingGrid craftingGrid) {
        super(listener, levelSupplier);
        this.craftingGrid = craftingGrid;
    }

    @Override
    public ResourceLocation getInsert() {
        return INSERT_TEXTURE;
    }

    @Override
    public int getInsertHeight() {
        return 58;
    }

    @Override
    public List<Slot> getMatrixSlots() {
        final List<Slot> slots = new ArrayList<>(matrixSlots);
        slots.add(resultSlot);
        return slots;
    }

    @Override
    public void renderSlots(final AbstractGridContainerMenu menu,
                            final Player gridPlayer,
                            final int playerInventoryY,
                            final int topYStart,
                            final int topYEnd) {
        matrixSlots.clear();
        final int y = playerInventoryY - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_SMITHING_TABLE_SLOTS;
        for (int i = 0; i < 3; ++i) {
            final int ii = i;
            matrixSlots.add(new FilterSlot(getMatrix(), i, 8 + (i * 18), y) {
                @Override
                public boolean mayPlace(final ItemStack stack) {
                    return smithingTableRecipes.stream().anyMatch(recipe -> switch (ii) {
                        case 0 -> recipe.value().isTemplateIngredient(stack);
                        case 1 -> recipe.value().isBaseIngredient(stack);
                        case 2 -> recipe.value().isAdditionIngredient(stack);
                        default -> false;
                    });
                }
            });
        }
        resultSlot = new SmithingGridResultSlot(gridPlayer, this, 98, y);
    }

    @Override
    public RecipeMatrixContainer getMatrix() {
        return recipeContainer.getInput();
    }

    @Override
    public Optional<Container> getResult() {
        return recipeContainer.getOutput()
            .map(output -> output);
    }

    @Override
    public String getWorkstationType() {
        return WORKSTATION_TYPE;
    }

    @Override
    public CompoundTag writeToTag(final HolderLookup.Provider provider) {
        return recipeContainer.writeToTag(provider);
    }

    @Override
    public void readFromTag(final CompoundTag tag, final HolderLookup.Provider provider) {
        recipeContainer.readFromTag(tag, provider);
    }

    @Override
    public void changed() {
        inputChanged();
    }

    @Override
    public void transferRecipe(final Player player, @Nullable final RootStorage rootStorage,
                               final List<List<ItemResource>> recipe) {
        recipeContainer.transferRecipe(player, rootStorage, recipe);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final Player player, final CraftingInput input) {
        return getRemainingCraftingItems(player, input);
    }

    @Override
    public ExtractTransaction startExtractTransaction(final Player player, final boolean b) {
        return craftingGrid.startExtractTransaction(player, b);
    }

    @Override
    public boolean clearToPlayerInventory(final Player player) {
        return recipeContainer.clearToPlayerInventory(player);
    }

    @Override
    public boolean clearIntoStorage(final RootStorage storage, final Player player) {
        return recipeContainer.clearIntoStorage(storage, player);
    }

    public void updateMatrixAndNotifyListenerLater(final Runnable runnable) {
        recipeContainer.updateMatrixAndNotifyListenerLater(runnable);
    }
}
