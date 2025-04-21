package com.zeml.rotp_zwa.action.stand;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.PillarmanHeavyPunch;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityLightAttack;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.entity.stand.StandStatFormulas;
import com.github.standobyte.jojo.init.ModSounds;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.zeml.rotp_zwa.init.InitSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.Nullable;

public class WhiteAlbumPunch extends StandEntityLightAttack {
    public WhiteAlbumPunch(StandEntityLightAttack.Builder builder){
        super(builder);
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {

    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target, @Nullable PacketBuffer extraInput) {
        punchPerform(world,user,power,target, InitSounds.WHITE_ALBUM_PUNCH_LIGHT.get(), 1F,1F);
    }

    public static void punchPerform(World world, LivingEntity user, IStandPower power, ActionTarget target, SoundEvent sound, float volume, float pitch) {
        switch (target.getType()) {
            case BLOCK:
                blockDestroy(world, user, target, 0.0, 0.0, 0.0,power);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSounds.HAMON_SYO_PUNCH.get(), user.getSoundSource(), 1.5F, 1.2F);
                break;
            case ENTITY:
                if (!world.isClientSide() && target.getType() == ActionTarget.TargetType.ENTITY) {
                    Entity entity = target.getEntity();
                    if (entity instanceof PlayerEntity) {
                        LivingEntity targetEntity = (LivingEntity)entity;
                        PlayerEntity pEntity = (PlayerEntity)user;
                        if (entity.hurt(EntityDamageSource.playerAttack(pEntity), getDamage(world, power))) {
                            world.playSound(null, targetEntity.getX(), targetEntity.getEyeY(), targetEntity.getZ(), sound, targetEntity.getSoundSource(), volume, pitch);
                            targetEntity.knockback(2.0F, user.getX() - targetEntity.getX(), user.getZ() - targetEntity.getZ());
                        }
                    }else if(entity instanceof LivingEntity){
                        LivingEntity targetEntity = (LivingEntity)entity;
                        LivingEntity mob = user;
                        if (entity.hurt(EntityDamageSource.mobAttack(mob), getDamage(world, power))) {
                            world.playSound(null, targetEntity.getX(), targetEntity.getEyeY(), targetEntity.getZ(), sound, targetEntity.getSoundSource(), volume, pitch);
                            targetEntity.knockback(2.0F, user.getX() - targetEntity.getX(), user.getZ() - targetEntity.getZ());
                        }
                    }
                }
        }

    }

    @Override
    public void onTaskSet(World world, StandEntity standEntity, IStandPower standPower, Phase phase, StandEntityTask task, int ticks) {
        if(!world.isClientSide){
            LivingEntity user = standPower.getUser();
            if(user != null){
                user.addEffect(new EffectInstance(ModStatusEffects.INTEGRATED_STAND.get(),40,0,false,false,false));
            }
        }
    }


    @Override
    protected void preTaskInit(World world, IStandPower standPower, StandEntity standEntity, ActionTarget target) {
        if(!world.isClientSide){
            LivingEntity user = standPower.getUser();
            if(user != null){
                user.addEffect(new EffectInstance(ModStatusEffects.INTEGRATED_STAND.get(),40));
            }
        }
    }

    public static void blockDestroy(World world, LivingEntity user, ActionTarget target, double x, double y, double z, IStandPower power) {
        BlockPos pos = target.getBlockPos().offset(x, y, z);
        if (!world.isClientSide() && StandStatFormulas.isBlockBreakable(power.getType().getStats().getBasePower(),  world.getBlockState(pos).getDestroySpeed(world,pos), world.getBlockState(pos).getHarvestLevel()) && !world.isEmptyBlock(pos)) {
            BlockState blockState = world.getBlockState(pos);
            float digDuration = blockState.getDestroySpeed(world, pos);
            boolean dropItem = true;
            if (user instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)user;
                digDuration /= player.getDigSpeed(blockState, pos) / 2.0F;
                if (player.abilities.instabuild) {
                    digDuration = 0.0F;
                    dropItem = false;
                } else if (!ForgeHooks.canHarvestBlock(blockState, player, world, pos)) {
                    digDuration *= 0.33333334F;
                }
            }

            if (digDuration >= 0.0F && (double)digDuration <= 2.5 * Math.sqrt(user.getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                world.destroyBlock(pos, dropItem);
            } else {
                SoundType soundType = blockState.getSoundType(world, pos, user);
                world.playSound(null, pos, soundType.getHitSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 8.0F, soundType.getPitch() * 0.5F);
            }
        }

    }

    public static float getDamage(World world, IStandPower power) {
        double powerStat = power.getType().getStats().getBasePower();
        return StandStatFormulas.getLightAttackDamage(powerStat);
    }

    @Override
    public boolean cancelsVanillaClick() {
        return true;
    }
}
