package com.refinedmods.refinedstorage.common.grid.workstations;

import com.refinedmods.refinedstorage.api.core.NullableType;
import com.refinedmods.refinedstorage.common.autocrafting.VanillaConstants;
import com.refinedmods.refinedstorage.common.support.RecipeMatrixContainer;
import com.refinedmods.refinedstorage.common.util.ClientPlatformUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

public class SmithingMatrix {
    protected static final int Y_OFFSET_BETWEEN_PLAYER_INVENTORY_AND_SMITHING_TABLE_SLOTS = 32;

    protected static final ResourceLocation WORKSTATION_TYPE =
        ResourceLocation.fromNamespaceAndPath("minecraft", "smithing");

    private static final RecipeType<SmithingRecipe> RECIPE_TYPE = RecipeType.SMITHING;

    protected final List<Slot> matrixSlots = new ArrayList<>();

    protected final WorkstationRecipeContainer<RecipeMatrixContainer, ResultContainer> recipeContainer;

    protected final Supplier<Level> levelSupplier;

    @Nullable
    protected List<RecipeHolder<SmithingRecipe>> smithingTableRecipes;

    @Nullable
    protected RecipeHolder<SmithingRecipe> currentRecipe;

    @Nullable
    protected ArmorStand preview;

    protected boolean active;

    //TODO: may cause problems if inventory is overwritten to have a different size
    private final GridCyclingSlotBackground templateIcon = new GridCyclingSlotBackground(0);
    private final GridCyclingSlotBackground baseIcon = new GridCyclingSlotBackground(1);
    private final GridCyclingSlotBackground additionalIcon = new GridCyclingSlotBackground(2);

    public SmithingMatrix(@Nullable final Runnable listener, final Supplier<@NullableType Level> levelSupplier) {
        this.levelSupplier = levelSupplier;
        this.recipeContainer = new WorkstationRecipeContainer<>(
            listener,
            new RecipeMatrixContainer(this::inputChanged, 3, 1),
            new ResultContainer()
        );
    }

    protected void updateRecipes() {
        if (getLevel() != null) {
            this.smithingTableRecipes = getLevel().getRecipeManager()
                .getAllRecipesFor(RecipeType.SMITHING);
        } else {
            this.smithingTableRecipes = new ArrayList<>();
        }
    }

    protected void inputChanged() {
        if (getLevel() == null || recipeContainer.isMuted()) {
            return;
        }
        final SmithingRecipeInput input = getInputAsSmithingRecipe();
        if (currentRecipe == null || !currentRecipe.value().matches(input, getLevel())) {
            currentRecipe = loadRecipe(getLevel());
        }
        if (currentRecipe == null) {
            setResult(null, ItemStack.EMPTY);
        } else {
            setResult(currentRecipe, currentRecipe.value().assemble(input, getLevel().registryAccess()));
        }
        recipeContainer.changed();
        updatePreview();
    }

    private void setResult(@Nullable final RecipeHolder<?> recipe, final ItemStack result) {
        recipeContainer.getOutput().ifPresent(output -> {
            output.setRecipeUsed(recipe);
            output.setItem(0, result);
        });
    }

    @Nullable
    protected Level getLevel() {
        return levelSupplier.get();
    }

    @Nullable
    private RecipeHolder<SmithingRecipe> loadRecipe(final Level level) {
        return level
            .getRecipeManager()
            .getRecipeFor(RECIPE_TYPE, getInputAsSmithingRecipe(), level)
            .orElse(null);
    }

    protected SmithingRecipeInput getInputAsSmithingRecipe() {
        final RecipeMatrixContainer inputContainer = recipeContainer.getInput();
        return new SmithingRecipeInput(inputContainer.getItem(0),
            inputContainer.getItem(1),
            inputContainer.getItem(2)
        );
    }

    //TODO: should actually look for remaining items. is it needed for patterns? if not move to CraftingCrafting
    protected NonNullList<ItemStack> getRemainingCraftingItems(final Player player, final CraftingInput input) {
        return NonNullList.withSize(input.size(), ItemStack.EMPTY);
    }

