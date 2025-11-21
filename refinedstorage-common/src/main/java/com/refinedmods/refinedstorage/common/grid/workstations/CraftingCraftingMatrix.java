package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.grid.CraftingGrid;
import com.refinedmods.refinedstorage.common.grid.ExtractTransaction;
import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

public class CraftingCraftingMatrix extends CraftingMatrix implements AbstractCraftingMatrix {

    private static final ResourceLocation INSERT_TEXTURE =
        createIdentifier("textures/gui/workstations/crafting_matrix.png");

    @Nullable
    private CraftingGridResultSlot resultSlot;

    @Nullable
    private CraftingGrid craftingGrid;

    public CraftingCraftingMatrix(@Nullable final Runnable listener,
                                  final Supplier<@NullableType Level> levelSupplier) {
        super(listener, levelSupplier);
    }

    @Override
    public void setCraftingGrid(final CraftingGrid craftingGrid) {
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
    public void prepRenderers(final Player gridPlayer,
                              final int playerInventoryY,
                              final int topYStart,
                              final int topYEnd) {
        prepSlots(gridPlayer, playerInventoryY, topYStart, topYEnd);
    }

    private void prepSlots(final Player gridPlayer,
                           final int playerInventoryY,
                           final int topYStart,
                           final int topYEnd) {
        matrixSlots.clear();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                final int slotX = 26 + ((x % 3) * 18);
                final int slotY = playerInventoryY
                    - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT
                    + ((y % 3) * 18);
                matrixSlots.add(new Slot(getMatrix(), x + y * 3, slotX, slotY) {
                    @Override
                    public boolean isActive() {
                        return active;
                    }
                });
            }
        }
        resultSlot = new CraftingGridResultSlot(
            gridPlayer,
            this,
            130 + 4,
            playerInventoryY - Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_FIRST_CRAFTING_MATRIX_SLOT + 18
        ) {
            @Override
            public boolean isActive() {
                return active;
            }
        };
    }

    @Override
    public void render(final AbstractContainerMenu menu,
                       final GuiGraphics graphics, final float partialTicks,
                       final int mouseX, final int mouseY, final int topX, final int topY, final int insertY) {

    }

    @Override
    public void renderTooltip(final Font font, @org.jetbrains.annotations.Nullable final Slot hoveredSlot,
                              final GuiGraphics graphics,
                              final int mouseX, final int mouseY) {

    }

    @Override
    public void addWidgets(final Consumer<AbstractWidget> widgets, final Consumer<AbstractWidget> renderables) {

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
    public ResourceLocation getWorkstationType() {
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
        return Objects.requireNonNull(craftingGrid).startExtractTransaction(player, b);
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

    @Override
    public void tick() {
    }

    @Override
    public void levelChanged() {
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }
}
