package com.zeml.rotp_zwa.action.stand;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.zeml.rotp_zwa.init.InitSounds;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class FreezFloor extends StandEntityAction {
    public FreezFloor(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){

            SoundEvent frez = ((WhiteAlbumEntity) standEntity).geFreezeWater()?InitSounds.WHITE_ALBUM_UNFREEZE.get():InitSounds.WHITE_ALBUM_WALKER.get();

            world.playSound(null,standEntity.blockPosition(),frez,SoundCategory.PLAYERS,1,1);

            ((WhiteAlbumEntity) standEntity).setFreezeWater( !((WhiteAlbumEntity) standEntity).geFreezeWater());
        }
    }
}
