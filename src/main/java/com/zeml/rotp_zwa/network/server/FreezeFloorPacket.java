package com.zeml.rotp_zwa.network.server;

import com.github.standobyte.jojo.client.ClientUtil;
import com.zeml.rotp_zwa.capability.LivingData;
import com.zeml.rotp_zwa.capability.LivingDataProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class FreezeFloorPacket {
    private final int entityID;
    private final boolean freezing;

    public FreezeFloorPacket(int entityID, boolean stay){
        this.entityID = entityID;
        this.freezing = stay;
    }


    public static void encode(FreezeFloorPacket msg, PacketBuffer buf) {
        buf.writeInt(msg.entityID);
        buf.writeBoolean(msg.freezing);
    }

    public static FreezeFloorPacket decode(PacketBuffer buf) {
        return new FreezeFloorPacket(buf.readInt(), buf.readBoolean());
    }


    public static void handle(FreezeFloorPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = ClientUtil.getEntityById(msg.entityID);
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity) entity;
                LazyOptional<LivingData> playerDataOptional = living.getCapability(LivingDataProvider.CAPABILITY);
                playerDataOptional.ifPresent(playerData ->{
                    playerData.setFreezeFloor(msg.freezing);
                });
            }
        });
        ctx.get().setPacketHandled(true);

    }
}
