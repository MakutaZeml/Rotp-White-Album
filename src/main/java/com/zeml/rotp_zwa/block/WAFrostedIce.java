package com.zeml.rotp_zwa.block;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WAFrostedIce extends FrostedIceBlock {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public WAFrostedIce(Properties p_i48394_1_) {
        super(p_i48394_1_);
    }

    @Override
    public void playerDestroy(World p_180657_1_, PlayerEntity p_180657_2_, BlockPos p_180657_3_, BlockState p_180657_4_, @Nullable TileEntity p_180657_5_, ItemStack p_180657_6_) {
        super.playerDestroy(p_180657_1_, p_180657_2_, p_180657_3_, p_180657_4_, p_180657_5_, p_180657_6_);
        p_180657_1_.setBlockAndUpdate(p_180657_3_, Blocks.AIR.defaultBlockState());
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }


    @Override
    protected void melt(BlockState p_196454_1_, World p_196454_2_, BlockPos p_196454_3_) {
        p_196454_2_.removeBlock(p_196454_3_, false);
    }
}
