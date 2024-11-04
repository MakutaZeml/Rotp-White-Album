package com.zeml.rotp_zwa.capability;

import com.zeml.rotp_zwa.network.AddonPackets;
import com.zeml.rotp_zwa.network.server.FreezeFloorPacket;
import com.zeml.rotp_zwa.network.server.GentlyWeepsPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class LivingData implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entity;
    private boolean gentlyWeeps = false;
    private boolean freezeFloor = false;

    public LivingData(LivingEntity entity) {
        this.entity = entity;
    }

    public void setGentlyWeeps(boolean gentlyWeeps) {
        this.gentlyWeeps = gentlyWeeps;
        if(entity instanceof ServerPlayerEntity){
            AddonPackets.sendToClient(new GentlyWeepsPacket(entity.getId(),gentlyWeeps),(ServerPlayerEntity) entity);
        }
    }

    public boolean isGentlyWeeps() {
        return this.gentlyWeeps;
    }


    public void setFreezeFloor(boolean freezeFloor) {
        this.freezeFloor = freezeFloor;
        if(entity instanceof ServerPlayerEntity){
            AddonPackets.sendToClient(new FreezeFloorPacket(entity.getId(),freezeFloor),(ServerPlayerEntity) entity);
        }
    }

    public boolean isFreezeFloor() {
        return this.freezeFloor;
    }

    public void syncWithAnyPlayer(ServerPlayerEntity player) {
        //AddonPackets.sendToClient(new TrPickaxesThrownPacket(entity.getId(), pickaxesThrown), player);
    }

    // If there is data that should only be known to the player, and not to other ones, sync it here instead.
    public void syncWithEntityOnly(ServerPlayerEntity player) {
        AddonPackets.sendToClient(new GentlyWeepsPacket(player.getId(),this.gentlyWeeps), player);
        AddonPackets.sendToClient(new FreezeFloorPacket(player.getId(),this.freezeFloor),player);
//        AddonPackets.sendToClient(new SomeDataPacket(someDataField), player);
    }




    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("GentlyWeeps",this.gentlyWeeps);
        nbt.putBoolean("FreezeFloor",this.freezeFloor);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.gentlyWeeps = nbt.getBoolean("GentlyWeeps");
        this.freezeFloor = nbt.getBoolean("FreezeFloor");
    }
}
