package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public interface AbstractMatrix {

    ResourceLocation getInsert();

    int getInsertHeight();

    List<Slot> getMatrixSlots();

    void renderSlots(AbstractGridContainerMenu menu,
                     Player gridPlayer,
                     int playerInventoryY,
                     int topYStart,
                     int topYEnd);

    Container getMatrix();

    Optional<Container> getResult();

    String getWorkstationType();

    CompoundTag writeToTag(HolderLookup.Provider provider);

    void readFromTag(CompoundTag tag, HolderLookup.Provider provider);

    void changed();

    void transferRecipe(Player player,
                        @Nullable RootStorage rootStorage,
                        List<List<ItemResource>> recipe);
}
