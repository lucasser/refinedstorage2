package com.refinedmods.refinedstorage.common.api.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.world.level.Level;
import org.apiguardian.api.API;

@API(status = API.Status.EXPERIMENTAL)
@FunctionalInterface
public interface MatrixMenuFactory<T, E> {
    T create(@Nullable Runnable listener,
             Supplier<@NullableType Level> levelSupplier,
             E parent
    );
}

