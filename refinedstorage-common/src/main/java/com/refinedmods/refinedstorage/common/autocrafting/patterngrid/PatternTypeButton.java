package com.refinedmods.refinedstorage.common.autocrafting.patterngrid;

import com.refinedmods.refinedstorage.common.grid.crafting.WorkstationMenuRegistry;
import com.refinedmods.refinedstorage.common.support.widget.CustomButton;

import java.util.function.Consumer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

class PatternTypeButton extends CustomButton {
    private static final WidgetSprites GENERIC_SMALL_BUTTON_SPRITES = new WidgetSprites(
        createIdentifier("widget/generic_small_button"),
        createIdentifier("widget/generic_small_button_disabled"),
        createIdentifier("widget/generic_small_button_focused"),
        createIdentifier("widget/generic_small_button_disabled")
    );

    private final WorkstationMenuRegistry.WorkstationRegistryElement workstation;
    private boolean selected;

    PatternTypeButton(final int x,
                      final int y,
                      final Consumer<CustomButton> onPress,
                      final WorkstationMenuRegistry.WorkstationRegistryElement patternType,
                      final boolean selected) {
        super(x, y, 16, 16, GENERIC_SMALL_BUTTON_SPRITES, onPress, patternType.translatedName);
        this.workstation = patternType;
        this.selected = selected;
        this.setTooltip(Tooltip.create(patternType.translatedName));
    }

    void setSelected(final boolean selected) {
        this.selected = selected;
    }

    @Override
    public void renderWidget(final GuiGraphics graphics, final int x, final int y, final float partialTicks) {
        final ResourceLocation location = sprites.get(isActive(), isHovered() || selected);
        graphics.blitSprite(location, getX(), getY(), width, height);
        graphics.renderItem(workstation.icon, getX(), getY());
    }
}
