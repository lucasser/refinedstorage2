package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.api.autocrafting.calculation.CancellationToken;
import com.refinedmods.refinedstorage.api.autocrafting.preview.Preview;
import com.refinedmods.refinedstorage.api.autocrafting.preview.TreePreview;
import com.refinedmods.refinedstorage.api.autocrafting.task.TaskId;
import com.refinedmods.refinedstorage.api.network.node.grid.GridOperations;
import com.refinedmods.refinedstorage.api.network.node.grid.GridWatcher;
import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.storage.Actor;
import com.refinedmods.refinedstorage.api.storage.Storage;
import com.refinedmods.refinedstorage.api.storage.TrackedResourceAmount;
import com.refinedmods.refinedstorage.common.api.support.resource.PlatformResourceKey;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceType;
import com.refinedmods.refinedstorage.common.grid.workstations.AbstractCraftingMatrix;
import com.refinedmods.refinedstorage.common.grid.workstations.CraftingWorkstationList;
import com.refinedmods.refinedstorage.common.support.packet.c2s.C2SPackets;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;

class ClientCraftingGrid implements CraftingGrid {

    private final CraftingWorkstationList matrixList = new CraftingWorkstationList();

    @Nullable
    private AbstractCraftingMatrix activeMatrix;

    private final Supplier<Level> levelSupplier;

    ClientCraftingGrid(final Supplier<Level> levelSupplier) {
        this.levelSupplier = levelSupplier;
        matrixList.initialize(null, levelSupplier);
    }

    @Override
    public AbstractCraftingMatrix getActiveMatrix() {
        return activeMatrix;
    }

    @Override
    public CraftingWorkstationList getMatrixList() {
        return matrixList;
    }

    //TODO: problem
    @Override
    public Optional<Container> getResult() {
        return activeMatrix.getResult();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final Player player, final CraftingInput input) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ExtractTransaction startExtractTransaction(final Player player, final boolean directCommit) {
        return ExtractTransaction.NOOP;
    }

    @Override
    public boolean clearMatrix(final Player player, final boolean toPlayerInventory) {
        C2SPackets.sendCraftingGridClear(toPlayerInventory);
        return true;
    }

    @Override
    public void transferRecipe(final Player player, final List<List<ItemResource>> recipe) {
        C2SPackets.sendCraftingGridRecipeTransfer(recipe);
    }

    @Override
    public void acceptQuickCraft(final Player player, final ItemStack craftedStack) {
        throw new UnsupportedOperationException();
    }

    //TODO: delete slots when switching?
    @Override
    public void setActiveMatrix(final ResourceLocation workstationid) {
        if (activeMatrix != null) {
            activeMatrix.setActive(false);
        }
        activeMatrix = matrixList.getById(workstationid);
        activeMatrix.setCraftingGrid(this);
        activeMatrix.setActive(true);
        activeMatrix.changed();
        activeMatrix.levelChanged();
        C2SPackets.sendCraftingGridWorkstationChange(workstationid);
    }

    @Override
    public void addWatcher(final GridWatcher watcher, final Class<? extends Actor> actorType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeWatcher(final GridWatcher watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Storage getItemStorage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGridActive() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TrackedResourceAmount> getResources(final Class<? extends Actor> actorType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<PlatformResourceKey> getAutocraftableResources() {
        throw new UnsupportedOperationException();
    }

    @Override
    public GridOperations createOperations(final ResourceType resourceType, final ServerPlayer player) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean canMenuStayOpen(final Player player) {
        return true;
    }

    @Override
    public CompletableFuture<Optional<Preview>> getPreview(final ResourceKey resource, final long amount,
                                                           final CancellationToken cancellationToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Optional<TreePreview>> getTreePreview(final ResourceKey resource, final long amount,
                                                                   final CancellationToken cancellationToken) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Long> getMaxAmount(final ResourceKey resource, final CancellationToken cancellationToken) {
        return CompletableFuture.completedFuture(0L);
    }

    @Override
    public Optional<TaskId> startTask(final ResourceKey resource,
                                      final long amount,
                                      final Actor actor,
                                      final boolean notify,
                                      final CancellationToken cancellationToken) {
        throw new UnsupportedOperationException();
    }
}
