package com.zeml.rotp_zwa.network.server;

import com.zeml.rotp_zwa.action.stand.FreezFloor;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ActiveFreezFloorPacket {
    private final int entityID;
    private final boolean active;


    public ActiveFreezFloorPacket(int entityID, boolean active){
        this.entityID = entityID;
        this.active = active;
    }

    public static void encode(ActiveFreezFloorPacket msg, PacketBuffer buff){
        buff.writeInt(msg.entityID);
        buff.writeBoolean(msg.active);
    }

    public static ActiveFreezFloorPacket decode(PacketBuffer buffer){
        int entityID = buffer.readInt();
        boolean active = buffer.readBoolean();
        return new ActiveFreezFloorPacket(entityID,active);
    }

    public static void handle(ActiveFreezFloorPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = com.github.standobyte.jojo.client.ClientUtil.getEntityById(msg.entityID);
            if (entity != null) {
                FreezFloor.active = msg.active;
            }
        });
        ctx.get().setPacketHandled(true);
    }


}
