package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.api.resource.filter.FilterMode;
import com.refinedmods.refinedstorage.common.support.FilterModeSettings;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyType;
import com.refinedmods.refinedstorage.common.support.containermenu.PropertyTypes;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

final class PatternGridPropertyTypes {
//    static final PropertyType<String> PATTERN_TYPE = PropertyTypes.createStringProperty(
//        createIdentifier("pattern_type")
//    );

    static final PropertyType<Integer> STONECUTTER_SELECTED_RECIPE = PropertyTypes.createIntegerProperty(
        createIdentifier("stonecutter_selected_recipe")
    );

    private PatternGridPropertyTypes() {
    }
}
