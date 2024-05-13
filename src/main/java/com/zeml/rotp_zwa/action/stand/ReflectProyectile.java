package com.zeml.rotp_zwa.action.stand;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.zeml.rotp_zwa.init.InitSounds;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ReflectProyectile extends StandEntityAction {

    public ReflectProyectile(StandEntityAction.Builder builder) {
        super(builder);
    }


    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){
            SoundEvent stand = ((WhiteAlbumEntity) standEntity).getReflect()? InitSounds.WHITE_ALBUM_WEEPS_UNSUMMON.get():InitSounds.WHITE_ALBUM_WEEPS_SUMMON.get();
            SoundEvent user = ((WhiteAlbumEntity) standEntity).getReflect()? InitSounds.USER_WEEPS_UNSUMMON.get():InitSounds.USER_WEEPS_SUMMON.get();

            world.playSound(null,standEntity.blockPosition(),stand,SoundCategory.PLAYERS,1,1);

            world.playSound(null,standEntity.blockPosition(),user,SoundCategory.PLAYERS,1,1);

            ((WhiteAlbumEntity) standEntity).setReflect( !((WhiteAlbumEntity) standEntity).getReflect());
        }
    }





}
