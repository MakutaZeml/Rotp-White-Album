package com.zeml.rotp_zwa.action.stand;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.zeml.rotp_zwa.init.InitSounds;
import com.zeml.rotp_zwa.network.AddonPackets;
import com.zeml.rotp_zwa.network.server.ActiveFreezFloorPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FreezFloor extends StandEntityAction {
    public static boolean active = false;
    public FreezFloor(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide) {
            ((WhiteAlbumEntity) standEntity).setFreezeWater(!((WhiteAlbumEntity) standEntity).geFreezeWater());
            this.active = !this.active;
            if(userPower.getUser() instanceof ServerPlayerEntity){
                AddonPackets.sendToClient(new ActiveFreezFloorPacket(userPower.getUser().getId(),active), (ServerPlayerEntity) userPower.getUser());
            }
        }
    }


    @Override
    public List<Supplier<SoundEvent>> getSounds(StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task) {
        List<Supplier<SoundEvent>> list = new ArrayList<>();
        list.add(((WhiteAlbumEntity) standEntity).geFreezeWater() ? InitSounds.WHITE_ALBUM_UNFREEZE : InitSounds.WHITE_ALBUM_WALKER);
        return list;
    }



    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        return this.active;
    }
    ResourceLocation UN_FREEZE = new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/action/wa_unfreeze.png");

    @Override
    public ResourceLocation getIconTexture(@Nullable IStandPower power) {
        if(!active){
            return super.getIconTexture(power);
        }
        return UN_FREEZE;
    }

}