    protected void renderIcons(final AbstractContainerMenu menu, final GuiGraphics graphics,
                               final float partialTicks, final int leftPos, final int topPos) {
        templateIcon.render(matrixSlots.get(templateIcon.getSlotIndex()), graphics, partialTicks, leftPos, topPos);
        baseIcon.render(matrixSlots.get(baseIcon.getSlotIndex()), graphics, partialTicks, leftPos, topPos);
        additionalIcon.render(matrixSlots.get(additionalIcon.getSlotIndex()), graphics, partialTicks, leftPos, topPos);
    }

    Optional<SmithingTemplateItem> getSmithingTableTemplateItem() {
        final ItemStack stack = recipeContainer.getInput().getItem(0);
        if (!stack.isEmpty()) {
            final Item item = stack.getItem();
            if (item instanceof SmithingTemplateItem templateItem) {
                return Optional.of(templateItem);
            }
        }
        return Optional.empty();
    }

    protected void tickMatrix() {
        final Optional<SmithingTemplateItem> templateItem = getSmithingTableTemplateItem();
        templateIcon.tick(VanillaConstants.EMPTY_SLOT_SMITHING_TEMPLATES);
        baseIcon.tick(templateItem.map(SmithingTemplateItem::getBaseSlotEmptyIcons).orElse(List.of()));
        additionalIcon.tick(templateItem.map(SmithingTemplateItem::getAdditionalSlotEmptyIcons).orElse(List.of()));
    }

    protected void updatePreview() {
        if (preview == null) {
            return;
        }
        for (final EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            preview.setItemSlot(equipmentslot, ItemStack.EMPTY);
        }
        if (recipeContainer.getOutput().isEmpty()) {
            return;
        }

        final ItemStack result = recipeContainer.getOutput().get().getItem(0);

        if (result.getItem() instanceof ArmorItem armorItem) {
            preview.setItemSlot(armorItem.getEquipmentSlot(), result);
        } else {
            preview.setItemSlot(EquipmentSlot.OFFHAND, result);
        }
    }

    protected void renderWidgets(final Consumer<AbstractWidget> widgets, final Consumer<AbstractWidget> renderables) {

    }

    protected void prepArmourStand() {
        final Level level = ClientPlatformUtil.getClientLevel();
        if (level == null) {
            return;
        }
        preview = new ArmorStand(level, 0.0, 0.0, 0.0);
        preview.setNoBasePlate(true);
        preview.setShowArms(true);
        preview.yBodyRot = 210.0F;
        preview.setXRot(25.0F);
        preview.yHeadRot = preview.getYRot();
        preview.yHeadRotO = preview.getYRot();
    }

    protected void addTooltip(final Font font, @org.jetbrains.annotations.Nullable final Slot hoveredSlot,
                              final GuiGraphics graphics,
                              final int mouseX, final int mouseY) {
        if (hoveredSlot == null || hoveredSlot.hasItem() || !(hoveredSlot.container instanceof RecipeMatrixContainer)) {
            return;
        }
        if (recipeContainer.getInput().getItem(0).getItem() instanceof SmithingTemplateItem template) {
            if (hoveredSlot.getContainerSlot() == 1) {
                graphics.renderTooltip(font, split(font, template.getBaseSlotDescription()), mouseX, mouseY);
            } else if (hoveredSlot.getContainerSlot() == 2) {
                graphics.renderTooltip(font, split(font, template.getAdditionSlotDescription()), mouseX, mouseY);
            }
        } else {
            if (hoveredSlot.getContainerSlot() == 0) {
                graphics.renderTooltip(
                    font,
                    split(font, VanillaConstants.MISSING_SMITHING_TEMPLATE_TOOLTIP),
                    mouseX,
                    mouseY
                );
            }
        }
    }

    private static List<FormattedCharSequence> split(final Font font, final Component template) {
        return font.split(template, 115);
    }
}
