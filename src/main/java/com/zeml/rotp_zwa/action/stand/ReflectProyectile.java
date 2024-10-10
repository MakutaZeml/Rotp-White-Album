package com.zeml.rotp_zwa.action.stand;

import java.util.stream.Stream;

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
    public static boolean active = false;
    public ReflectProyectile(StandEntityAction.Builder builder) {
        super(builder);
    }


    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){
            ((WhiteAlbumEntity) standEntity).setReflect( !((WhiteAlbumEntity) standEntity).getReflect());
            this.active = !this.active;
            SoundEvent user = ((WhiteAlbumEntity) standEntity).getReflect()? InitSounds.USER_WEEPS_SUMMON.get():InitSounds.USER_WEEPS_UNSUMMON.get();
            world.playSound(null,standEntity.blockPosition(),user, SoundCategory.PLAYERS,1,1);

        }
    }

    @Override
    public boolean greenSelection(IStandPower power, ActionConditionResult conditionCheck) {
        return this.active;
    }

    @Override
    public Stream<SoundEvent> getSounds(StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task) {
        return Stream.of(((WhiteAlbumEntity) standEntity).geFreezeWater() ? InitSounds.WHITE_ALBUM_WALKER.get() : InitSounds.WHITE_ALBUM_UNFREEZE.get());
    }


    ResourceLocation UN_REFLECT = new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/action/wa_unreflec.png");

    @Override
    public ResourceLocation getIconTexture(@Nullable IStandPower power) {
        if(!active){
            return super.getIconTexture(power);
        }
        return UN_REFLECT;
    }


}
