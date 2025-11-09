package com.refinedmods.refinedstorage.common.grid;

import com.refinedmods.refinedstorage.api.core.Action;
import com.refinedmods.refinedstorage.api.network.Network;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.common.Platform;
import com.refinedmods.refinedstorage.common.api.storage.PlayerActor;
import com.refinedmods.refinedstorage.common.content.BlockEntities;
import com.refinedmods.refinedstorage.common.content.ContentNames;
import com.refinedmods.refinedstorage.common.grid.workstations.AbstractCraftingMatrix;
import com.refinedmods.refinedstorage.common.grid.workstations.AbstractMatrix;
import com.refinedmods.refinedstorage.common.grid.workstations.CraftingCraftingMatrix;
import com.refinedmods.refinedstorage.common.grid.workstations.CraftingSmithingMatrix;
import com.refinedmods.refinedstorage.common.grid.workstations.WorkstationList;
import com.refinedmods.refinedstorage.common.support.BlockEntityWithDrops;
import com.refinedmods.refinedstorage.common.support.containermenu.NetworkNodeExtendedMenuProvider;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CraftingGridBlockEntity extends AbstractGridBlockEntity implements BlockEntityWithDrops,
    NetworkNodeExtendedMenuProvider<GridData>, CraftingGrid {
    private static final String TAG_WORKSTATION_LIST = "workstations";
    private static final String TAG_WORKSTATION_AMOUNT = "workstation_amount";

    private final WorkstationList<AbstractCraftingMatrix> matrixList = new WorkstationList<>();

    @Nullable
    private AbstractCraftingMatrix activeMatrix;

    public CraftingGridBlockEntity(final BlockPos pos, final BlockState state) {
        super(
            BlockEntities.INSTANCE.getCraftingGrid(),
            pos,
            state,
            Platform.INSTANCE.getConfig().getCraftingGrid().getEnergyUsage()
        );
        matrixList.add(new CraftingSmithingMatrix(
            this::setChanged,
            this::getLevel,
            this
        ));
        matrixList.add(new CraftingCraftingMatrix(
            this::setChanged,
            this::getLevel,
            this
        ));
        activeMatrix = matrixList.getById("crafting.crafting");
    }

    @Override
    public AbstractCraftingMatrix getActiveMatrix() {
        return activeMatrix;
    }

    @Override
    public Optional<Container> getResult() {
        return activeMatrix.getResult();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final Player player, final CraftingInput input) {
        return activeMatrix.getRemainingItems(player, input);
    }

    @Override
    public ExtractTransaction startExtractTransaction(final Player player, final boolean directCommit) {
        return getNetwork()
            .map(network -> network.getComponent(StorageNetworkComponent.class))
            .map(storage -> directCommit
                ? new DirectCommitExtractTransaction(storage)
                : new SnapshotExtractTransaction(player, storage, getActiveMatrix().getMatrix()))
            .orElse(ExtractTransaction.NOOP);
    }

    @Override
    public boolean clearMatrix(final Player player, final boolean toPlayerInventory) {
        return toPlayerInventory
            ? getActiveMatrix().clearToPlayerInventory(player)
            : clearMatrixIntoStorage(player);
    }

    private boolean clearMatrixIntoStorage(final Player player) {
        return getNetwork()
            .map(network -> network.getComponent(StorageNetworkComponent.class))
            .map(storage -> getActiveMatrix().clearIntoStorage(storage, player))
            .orElse(false);
    }

    @Override
    public void transferRecipe(final Player player, final List<List<ItemResource>> recipe) {
        getActiveMatrix().transferRecipe(
            player,
            getNetwork().map(network -> network.getComponent(StorageNetworkComponent.class)).orElse(null),
            recipe
        );
    }

    @Override
    public void acceptQuickCraft(final Player player, final ItemStack craftedStack) {
        if (player.getInventory().add(craftedStack)) {
            return;
        }
        final long inserted = getNetwork()
            .map(network -> network.getComponent(StorageNetworkComponent.class))
            .map(rootStorage -> rootStorage.insert(
                ItemResource.ofItemStack(craftedStack),
                craftedStack.getCount(),
                Action.EXECUTE,
                new PlayerActor(player)
            ))
            .orElse(0L);
        if (inserted != craftedStack.getCount()) {
            final long remainder = craftedStack.getCount() - inserted;
            final ItemStack remainderStack = craftedStack.copyWithCount((int) remainder);
            player.drop(remainderStack, false);
        }
    }

    @Override
    public GridData getMenuData() {
        return GridData.of(this);
    }

    @Override
    public StreamEncoder<RegistryFriendlyByteBuf, GridData> getMenuCodec() {
        return GridData.STREAM_CODEC;
    }

    @Override
    public Component getName() {
        return overrideName(ContentNames.CRAFTING_GRID);
    }

    @Override
    @Nullable
    public AbstractGridContainerMenu createMenu(final int syncId, final Inventory inventory, final Player player) {
        return new CraftingGridContainerMenu(syncId, inventory, this);
    }

    //TODO: dont save empty matrices
    @Override
    public void saveAdditional(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        final CompoundTag workstationList = new CompoundTag();
        matrixList.forEach(matrix -> {
            workstationList.put(matrix.getWorkstationType(), matrix.writeToTag(provider));
        });
        tag.put(TAG_WORKSTATION_LIST, workstationList);
        tag.putInt(TAG_WORKSTATION_AMOUNT, matrixList.size());
    }


    //TODO: replace with registry lookup, create a list if none found (for world upgrade stuff)
    @Override
    public void loadAdditional(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains(TAG_WORKSTATION_LIST)) {
            makeWorkstationList(tag.getCompound(TAG_WORKSTATION_LIST), provider);
        }
    }

    //TODO: foreach workstation in registry
    private void makeWorkstationList(final CompoundTag tag, final HolderLookup.Provider provider) {
        if (tag.contains("crafting.crafting")) {
            AbstractCraftingMatrix matrix;
            try {
                matrix = matrixList.getById("crafting.crafting");
            } catch (NoSuchElementException e) {
                matrix = new CraftingCraftingMatrix(
                    this::setChanged,
                    this::getLevel,
                    this
                );
            }
            matrix.readFromTag(tag.getCompound("crafting.crafting"), provider);
            matrixList.add(matrix);
        }

        if (tag.contains("crafting.smithing")) {
            AbstractCraftingMatrix matrix;
            try {
                matrix = matrixList.getById("crafting.smithing");
            } catch (NoSuchElementException e) {
                matrix = new CraftingSmithingMatrix(
                    this::setChanged,
                    this::getLevel,
                    this
                );
            }
            matrix.readFromTag(tag.getCompound("crafting.smithing"), provider);
            matrixList.add(matrix);
        }

        activeMatrix = matrixList.getById("crafting.crafting");
    }

    @Override
    public void setLevel(final Level level) {
        super.setLevel(level);
        matrixList.forEach(AbstractMatrix::levelChanged);
    }

    //TODO: should be all matrices
    @Override
    public final NonNullList<ItemStack> getDrops() {
        final NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < activeMatrix.getMatrix().getContainerSize(); ++i) {
            drops.add(activeMatrix.getMatrix().getItem(i));
        }
        return drops;
    }

    private Optional<Network> getNetwork() {
        if (!mainNetworkNode.isActive()) {
            return Optional.empty();
        }
        return Optional.ofNullable(mainNetworkNode.getNetwork());
    }
}
