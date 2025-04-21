package com.refinedmods.refinedstorage.common.grid.crafting;

import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractBaseMatrix;
import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractCraftingMatrix;
import com.refinedmods.refinedstorage.common.api.grid.workstations.MatrixMenuFactory;
import com.refinedmods.refinedstorage.common.api.grid.workstations.MenuPairFactory;
import com.refinedmods.refinedstorage.common.api.grid.workstations.RecipeType;
import com.refinedmods.refinedstorage.common.api.grid.workstations.AbstractPatternMatrix;
import com.refinedmods.refinedstorage.common.grid.AbstractGridContainerMenu;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

public class WorkstationMenuRegistry {
    public static final Map<String, WorkstationRegistryElement> REGISTRY = new HashMap<>();

    public static void register(final String workstationName, final MenuPairFactory pair, final ItemStack icon) {
        REGISTRY.putIfAbsent(workstationName, new WorkstationRegistryElement(workstationName, pair.craftingFactory(),
            pair.patternFactory(), icon));
    }

    public static <T extends AbstractBaseMatrix> T create(final String workstationId, final RecipeType mode, final Inventory inventory, final MenuType<? extends AbstractGridContainerMenu> menuType, final Integer syncid) {
        final MatrixMenuFactory<T> factory = switch (mode) {
            case CRAFTING -> (MatrixMenuFactory<T>) REGISTRY.get(workstationId).craftingMatrix;
            case PATTERN -> (MatrixMenuFactory<T>) REGISTRY.get(workstationId).patternMatrix;
            default ->
                throw new IllegalArgumentException("No menu registered for: " + workstationId + " with mode: " + mode);
        };

        return factory.create(inventory, menuType, syncid);
    }

    public static int getWorkstationId(final String workstationName) {
        if (REGISTRY.containsKey(workstationName)) {
            return REGISTRY.get(workstationName).workstationId;
        }
        return 0;
    }

    public static String getWorkstationName(final int workstationId) {
        for (final Map.Entry<String, WorkstationRegistryElement> entry : REGISTRY.entrySet()) {
            if (entry.getValue().workstationId == workstationId) {
                return entry.getKey();
            }
        }
        return "";
    }

    public static class WorkstationRegistryElement {
        public final Integer workstationId;
        public final String workstationName;
        public final MatrixMenuFactory<? extends AbstractCraftingMatrix<?>> craftingMatrix;
        public final MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternMatrix;
        public final ItemStack icon;
        public final MutableComponent translatedName;

        public WorkstationRegistryElement(final String workstationName,
                                          final MatrixMenuFactory<? extends AbstractCraftingMatrix<?>> craftingMatrix,
                                          final MatrixMenuFactory<? extends AbstractPatternMatrix<?>> patternMatrix,
                                          final ItemStack icon) {
            this.workstationId = workstationName.hashCode();
            this.workstationName = workstationName;
            this.craftingMatrix = craftingMatrix;
            this.patternMatrix = patternMatrix;
            this.icon = icon;
            this.translatedName = createTranslation("misc", "pattern." + workstationName);
        }
    }
}
