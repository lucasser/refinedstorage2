package com.refinedmods.refinedstorage.common.support.packet.c2s;

import com.refinedmods.refinedstorage.common.grid.AbstractCraftingGridContainerMenu;
import com.refinedmods.refinedstorage.common.support.packet.PacketContext;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static com.refinedmods.refinedstorage.common.util.IdentifierUtil.createIdentifier;

public record CraftingGridWorkstationChangePacket(String workstationid) implements CustomPacketPayload {
    public static final Type<CraftingGridWorkstationChangePacket> PACKET_TYPE = new Type<>(
        createIdentifier("crafting_grid_workstation_change")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CraftingGridWorkstationChangePacket> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            CraftingGridWorkstationChangePacket::workstationid,
            CraftingGridWorkstationChangePacket::new
        );

    public static void handle(final CraftingGridWorkstationChangePacket packet, final PacketContext ctx) {
        if (ctx.getPlayer().containerMenu instanceof AbstractCraftingGridContainerMenu craftingGridContainerMenu) {
            craftingGridContainerMenu.setWorkstationType(ResourceLocation.parse(packet.workstationid));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
