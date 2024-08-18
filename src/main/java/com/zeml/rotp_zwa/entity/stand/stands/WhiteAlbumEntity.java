package com.zeml.rotp_zwa.entity.stand.stands;


import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.OwnerBoundProjectileEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandRelativeOffset;
import com.github.standobyte.jojo.entity.stand.StandEntityType;

import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.zeml.rotp_zwa.action.stand.FreezFloor;
import com.zeml.rotp_zwa.action.stand.ReflectProyectile;
import com.zeml.rotp_zwa.init.InitBlocks;
import com.zeml.rotp_zwa.init.InitParticles;
import com.zeml.rotp_zwa.init.InitSounds;
import com.zeml.rotp_zwa.init.InitTags;
import com.zeml.rotp_zwa.network.AddonPackets;
import com.zeml.rotp_zwa.network.server.ActiveFreezFloorPacket;
import com.zeml.rotp_zwa.util.GameplayHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.List;
import java.util.Objects;

public class WhiteAlbumEntity extends StandEntity {
    private static final DataParameter<Boolean> REFLECT = EntityDataManager.defineId(WhiteAlbumEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> FREEZE_WATER = EntityDataManager.defineId(WhiteAlbumEntity.class, DataSerializers.BOOLEAN);


    public WhiteAlbumEntity(StandEntityType<WhiteAlbumEntity> type, World world){
        super(type, world);
        unsummonOffset = getDefaultOffsetFromUser().copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REFLECT, false);
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
            IStandPower.getStandPowerOptional(this.getUser()).ifPresent(power -> {

                if(getReflect()){
                    if(!level.isClientSide){
                        power.consumeStamina(7);
                        level.getEntitiesOfClass(ProjectileEntity.class, Objects.requireNonNull(this.getUser()).getBoundingBox().inflate(6.5),EntityPredicates.ENTITY_STILL_ALIVE).stream()
                                .filter(projectileEntity ->projectileEntity.getDeltaMovement().length() > 7)
                                .filter(projectileEntity -> !(projectileEntity instanceof OwnerBoundProjectileEntity))

                                .forEach(projectileEntity -> {
                            if(!InitTags.NO_REFLECT.contains(projectileEntity.getType())){
                                RayTraceResult result = level.clip(new RayTraceContext(
                                        projectileEntity.position(),
                                        projectileEntity.position().add(projectileEntity.getDeltaMovement()),
                                        RayTraceContext.BlockMode.COLLIDER,
                                        RayTraceContext.FluidMode.NONE,
                                        projectileEntity));

                                if(result.getType() == RayTraceResult.Type.MISS){
                                    JojoModUtil.deflectProjectile(projectileEntity,projectileEntity.getDeltaMovement().reverse());
                                    power.consumeStamina(14);
                                    level.playSound(null,projectileEntity.blockPosition(), InitSounds.WHITE_ALBUM_WEEPS.get(), SoundCategory.PLAYERS,0.7F,1);
                                    level.playSound(null,projectileEntity.blockPosition(), InitSounds.WHITE_ALBUM_DEFLECT.get(), SoundCategory.PLAYERS,1,1);

                                }

                            }
                        });
                        level.getEntitiesOfClass(ProjectileEntity.class, Objects.requireNonNull(this.getUser()).getBoundingBox().inflate(2),EntityPredicates.ENTITY_STILL_ALIVE).stream()
                                .filter(projectileEntity -> !(projectileEntity instanceof OwnerBoundProjectileEntity))
                                .filter(projectileEntity ->projectileEntity.getDeltaMovement().length() <= 7).forEach(projectileEntity -> {

                            if(!InitTags.NO_REFLECT.contains(projectileEntity.getType())){
                                RayTraceResult result = level.clip(new RayTraceContext(
                                        projectileEntity.position(),
                                        projectileEntity.position().add(projectileEntity.getDeltaMovement()),
                                        RayTraceContext.BlockMode.COLLIDER,
                                        RayTraceContext.FluidMode.NONE,
                                        projectileEntity));

                                if(result.getType() == RayTraceResult.Type.MISS){
                                    JojoModUtil.deflectProjectile(projectileEntity,projectileEntity.getDeltaMovement().reverse());
                                    power.consumeStamina(14);
                                    level.playSound(null,projectileEntity.blockPosition(), InitSounds.WHITE_ALBUM_WEEPS.get(), SoundCategory.PLAYERS,0.7F,1);
                                    level.playSound(null,projectileEntity.blockPosition(), InitSounds.WHITE_ALBUM_DEFLECT.get(), SoundCategory.PLAYERS,1,1);
                                }
                            }
                        });
                        if(power.getStamina()==0){
                            if(getReflect()){
                                setReflect(false);
                                level.playSound(null,this.blockPosition(), InitSounds.WHITE_ALBUM_WEEPS_UNSUMMON.get(), SoundCategory.PLAYERS,1,1);

                            }
                            ReflectProyectile.active = false;
                            FreezFloor.active = false;
                            setFreezeWater(false);
                            if(this.getUser() instanceof ServerPlayerEntity){
                                AddonPackets.sendToClient(new ActiveFreezFloorPacket(this.getUser().getId(),false), (ServerPlayerEntity) this.getUser());
                            }
                        }
                    }

                    if(level.isClientSide){

                        level.addParticle(InitParticles.ICE.get(),
                                this.getUser().getX()+3*this.getUser().getBbWidth()*(random.nextFloat()-.5),
                                this.getUser().getY()+this.getUser().getBbHeight()*(random.nextFloat()),
                                this.getUser().getZ()+3*this.getUser().getBbWidth()*(random.nextFloat()-.5),
                                0,.5,0
                        );
                    }

                }

                if(this.geFreezeWater()){
                    if(!level.isClientSide){
                        frostWalkerImitation(this.getUser(),level,this.getUser().blockPosition(),3F);
                        power.consumeStamina(1);
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

    public void setReflect(boolean active){
        entityData.set(REFLECT,active);
    }

    public boolean getReflect(){
        return entityData.get(REFLECT);
    }

    public void setFreezeWater(boolean freezeWater){
        entityData.set(FREEZE_WATER,freezeWater);
    }

    public boolean geFreezeWater(){
        return entityData.get(FREEZE_WATER);
    }


}
