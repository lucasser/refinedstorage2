package com.refinedmods.refinedstorage.common.api.grid.workstations;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL)
@FunctionalInterface
public interface MatrixMenuFactory<T> {
    T create(Inventory inventory, MenuType<?> menuType, Integer syncid);
}
