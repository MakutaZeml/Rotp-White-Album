package com.zeml.rotp_zwa.action.stand.projectile;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.LiquidOnlyRayTraceContext;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FreezeProjectile extends StandEntityAction {

    public FreezeProjectile(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target){
        if (user.level.dimensionType().ultraWarm()) {
            return conditionMessage("ultrawarm");
        }
        return ActionConditionResult.POSITIVE;
    }


    @Override
    public void standTickPerform(@NotNull World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task){
        if(!world.isClientSide){
            world.getEntitiesOfClass(LivingEntity.class,userPower.getUser().getBoundingBox().inflate(5F), EntityPredicates.ENTITY_STILL_ALIVE).forEach(
                    entity -> {
                        if(entity.distanceTo(standEntity)<5 && entity != userPower.getUser() && entity != standEntity){

                            float a = (float) standEntity.getAttackDamage();
                            float c = (float) (standEntity.getMaxRange()*2.6/10);
                            float damage = (float) (a*Math.exp(-entity.distanceTo(standEntity)*entity.distanceTo(standEntity)/(2*c*c)))/2;
                            if(entity instanceof StandEntity && ((StandEntity) entity).getUser().distanceTo(standEntity)>5){
                                DamageUtil.dealColdDamage(((StandEntity)entity).getUser(),damage,userPower.getUser(),null);
                                ((StandEntity)entity).getUser().addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),100));
                            }else if(!(entity instanceof StandEntity)){
                                if(!entity.isOnFire()){
                                    DamageUtil.dealColdDamage(entity,damage,userPower.getUser(),null);
                                }
                            }
                        }
                    }
            );
        }
    }


    @Override
    protected void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (!world.isClientSide() && requirementsFulfilled) {
            if (target.getType() == ActionTarget.TargetType.ENTITY) {
                Entity entityTarget = target.getEntity();
                if (entityTarget instanceof LivingEntity && !entityTarget.isOnFire()) {
                    int difficulty = world.getDifficulty().getId();
                    LivingEntity targetLiving = (LivingEntity) entityTarget;
                    float damage = 2;
                    if (targetLiving.getType() == EntityType.SKELETON && targetLiving.isAlive() && targetLiving.getHealth() <= damage) {
                        turnSkeletonIntoStray(targetLiving);
                    }
                    else if (DamageUtil.dealColdDamage(targetLiving, damage, user, null)) {
                        EffectInstance freezeInstance = targetLiving.getEffect(ModStatusEffects.FREEZE.get());
                        if (freezeInstance == null) {
                            world.playSound(null, targetLiving, ModSounds.VAMPIRE_FREEZE.get(), targetLiving.getSoundSource(), 1.0F, 1.0F);
                            targetLiving.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(), (difficulty + 1) * 50, 0));
                        }
                        else {
                            int additionalDuration = (difficulty - 1) * 5 + 1;
                            int duration = freezeInstance.getDuration() + additionalDuration;
                            int lvl = duration / 100;
                            targetLiving.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(), duration, lvl));
                        }
                    }
                }
            }
            frostWalkerImitation(user, world, user.blockPosition(), 4);
        }
    }


    public static boolean turnSkeletonIntoStray(LivingEntity skeleton) {
        if (skeleton.level.isClientSide()) return false;
        ServerWorld world = (ServerWorld) skeleton.level;
        if ((world.getDifficulty() == Difficulty.NORMAL && skeleton.getRandom().nextBoolean() || world.getDifficulty() == Difficulty.HARD)) {
            StrayEntity stray = null;
            if (ForgeEventFactory.canLivingConvert(skeleton, EntityType.STRAY, (timer) -> {})) {
                stray = ((MobEntity) skeleton).convertTo(EntityType.STRAY, true);
            }
            else {
                return false;
            }
            ForgeEventFactory.onLivingConvert(skeleton, stray);
            if (!skeleton.isSilent()) {
                world.levelEvent(null, 1026, skeleton.blockPosition(), 0);
            }
            return true;
        }
        return false;
    }

    public void frostWalkerImitation(LivingEntity entity, World world, BlockPos entityPos, float radius) {
        if (entity.isOnGround()) {
            BlockPos.Mutable posMutable = new BlockPos.Mutable();
            for (BlockPos blockPos : BlockPos.betweenClosed(entityPos.offset(-radius, -1.0, -radius), entityPos.offset(radius, -1.0, radius))) {
                if (blockPos.closerThan(entity.position(), (double) radius)) {
                    posMutable.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
                    BlockState blockState = world.getBlockState(posMutable);
                    if (blockState.getBlock().isAir(blockState, world, posMutable)) {
                        freezeWaterBlock(world, blockPos, entity);
                    }
                }
            }
        }

        Vector3d eyePos = entity.getEyePosition(1.0F);
        Vector3d lookVec = entity.getLookAngle();
        RayTraceResult rayTraceResult = world.clip(new LiquidOnlyRayTraceContext(
                eyePos.add(lookVec), eyePos.add(lookVec.scale(8)), RayTraceContext.FluidMode.SOURCE_ONLY, entity));
        if (rayTraceResult.getType() == RayTraceResult.Type.BLOCK) {
            freezeWaterBlock(world, ((BlockRayTraceResult) rayTraceResult).getBlockPos(), entity);
        }
    }

    private static final BlockState ICE = Blocks.FROSTED_ICE.defaultBlockState();
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


    @Override
    public boolean cancelHeldOnGettingAttacked(IStandPower power, DamageSource dmgSource, float dmgAmount) {
        return true;
    }
}
