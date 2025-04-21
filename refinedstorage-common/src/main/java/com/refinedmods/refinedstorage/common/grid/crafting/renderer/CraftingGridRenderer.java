package com.refinedmods.refinedstorage.common.grid.crafting.renderer;

import com.refinedmods.refinedstorage.common.api.grid.workstations.MatrixRenderer;
import com.refinedmods.refinedstorage.common.grid.crafting.CraftingGridMenu;

import java.util.function.Consumer;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.resources.ResourceLocation;

import static com.refinedmods.refinedstorage.common.autocrafting.patterngrid.PatternGridScreen.INSET_PADDING;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;
import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createTranslation;

public class CraftingGridRenderer implements MatrixRenderer {
    private static final ResourceLocation CRAFTING = createIdentifier("pattern_grid/crafting");

    protected final CraftingGridMenu menu;
    protected final int leftPos;
    protected final int x;
    protected final int y;

    public CraftingGridRenderer(final CraftingGridMenu menu, final int leftPos, final int x, final int y) {
        this.menu = menu;
        this.leftPos = leftPos;
        this.x = x;
        this.y = y;
    }

    @Override
    public void addWidgets(final Consumer<AbstractWidget> widgets, final Consumer<AbstractWidget> renderables) {
    }

    @Override
    public int getClearButtonX() {
        return leftPos + 68;
    }

    @Override
    public int getClearButtonY() {
        return y + INSET_PADDING;
    }

    @Override
    public void workstationChanged(final String newPatternType) {
    }

    @Override
    public void renderBackground(final GuiGraphics graphics,
                                 final float partialTicks,
                                 final int mouseX,
                                 final int mouseY) {
        graphics.blitSprite(CRAFTING, x + INSET_PADDING, y + INSET_PADDING, 130, 54);
    }
}
