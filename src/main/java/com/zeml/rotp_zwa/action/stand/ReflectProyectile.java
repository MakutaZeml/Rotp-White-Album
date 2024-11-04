package com.zeml.rotp_zwa.action.stand;

import java.util.stream.Stream;

import com.github.standobyte.jojo.action.ActionTarget;
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
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.zeml.rotp_zwa.init.InitSounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ReflectProyectile extends StandEntityAction {
    public ReflectProyectile(StandEntityAction.Builder builder) {
        super(builder);
    }


    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){
            LivingEntity standUser = userPower.getUser();
            LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
            playerDataOptional.ifPresent(playerData ->{
                boolean isGently = playerData.isGentlyWeeps();
                playerData.setGentlyWeeps(!isGently);
            });
        }
    }

    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        LivingEntity standUser = power.getUser();
        LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
        return playerDataOptional.map(LivingData::isGentlyWeeps).isPresent()?playerDataOptional.map(LivingData::isGentlyWeeps).get() :false;
    }

    @Override
    public Stream<SoundEvent> getSounds(StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task) {
        LivingEntity standUser = standPower.getUser();
        LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
        boolean activated = playerDataOptional.map(LivingData::isGentlyWeeps).isPresent()?playerDataOptional.map(LivingData::isGentlyWeeps).get() :false;
        return Stream.of(activated? InitSounds.WHITE_ALBUM_WALKER.get() : InitSounds.WHITE_ALBUM_UNFREEZE.get());
    }


    @Override
    protected SoundEvent getShout(LivingEntity user, IStandPower power, ActionTarget target, boolean wasActive) {
        LazyOptional<LivingData> playerDataOptional = user.getCapability(LivingDataProvider.CAPABILITY);
        boolean activated = playerDataOptional.map(LivingData::isGentlyWeeps).isPresent()?playerDataOptional.map(LivingData::isGentlyWeeps).get() :false;
        return !activated ? InitSounds.USER_WEEPS_SUMMON.get():InitSounds.USER_WEEPS_UNSUMMON.get();
    }

    ResourceLocation UN_REFLECT = new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/action/wa_unreflec.png");

    @Override
    public ResourceLocation getIconTexture(@Nullable IStandPower power) {
        LivingEntity standUser = power.getUser();
        LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
        boolean activated = playerDataOptional.map(LivingData::isGentlyWeeps).isPresent()?playerDataOptional.map(LivingData::isGentlyWeeps).get() :false;
        if(!activated){
            return super.getIconTexture(power);
        }
        return UN_REFLECT;
    }


}
