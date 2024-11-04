package com.zeml.rotp_zwa.action.stand;

import java.util.stream.Stream;

import com.zeml.rotp_zwa.capability.LivingData;
import com.zeml.rotp_zwa.capability.LivingDataProvider;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.init.InitSounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class FreezFloor extends StandEntityAction {


    public FreezFloor(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide) {
            LazyOptional<LivingData> playerDataOptional = userPower.getUser().getCapability(LivingDataProvider.CAPABILITY);
            playerDataOptional.ifPresent(livingData -> {
                livingData.setFreezeFloor(!livingData.isFreezeFloor());
            });
        }
    }


    @Override
    public Stream<SoundEvent> getSounds(StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task) {
        LivingEntity standUser = standPower.getUser();
        LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
        boolean activated = playerDataOptional.map(LivingData::isFreezeFloor).isPresent()?playerDataOptional.map(LivingData::isFreezeFloor).get() :false;
        return Stream.of(activated ? InitSounds.WHITE_ALBUM_UNFREEZE.get() : InitSounds.WHITE_ALBUM_WALKER.get());
    }



    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        LivingEntity standUser = power.getUser();
        LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
        return playerDataOptional.map(LivingData::isFreezeFloor).isPresent()?playerDataOptional.map(LivingData::isFreezeFloor).get() :false;
    }

    ResourceLocation UN_FREEZE = new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/action/wa_unfreeze.png");

    @Override
    public ResourceLocation getIconTexture(@Nullable IStandPower power) {
        LivingEntity standUser = power.getUser();
        LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
        boolean activated = playerDataOptional.map(LivingData::isFreezeFloor).isPresent()?playerDataOptional.map(LivingData::isFreezeFloor).get() :false;
        if(!activated){
            return super.getIconTexture(power);
        }
        return UN_FREEZE;
    }
}