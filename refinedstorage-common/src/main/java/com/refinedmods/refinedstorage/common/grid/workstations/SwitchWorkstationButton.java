package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.common.support.widget.CustomButton;

import java.util.function.Consumer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

public class SwitchWorkstationButton extends CustomButton {
    private static final WidgetSprites GENERIC_SMALL_BUTTON_SPRITES = new WidgetSprites(
        createIdentifier("widget/generic_small_button"),
        createIdentifier("widget/generic_small_button_disabled"),
        createIdentifier("widget/generic_small_button_focused"),
        createIdentifier("widget/generic_small_button_disabled")
    );

    private final ResourceLocation workstationid;
    private final ItemStack icon;
    private boolean selected;

    public SwitchWorkstationButton(final int x,
                                   final int y,
                                   final Consumer<CustomButton> onPress,
                                   final ResourceLocation workstationid,
                                   final ItemStack icon,
                                   final boolean selected) {
        super(x, y, 16, 16, GENERIC_SMALL_BUTTON_SPRITES, onPress,
            createTranslation("misc", "workstations." + workstationid));
        this.workstationid = workstationid;
        this.icon = icon;
        this.selected = selected;
        this.setTooltip(Tooltip.create(getTranslatedName()));
    }

    void setSelected(final boolean selected) {
        this.selected = selected;
    }

    @Override
    public void renderWidget(final GuiGraphics graphics, final int x, final int y, final float partialTicks) {
        final ResourceLocation location = sprites.get(isActive(), isHovered() || selected);
        graphics.blitSprite(location, getX(), getY(), width, height);
        graphics.renderItem(icon, getX(), getY());
    }

    private MutableComponent getTranslatedName() {
        return createTranslation("misc", "workstations." + workstationid);
    }
}
