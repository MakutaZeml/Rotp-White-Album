package com.zeml.rotp_zwa.util;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.entity.damaging.projectile.MRCrossfireHurricaneEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.MRFireballEntity;
import com.github.standobyte.jojo.entity.damaging.projectile.MRFlameEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModEntityTypes;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.init.power.non_stand.ModPowers;
import com.github.standobyte.jojo.init.power.non_stand.hamon.ModHamonSkills;
import com.github.standobyte.jojo.power.impl.nonstand.INonStandPower;
import com.github.standobyte.jojo.power.impl.nonstand.type.hamon.HamonData;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import com.github.standobyte.jojo.util.mc.damage.StandDamageSource;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.init.InitEntities;
import com.zeml.rotp_zwa.init.InitSounds;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;


@Mod.EventBusSubscriber(modid = RotpWhiteAlbumAddon.MOD_ID)
public class GameplayHandler {


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        IStandPower.getStandPowerOptional(player).ifPresent(power -> {
            StandType<?> wa = InitStands.STAND_WHITE_ALBUM.getStandType();
            /**/

            if(power.getType() == wa ){
                if(power.getStandManifestation() instanceof StandEntity){
                    List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(11),EntityPredicates.ENTITY_STILL_ALIVE);
                    list.forEach(entity -> {
                        if(entity.distanceTo(player) <=10 && (entity != player)){
                            int amount = Math.round(2-entity.distanceTo(player)/5);
                            entity.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),20,amount));
                        }
                    });
                    player.removeEffect(ModStatusEffects.FREEZE.get());
                }
            }
            if(power.getHeldAction()==InitStands.WA_BLOCK.get()){
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(11),EntityPredicates.ENTITY_STILL_ALIVE);
                list.forEach(entity -> {
                    if(entity.distanceTo(player) <=10 && (entity != player)){
                        int amount = Math.round(4-2*entity.distanceTo(player)/5);
                        entity.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),20,amount));
                    }
                });
            }
            if(power.getHeldAction() == InitStands.REFLECT_PRO.get()){
                List<ProjectileEntity> list =  player.level.getEntitiesOfClass(ProjectileEntity.class,player.getBoundingBox().inflate(1.4),EntityPredicates.ENTITY_STILL_ALIVE);
                list.forEach(projectileEntity -> {

                    JojoModUtil.deflectProjectile(projectileEntity,projectileEntity.getDeltaMovement().reverse());
                });
                if(power.getStamina()==0){
                    power.stopHeldAction(false);
                }

            }

        });

    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void blockDamage(LivingHurtEvent event){
        DamageSource dmgSource = event.getSource();
        LivingEntity target = event.getEntityLiving();
        if(dmgSource.getDirectEntity() != null){
            Entity ent = dmgSource.getDirectEntity();

            IStandPower.getStandPowerOptional(target).ifPresent(
                    standPower ->{
                        StandType<?> wa = InitStands.STAND_WHITE_ALBUM.getStandType();
                        if(standPower.getType() == wa && standPower.getStandManifestation() instanceof StandEntity){
                            if(ent instanceof LivingEntity){
                                LivingEntity living = (LivingEntity) ent;
                                living.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),20));
                            }
                        }

                        if(standPower.getHeldAction()==InitStands.WA_BLOCK.get()){
                            if(ent instanceof LivingEntity){
                                LivingEntity liv = (LivingEntity) ent;
                                liv.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),30));
                                event.setAmount(Math.max(event.getAmount() - 18.F / 2F, 0));
                            }
                            if(dmgSource instanceof StandDamageSource){
                                double conf = JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get();
                                event.setAmount(Math.max(event.getAmount() - (float) conf*18F / 2F, 0));
                            }

                            if (dmgSource.isProjectile()) {
                                if(!(ent instanceof MRFireballEntity) && !(ent instanceof MRCrossfireHurricaneEntity) &&
                                        !(ent instanceof MRFlameEntity)&& !(ent instanceof FireballEntity) &&
                                !(ent instanceof SmallFireballEntity)){
                                    event.setAmount(0);
                                }else {
                                    event.setAmount(Math.max(event.getAmount() - 18.F / 2F, 0));
                                }

                            }
                            if(standPower.getStamina()==0){
                                standPower.stopHeldAction(false);
                            }
                        }

                    });
        }
    }

}
