package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.storage.root.RootStorage;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;
import com.refinedmods.refinedstorage.common.support.resource.ItemResource;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public interface AbstractMatrix {

    ResourceLocation getInsert();

    int getInsertHeight();

    //TODO: remove these 2
    List<Slot> getMatrixSlots();

    /**
     * Preps various elements to be rendered: slots, smithing table armour stand, scrollbar
     * */
    void prepRenderers(AbstractGridContainerMenu menu,
                       Player gridPlayer,
                       int playerInventoryY,
                       int topYStart,
                       int topYEnd);

    /**
     * Actually display all the elements
     * */
    void render(AbstractGridContainerMenu menu,
                GuiGraphics graphics,
                float partialTicks,
                int mouseX,
                int mouseY,
                int topX,
                int topY,
                int insertY);

    /**
     * Tooltip stuff
     * */
    void renderTooltip(Font font,
                              @Nullable Slot hoveredSlot,
                              GuiGraphics graphics,
                              int mouseX,
                              int mouseY);

    /**
     * Add widgets to as elements to render
     * */
    void addWidgets(Consumer<AbstractWidget> widgets,
                           Consumer<AbstractWidget> renderables);

    Container getMatrix();

    Optional<Container> getResult();

    String getWorkstationType();

    CompoundTag writeToTag(HolderLookup.Provider provider);

    void readFromTag(CompoundTag tag, HolderLookup.Provider provider);

    void changed();

    void transferRecipe(Player player,
                        @Nullable RootStorage rootStorage,
                        List<List<ItemResource>> recipe);

    void updateMatrixAndNotifyListenerLater(Runnable runnable);

    void tick();

    void levelChanged();
}
