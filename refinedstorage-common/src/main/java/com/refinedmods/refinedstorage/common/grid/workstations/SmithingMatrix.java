package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

public class SmithingMatrix {
    protected static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_SMITHING_TABLE_SLOTS = 32;

    protected static final String WORKSTATION_TYPE = "crafting.smithing";

    private static final RecipeType<SmithingRecipe> RECIPE_TYPE = RecipeType.SMITHING;

    protected final List<Slot> matrixSlots = new ArrayList<>();

    protected final WorkstationRecipeContainer<RecipeMatrixContainer, ResultContainer> recipeContainer;

    protected final Supplier<Level> levelSupplier;

    protected final List<RecipeHolder<SmithingRecipe>> smithingTableRecipes;

    @Nullable
    protected RecipeHolder<SmithingRecipe> currentRecipe;

    public SmithingMatrix(@Nullable final Runnable listener, final Supplier<@NullableType Level> levelSupplier) {
        this.levelSupplier = levelSupplier;
        this.recipeContainer = new WorkstationRecipeContainer<>(
            listener,
            new RecipeMatrixContainer(this::inputChanged, 3, 1),
            new ResultContainer()
        );
        if (getLevel() != null) {
            this.smithingTableRecipes = getLevel().getRecipeManager()
                .getAllRecipesFor(RecipeType.SMITHING);
        } else {
            this.smithingTableRecipes = new ArrayList<>();
        }
    }

    protected void inputChanged() {
        if (getLevel() == null || getLevel().isClientSide() || recipeContainer.isMuted()) {
            return;
        }
        final SmithingRecipeInput input = getInputAsSmithingRecipe();
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
    private RecipeHolder<SmithingRecipe> loadRecipe(final Level level) {
        return level
            .getRecipeManager()
            .getRecipeFor(RECIPE_TYPE, getInputAsSmithingRecipe(), level)
            .orElse(null);
    }

    protected SmithingRecipeInput getInputAsSmithingRecipe() {
        final RecipeMatrixContainer inputContainer = recipeContainer.getInput();
        return new SmithingRecipeInput(inputContainer.getItem(0),
            inputContainer.getItem(1),
            inputContainer.getItem(2)
        );
    }

    //TODO: should actually look for remaining items. is it needed for patterns? if not move to CraftingCrafting
    protected NonNullList<ItemStack> getRemainingCraftingItems(final Player player, final CraftingInput input) {
        return NonNullList.create();
    }
}
