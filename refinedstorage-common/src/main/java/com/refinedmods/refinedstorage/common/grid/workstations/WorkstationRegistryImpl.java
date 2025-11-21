package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.CoreValidations;
import com.refinedmods.refinedstorage.common.api.grid.workstations.WorkstationRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WorkstationRegistryImpl<T> implements WorkstationRegistry<T> {

    private static final String VALUE_NOT_PRESENT_ERROR = "Value must be present";
    private static final String ID_NOT_PRESENT_ERROR = "ID must be present";
    private static final String ICON_NOT_PRESENT = "Icon must be present";

    private final Map<ResourceLocation, T> idToValueMap = new HashMap<>();
    private final Map<ResourceLocation, ItemStack> idToIconMap = new HashMap<>();
    private final List<ResourceLocation> ids = new ArrayList<>();

    @Override
    public void register(final ResourceLocation id, final ItemStack icon, final T value) {
        CoreValidations.validateNotNull(id, ID_NOT_PRESENT_ERROR);
        CoreValidations.validateNotNull(value, VALUE_NOT_PRESENT_ERROR);
        CoreValidations.validateNotNull(icon, ICON_NOT_PRESENT);

        if (idToValueMap.containsKey(id)) {
            throw new IllegalArgumentException("Already registered");
        }
        idToValueMap.put(id, value);
        idToIconMap.put(id, icon);
        ids.add(id);
    }

    @Override
    public Optional<ItemStack> getIcon(final ResourceLocation id) {
        CoreValidations.validateNotNull(id, VALUE_NOT_PRESENT_ERROR);
        return Optional.ofNullable(idToIconMap.get(id));
    }

    @Override
    public Optional<T> get(final ResourceLocation id) {
        CoreValidations.validateNotNull(id, ID_NOT_PRESENT_ERROR);
        return Optional.ofNullable(idToValueMap.get(id));
    }

    @Override
    public List<ResourceLocation> getAllIds() {
        return ids;
    }
}
