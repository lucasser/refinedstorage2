package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CraftingMatrix {

    protected static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT = 69;

    protected static final String WORKSTATION_TYPE = "crafting.crafting";

    private static final RecipeType<CraftingRecipe> RECIPE_TYPE = RecipeType.CRAFTING;

    protected final List<Slot> matrixSlots = new ArrayList<>();

    protected final WorkstationRecipeContainer<RecipeMatrixContainer, ResultContainer> recipeContainer;

    protected final Supplier<Level> levelSupplier;

    @Nullable
    protected RecipeHolder<CraftingRecipe> currentRecipe;

    public CraftingMatrix(@Nullable final Runnable listener, final Supplier<@NullableType Level> levelSupplier) {
        this.levelSupplier = levelSupplier;
        this.recipeContainer = new WorkstationRecipeContainer<>(
            listener,
            new RecipeMatrixContainer(this::inputChanged, 3, 3),
            new ResultContainer()
        );
    }

    protected void inputChanged() {
        if (getLevel() == null || getLevel().isClientSide() || recipeContainer.isMuted()) {
            return;
        }
        final CraftingInput input = recipeContainer.getInput().asCraftInput();
        if (currentRecipe == null || !currentRecipe.value().matches(input, getLevel())) {
            currentRecipe = loadRecipe(getLevel());
        }
        if (currentRecipe == null) {
            setResult(null, ItemStack.EMPTY);
        } else {
            setResult(currentRecipe, currentRecipe.value().assemble(input, getLevel().registryAccess()));
        }
        recipeContainer.changed();
    }

    private void setResult(@Nullable final RecipeHolder<?> recipe, final ItemStack result) {
        recipeContainer.getOutput().ifPresent(output -> {
            output.setRecipeUsed(recipe);
            output.setItem(0, result);
        });
    }

    @Nullable
    protected Level getLevel() {
        return levelSupplier.get();
    }

    @Nullable
    private RecipeHolder<CraftingRecipe> loadRecipe(final Level level) {
        return level
            .getRecipeManager()
            .getRecipeFor(RECIPE_TYPE, recipeContainer.getInput().asCraftInput(), level)
            .orElse(null);
    }

    //TODO: should actually look for remaining items. is it needed for patterns? if not move to CraftingCrafting
    protected NonNullList<ItemStack> getRemainingCraftingItems(final Player player, final CraftingInput input) {
        if (getLevel() == null
            || currentRecipe == null) {
            return NonNullList.create();
        }
        return Platform.INSTANCE.getRemainingCraftingItems(player, currentRecipe.value(), input);
    }
}
