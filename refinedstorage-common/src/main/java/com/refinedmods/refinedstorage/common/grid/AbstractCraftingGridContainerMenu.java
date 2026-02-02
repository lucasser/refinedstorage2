package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.api.resource.ResourceKey;
import com.refinedmods.refinedstorage.api.resource.list.MutableResourceList;
import com.refinedmods.refinedstorage.api.resource.repository.ResourceRepositoryFilter;
import com.refinedmods.refinedstorage.common.api.grid.view.GridResource;
import com.refinedmods.refinedstorage.common.grid.view.ItemGridResource;
import com.refinedmods.refinedstorage.common.grid.workstations.AbstractCraftingMatrix;
import com.refinedmods.refinedstorage.common.grid.workstations.AbstractGridResultSlot;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apiguardian.api.API;

public abstract class AbstractCraftingGridContainerMenu extends AbstractGridContainerMenu {
    private final Player gridPlayer;
    private final CraftingGrid craftingGrid;

    @Nullable
    private Consumer<Boolean> activenessListener;
    @Nullable
    private ResourceRepositoryFilter<GridResource> filterBeforeFilteringBasedOnCraftingMatrixItems;

    private final List<Slot> matrixSlots = new ArrayList<>();

    protected AbstractCraftingGridContainerMenu(final MenuType<? extends AbstractGridContainerMenu> menuType,
                                                final int syncId,
                                                final Inventory playerInventory,
                                                final GridData gridData) {
        super(menuType, syncId, playerInventory, gridData);
        this.craftingGrid = new ClientCraftingGrid(playerInventory.player::level);
        craftingGrid.setActiveMatrix(ResourceLocation.fromNamespaceAndPath("minecraft", "crafting"));
        this.gridPlayer = playerInventory.player;
    }

    protected AbstractCraftingGridContainerMenu(final MenuType<? extends AbstractGridContainerMenu> menuType,
                                                final int syncId,
                                                final Inventory playerInventory,
                                                final CraftingGrid craftingGrid) {
        super(menuType, syncId, playerInventory, craftingGrid);
        this.craftingGrid = craftingGrid;
        craftingGrid.setActiveMatrix(ResourceLocation.fromNamespaceAndPath("minecraft", "crafting"));
        this.gridPlayer = playerInventory.player;
    }

    public void setActivenessListener(@Nullable final Consumer<Boolean> activenessListener) {
        this.activenessListener = activenessListener;
    }

    @Override
    public void onActiveChanged(final boolean newActive) {
        super.onActiveChanged(newActive);
        if (activenessListener != null) {
            activenessListener.accept(newActive);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(final ItemStack stack, final Slot slot) {
        if (slot instanceof AbstractGridResultSlot) {
            return false;
        }
        return super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    @SuppressWarnings("resource")
    public ItemStack quickMoveStack(final Player actor, final int slotIndex) {
        final Slot slot = getSlot(slotIndex);
        if (!actor.level().isClientSide()
            && slot instanceof AbstractGridResultSlot resultSlot
            && resultSlot.hasItem()) {
            final ItemStack craftedStack = resultSlot.onQuickCraft(actor);
            craftingGrid.acceptQuickCraft(actor, craftedStack);
            return ItemStack.EMPTY;
        }
        return super.quickMoveStack(actor, slotIndex);
    }

    //TODO: have matrixSlots list and manage that separately
    @Override
    public void resized(final int playerInventoryY, final int topYStart, final int topYEnd) {
        resetMatrixSlots();
        super.resized(playerInventoryY, topYStart, topYEnd);
        craftingGrid.getActiveMatrix().prepRenderers(gridPlayer, playerInventoryY, topYStart, topYEnd);
        craftingGrid.getActiveMatrix().getMatrixSlots().forEach(this::addSlot);
//        craftingGrid.getMatrixList().forEach(matrix -> {
//            matrix.prepRenderers(gridPlayer, playerInventoryY, topYStart, topYEnd);
//            matrix.getMatrixSlots().forEach(this::addSlot);
//        });
    }

    public List<Slot> getMatrixSlots() {
        return matrixSlots;
    }

    public void addMatrixSlot(final Slot slot) {
        matrixSlots.add(slot);
        super.addSlot(slot);
    }

    public void resetMatrixSlots() {
        matrixSlots.clear();
    }

    public Slot getMatrixSlot(final int slot) {
        return matrixSlots.get(slot);
    }

    public void clear(final boolean toPlayerInventory) {
        craftingGrid.clearMatrix(gridPlayer, toPlayerInventory);
    }

    public void setWorkstationType(final ResourceLocation workstationid) {
        craftingGrid.setActiveMatrix(workstationid);
        if (craftingGrid instanceof CraftingGridBlockEntity) {
            resized(0, 0, 0);
        }
        /*resetSlots();
        craftingGrid.getActiveMatrix().prepRenderers(gridPlayer, 40, 0, 0);
        getMatrixSlots().forEach(this::addSlot);*/
    }

    @API(status = API.Status.INTERNAL)
    public MutableResourceList getAvailableListForRecipeTransfer() {
        final MutableResourceList available = getRepository().copyBackingList();
        addContainerToList(craftingGrid.getActiveMatrix().getMatrix(), available);
        addContainerToList(gridPlayer.getInventory(), available);
        return available;
    }

    private void addContainerToList(final Container container, final MutableResourceList available) {
        for (int i = 0; i < container.getContainerSize(); ++i) {
            final ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            available.add(ItemResource.ofItemStack(stack), stack.getCount());
        }
    }

    public void transferRecipe(final List<List<ItemResource>> recipe) {
        craftingGrid.transferRecipe(gridPlayer, recipe);
    }

    public void filterBasedOnCraftingMatrixItems() {
        final Set<ItemResource> craftingMatrixItems = getCraftingMatrixItems();
        filterBeforeFilteringBasedOnCraftingMatrixItems = getRepository().setFilterAndSort(
            (view, resource) -> resource instanceof ItemGridResource itemResource
                && craftingMatrixItems.contains(itemResource.getItemResource())
        );
    }

    private Set<ItemResource> getCraftingMatrixItems() {
        final Set<ItemResource> craftingMatrixItems = new HashSet<>();
        for (int i = 0; i < craftingGrid.getActiveMatrix().getMatrix().getContainerSize(); ++i) {
            final ItemStack craftingMatrixStack = craftingGrid.getActiveMatrix().getMatrix().getItem(i);
            if (craftingMatrixStack.isEmpty()) {
                continue;
            }
            craftingMatrixItems.add(ItemResource.ofItemStack(craftingMatrixStack));
        }
        return craftingMatrixItems;
    }

    public void stopFilteringBasedOnCraftingMatrixItems() {
        if (filterBeforeFilteringBasedOnCraftingMatrixItems == null) {
            return;
        }
        getRepository().setFilterAndSort(filterBeforeFilteringBasedOnCraftingMatrixItems);
        filterBeforeFilteringBasedOnCraftingMatrixItems = null;
    }

    @Nullable
    @Override
    protected ResourceKey getResourceForAutocraftableHint(final Slot slot) {
        if (slot.container == craftingGrid.getActiveMatrix().getMatrix()
            || slot.container == craftingGrid.getResult().get()) {
            return ItemResource.ofItemStack(slot.getItem());
        }
        return super.getResourceForAutocraftableHint(slot);
    }

    @Override
    public boolean isLargeSlot(final Slot slot) {
        return slot.container == craftingGrid.getResult().get() || super.isLargeSlot(slot);
    }

    public AbstractCraftingMatrix getMatrix() {
        return craftingGrid.getActiveMatrix();
    }
}
