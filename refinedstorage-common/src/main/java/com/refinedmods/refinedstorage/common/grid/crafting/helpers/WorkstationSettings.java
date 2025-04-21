package com.refinedmods.refinedstorage.common.grid.crafting.helpers;

import com.refinedmods.refinedstorage.common.grid.crafting.WorkstationMenuRegistry;

public class WorkstationSettings {

    public static final int getWorkstationId(final String workstationName) {
        return WorkstationMenuRegistry.getWorkstationId(workstationName);
    }

    public static final String getWorkstationName(final int workstationId) {
        return WorkstationMenuRegistry.getWorkstationName(workstationId);
    }
}
