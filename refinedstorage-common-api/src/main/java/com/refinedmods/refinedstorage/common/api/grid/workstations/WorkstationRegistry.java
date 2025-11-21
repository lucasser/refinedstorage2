package com.refinedmods.refinedstorage.common.api.grid.workstations;

import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;

import java.util.List;
import java.util.Optional;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL, since = "2.0.1")
public interface WorkstationRegistry<T> {
    /**
     * Registers a value in the registry, identified by the id.
     * Duplicate IDs or values are not allowed.
     *
     * @param id    the id
     * @param value the value
     */
    void register(ResourceLocation id, ItemStack icon, T value);

    /**
     * @param id the id
     * @return the icon linked to the workstation
     */
    Optional<ItemStack> getIcon(ResourceLocation id);

    /**
     * @param id the id
     * @return the value, if present
     */
    Optional<T> get(ResourceLocation id);

    /**
     * @return an unmodifiable list of all values
     */
    List<ResourceLocation> getAllIds();
}
