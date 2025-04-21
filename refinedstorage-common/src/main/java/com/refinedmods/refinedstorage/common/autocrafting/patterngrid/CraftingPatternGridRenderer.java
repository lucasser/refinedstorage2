package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.common.grid.crafting.CraftingGridMenu;
import com.refinedmods.refinedstorage.common.grid.crafting.renderer.CraftingGridRenderer;
import com.refinedmods.refinedstorage.common.support.widget.CheckboxWidget;

import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static com.refinedmods.refinedstorage.common.autocrafting.patterngrid.PatternGridScreen.INSET_PADDING;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

public class CraftingPatternGridRenderer extends CraftingGridRenderer {
    private static final MutableComponent FUZZY_MODE = createTranslation("gui", "pattern_grid.fuzzy_mode");
    private static final MutableComponent FUZZY_MODE_ON_HELP =
        createTranslation("gui", "pattern_grid.fuzzy_mode.on.help");
    private static final MutableComponent FUZZY_MODE_OFF_HELP =
        createTranslation("gui", "pattern_grid.fuzzy_mode.off.help");

    @Nullable
    private CheckboxWidget fuzzyModeCheckbox;

    public CraftingPatternGridRenderer(final CraftingGridMenu menu, final int leftPos, final int x, final int y) {
        super(menu, leftPos, x, y);
    }

    @Override
    public void addWidgets(final Consumer<AbstractWidget> widgets, final Consumer<AbstractWidget> renderables) {
        super.addWidgets(widgets, renderables);
//        this.fuzzyModeCheckbox = createFuzzyModeCheckbox();
//        renderables.accept(fuzzyModeCheckbox);
    }

//    private CheckboxWidget createFuzzyModeCheckbox() {
//        final CheckboxWidget checkbox = new CheckboxWidget(
//            super.x + INSET_PADDING,
//            y + INSET_PADDING + 54 + INSET_PADDING - 2,
//            FUZZY_MODE,
//            Minecraft.getInstance().font,
//            menu.isFuzzyMode(),
//            CheckboxWidget.Size.SMALL
//        );
//        checkbox.setOnPressed((c, selected) -> menu.setFuzzyMode(selected));
//        checkbox.setHelpTooltip(getFuzzyModeTooltip(menu.isFuzzyMode()));
//        checkbox.visible = isFuzzyModeCheckboxVisible();
//        return checkbox;
//    }

    private static Component getFuzzyModeTooltip(final boolean fuzzyMode) {
        return fuzzyMode ? FUZZY_MODE_ON_HELP : FUZZY_MODE_OFF_HELP;
    }

    @Override
    public void workstationChanged(final String newPatternType) {
        if (fuzzyModeCheckbox != null) {
            fuzzyModeCheckbox.visible = isFuzzyModeCheckboxVisible();
        }
    }

    private boolean isFuzzyModeCheckboxVisible() {
        return true;
    }

//    @Override
//    public void fuzzyModeChanged(final boolean newFuzzyMode) {
//        if (fuzzyModeCheckbox == null) {
//            return;
//        }
//        fuzzyModeCheckbox.setSelected(newFuzzyMode);
//        fuzzyModeCheckbox.setHelpTooltip(getFuzzyModeTooltip(newFuzzyMode));
//    }
}
