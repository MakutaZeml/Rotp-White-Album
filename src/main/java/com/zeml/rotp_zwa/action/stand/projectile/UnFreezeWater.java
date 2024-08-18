package com.zeml.rotp_zwa.action.stand.projectile;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.init.InitBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

public class UnFreezeWater extends StandEntityAction {

    public UnFreezeWater(StandEntityAction.Builder builder) {
        super(builder);
    }



    @Override
    public void standPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){
            unFrostWalkerImitation(userPower.getUser(),world,userPower.getUser().blockPosition(),9);
        }
    }


    private void unFrostWalkerImitation(LivingEntity entity, World world, BlockPos entityPos, float radius) {
        BlockPos.Mutable posMutable = new BlockPos.Mutable();
        for (BlockPos blockPos : BlockPos.betweenClosed(entityPos.offset(-radius, -radius, -radius), entityPos.offset(radius, radius, radius))) {
            if (blockPos.closerThan(entity.position(), (double) radius)) {
                posMutable.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                BlockState blockState = world.getBlockState(posMutable);
                if (blockState.getBlock().isAir(blockState, world, posMutable)) {
                    unfreezeWaterBlock(world, blockPos, entity);
                }
            }
            unFreezeLayerAir(world,blockPos);
        }
    }

    private static final BlockState WATER = Blocks.WATER.defaultBlockState();
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private void unfreezeWaterBlock(World world, BlockPos blockPos, LivingEntity user) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() == Blocks.FROSTED_ICE) {
            world.setBlockAndUpdate(blockPos, WATER);
        }
    }

    private void unFreezeLayerAir(World world, BlockPos blockPos){
        BlockState blockState = world.getBlockState(blockPos.above());
        if(blockState.getBlock() == InitBlocks.WHITE_ALBUM_ICE_LAYER.get()){
            world.setBlockAndUpdate(blockPos.above(),AIR);
        }
    }

}
