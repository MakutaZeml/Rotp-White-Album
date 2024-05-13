package com.zeml.rotp_zwa.block;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.block.BlockState;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class WAFrostedIce extends FrostedIceBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public WAFrostedIce(Properties p_i48394_1_) {
        super(p_i48394_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)));
    }

    @Override
    public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random){
        world.players().forEach(player -> {
            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
                StandType<?> wa = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(power.getType() == wa){
                    if(blockPos.distSqr(player.getX(),player.getY(),player.getZ(),false)<=9){
                        world.setBlock(blockPos,blockState.setValue(AGE,Integer.valueOf(0)),2);
                    }
                }
            });
        });
    }
}
