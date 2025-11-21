package com.refinedmods.refinedstorage.common.grid.workstations;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GridCyclingSlotBackground {
    private static final int ICON_CHANGE_TICK_RATE = 30;
    private static final int ICON_SIZE = 16;
    private static final int ICON_TRANSITION_TICK_DURATION = 4;
    private final int slotIndex;
    private List<ResourceLocation> icons = List.of();
    private int tick;
    private int iconIndex;

    public GridCyclingSlotBackground(final int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public void tick(final List<ResourceLocation> iconList) {
        if (!this.icons.equals(iconList)) {
            this.icons = iconList;
            this.iconIndex = 0;
        }

        if (!this.icons.isEmpty() && ++this.tick % 30 == 0) {
            this.iconIndex = (this.iconIndex + 1) % this.icons.size();
        }

    }

    public void render(final Slot slot, final GuiGraphics guiGraphics,
                       final float partialTick, final int x, final int y) {
        if (!this.icons.isEmpty() && !slot.hasItem()) {
            final boolean flag = this.icons.size() > 1 && this.tick >= 30;
            final float f = flag ? this.getIconTransitionTransparency(partialTick) : 1.0F;
            if (f < 1.0F) {
                final int i = Math.floorMod(this.iconIndex - 1, this.icons.size());
                this.renderIcon(slot, this.icons.get(i), 1.0F - f, guiGraphics, x, y);
            }

            this.renderIcon(slot, this.icons.get(this.iconIndex), f, guiGraphics, x, y);
        }

    }

    public int getSlotIndex() {
        return this.slotIndex;
    }

    private void renderIcon(final Slot slot, final ResourceLocation icon,
                            final float alpha, final GuiGraphics guiGraphics, final int x, final int y) {
        final TextureAtlasSprite
            textureatlassprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(icon);
        guiGraphics.blit(x + slot.x, y + slot.y, 0, 16, 16, textureatlassprite, 1.0F, 1.0F, 1.0F, alpha);
    }

    private float getIconTransitionTransparency(final float partialTick) {
        final float f = (float) (this.tick % 30) + partialTick;
        return Math.min(f, 4.0F) / 4.0F;
    }
}
