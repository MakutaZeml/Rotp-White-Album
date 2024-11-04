package com.zeml.rotp_zwa.entity.stand.stands;


import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.capability.LivingData;
import com.zeml.rotp_zwa.capability.LivingDataProvider;
import com.zeml.rotp_zwa.init.InitBlocks;
import com.zeml.rotp_zwa.init.InitTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;

public class WhiteAlbumEntity extends StandEntity {
    private static final DataParameter<Boolean> FREEZE_WATER = EntityDataManager.defineId(WhiteAlbumEntity.class, DataSerializers.BOOLEAN);


    public WhiteAlbumEntity(StandEntityType<WhiteAlbumEntity> type, World world){
        super(type, world);
        unsummonOffset = getDefaultOffsetFromUser().copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FREEZE_WATER,false);

    }
    private final StandRelativeOffset offsetDefault = StandRelativeOffset.withYOffset(0, 0, 0);

    @Override
    public void tick() {
        super.tick();
        if(!level.isClientSide){
            this.addEffect(new EffectInstance(ModStatusEffects.FULL_INVISIBILITY.get(),20,0,false,false));
        }
        if(getUser() != null){
            LivingEntity standUser = getUser();
            LazyOptional<LivingData> playerDataOptional = standUser.getCapability(LivingDataProvider.CAPABILITY);
            playerDataOptional.ifPresent(livingData -> {
                if(livingData.isFreezeFloor()){
                    if(!level.isClientSide){
                        frostWalkerImitation(this.getUser(),level,this.getUser().blockPosition(),3F);
                        IStandPower.getStandPowerOptional(this.getUser()).ifPresent(power -> power.consumeStamina(1));
                    }
                }
            });

        }

    }


    @Override
    public boolean isPickable(){ return false;}

	public StandRelativeOffset getDefaultOffsetFromUser() {return offsetDefault;}

    private static final BlockState ICE = Blocks.FROSTED_ICE.defaultBlockState();
    private static final  BlockState ICE_LAYER = InitBlocks.WHITE_ALBUM_ICE_LAYER.get().defaultBlockState();

    private void frostWalkerImitation(LivingEntity entity, World world, BlockPos entityPos, float radius) {
            BlockPos.Mutable posMutable = new BlockPos.Mutable();
            for (BlockPos blockPos : BlockPos.betweenClosed(entityPos.offset(-radius, -1.0, -radius), entityPos.offset(radius, -1.0, radius))) {
                if (blockPos.closerThan(entity.position(), (double) radius)) {
                    posMutable.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                    BlockState blockState = world.getBlockState(posMutable);
                    if (blockState.getBlock().isAir(blockState, world, posMutable)) {
                        freezeWaterBlock(world, blockPos, entity);
                    }
                }
                setIceLayer(world, blockPos, entity);
            }
    }

    private void freezeWaterBlock(World world, BlockPos blockPos, LivingEntity user) {
        BlockState blockState = world.getBlockState(blockPos);
        boolean isFull = blockState.getBlock() == Blocks.WATER && blockState.getValue(FlowingFluidBlock.LEVEL) == 0;
        if (blockState.getMaterial() == Material.WATER && isFull && ICE.canSurvive(world, blockPos)
                && world.isUnobstructed(ICE, blockPos, ISelectionContext.empty())
                && !ForgeEventFactory.onBlockPlace(user, BlockSnapshot.create(world.dimension(), world, blockPos), Direction.UP)) {
            world.setBlockAndUpdate(blockPos, ICE);
            world.getBlockTicks().scheduleTick(blockPos, Blocks.FROSTED_ICE, MathHelper.nextInt(user.getRandom(), 20, 40));
        }
    }


    private void setIceLayer(World world, BlockPos blockPos, LivingEntity user){
        BlockState blockState = world.getBlockState(blockPos.above());
        BlockState belowBlockState = world.getBlockState(blockPos);
        if ((blockState.getMaterial() == Material.AIR|| blockState.getMaterial() == Material.REPLACEABLE_PLANT)
                && ICE_LAYER.canSurvive(world, blockPos.above())
                && blockState.getMaterial() != Material.WATER && belowBlockState.getMaterial() != Material.AIR
                && !InitTags.ICE_SPEED.contains(belowBlockState.getBlock())
                && belowBlockState.isCollisionShapeFullBlock(world,blockPos)
                && world.isUnobstructed(ICE_LAYER, blockPos.above(), ISelectionContext.empty())
                && !ForgeEventFactory.onBlockPlace(user, BlockSnapshot.create(world.dimension(), world, blockPos.above()), Direction.UP)) {
            world.setBlockAndUpdate(blockPos.above(), ICE_LAYER);
            world.getBlockTicks().scheduleTick(blockPos.above(), InitBlocks.WHITE_ALBUM_ICE_LAYER.get(), MathHelper.nextInt(user.getRandom(), 20, 40));
        }
    }




    public boolean geFreezeWater(){
        return entityData.get(FREEZE_WATER);
    }


}
