package com.zeml.rotp_zwa.util;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.entity.damaging.projectile.ownerbound.OwnerBoundProjectileEntity;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import com.github.standobyte.jojo.util.mc.damage.StandDamageSource;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.capability.LivingData;
import com.zeml.rotp_zwa.capability.LivingDataProvider;
import com.zeml.rotp_zwa.init.InitStands;
import com.zeml.rotp_zwa.init.InitTags;
import com.zeml.rotp_zwa.network.AddonPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;


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
                        if(entity.distanceTo(player) <=10 && (entity != player) && !entity.isOnFire()){
                            if(!(entity instanceof PlayerEntity && IStandPower.getPlayerStandPower((PlayerEntity) entity).getType() ==wa && IStandPower.getPlayerStandPower((PlayerEntity) entity).isActive())){
                                int amount = Math.round(2-entity.distanceTo(player)/5);
                                entity.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),20,amount));
                            }

                        }
                    });

                    int armor = 0;

                    for(ItemStack slot: player.getArmorSlots()){
                        if(slot.getItem() instanceof ArmorItem){
                            armor = armor+((ArmorItem) slot.getItem()).getDefense();
                        }
                    }

                    player.getAttribute(Attributes.ARMOR).setBaseValue(Math.min(10,Math.max(0,22-armor)));

                    if(InitTags.ICE_SPEED.contains(player.level.getBlockState(player.blockPosition().below()).getBlock())||
                            InitTags.ICE_LAYER_SPEED.contains(player.level.getBlockState(player.blockPosition()).getBlock())
                    ){
                        player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED,20,2));
                    }
                    LazyOptional<LivingData> targetDataOptional = player.getCapability(LivingDataProvider.CAPABILITY);
                    if (targetDataOptional.map(LivingData::isGentlyWeeps).orElse(false)) {
                        power.consumeStamina(2.5F);
                    }

                }else {
                    Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).setBaseValue(0);
                    LazyOptional<LivingData> playerDataOptional = player.getCapability(LivingDataProvider.CAPABILITY);
                    playerDataOptional.ifPresent(playerData ->{
                        playerData.setGentlyWeeps(false);
                        playerData.setFreezeFloor(false);
                    });


                }

            }else {
                Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).setBaseValue(0);
                LazyOptional<LivingData> playerDataOptional = player.getCapability(LivingDataProvider.CAPABILITY);
                playerDataOptional.ifPresent(playerData ->{
                    playerData.setGentlyWeeps(false);
                    playerData.setFreezeFloor(false);

                });

            }
            if(power.getHeldAction()==InitStands.WA_BLOCK.get()){
                List<LivingEntity> list = player.level.getEntitiesOfClass(LivingEntity.class,player.getBoundingBox().inflate(11),EntityPredicates.ENTITY_STILL_ALIVE);
                list.forEach(entity -> {
                    if(entity.distanceTo(player) <=10 && (entity != player)){
                        int amount = Math.round(4-2*entity.distanceTo(player)/5);
                        entity.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),20,amount));
                    }
                });
                if(power.getStamina() < 2.5){
                    LazyOptional<LivingData> playerDataOptional = player.getCapability(LivingDataProvider.CAPABILITY);
                    playerDataOptional.ifPresent(playerData ->{
                        playerData.setGentlyWeeps(false);
                        playerData.setFreezeFloor(false);
                    });
                }
                if(power.getStamina() ==0){
                    power.stopHeldAction(false);
                }
            }
            if(power.getHeldAction()==InitStands.FREEZE_SHOT.get()){
                if(power.getStamina() ==0){
                    power.stopHeldAction(false);
                }
            }

        });

    }



    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void highPlayerTick(TickEvent.PlayerTickEvent event){
        if(!event.player.level.isClientSide){
            PlayerEntity player = event.player;
            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
                StandType<?> wa = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(power.getType() == wa) {
                    if(power.getStandManifestation() instanceof StandEntity){
                        player.removeEffect(ModStatusEffects.FREEZE.get());
                        if(player.getItemInHand(Hand.MAIN_HAND) == ItemStack.EMPTY){
                            player.addEffect(new EffectInstance(ModStatusEffects.INTEGRATED_STAND.get(),20,0,false,false,false));
                        }
                    }
                    else {
                        player.removeEffect(ModStatusEffects.INTEGRATED_STAND.get());
                    }
                    if(power.getStamina() <= 2){
                        LazyOptional<LivingData> playerDataOptional = player.getCapability(LivingDataProvider.CAPABILITY);
                        playerDataOptional.ifPresent(playerData ->{
                            playerData.setGentlyWeeps(false);
                            playerData.setFreezeFloor(false);
                        });
                    }
                }
            });
        }

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
                                if(dmgSource == DamageUtil.COLD){
                                    event.setCanceled(true);
                                }
                            }

                        }


                        if(standPower.getHeldAction()==InitStands.WA_BLOCK.get()){
                            if(ent instanceof LivingEntity){
                                LivingEntity liv = (LivingEntity) ent;
                                liv.addEffect(new EffectInstance(ModStatusEffects.FREEZE.get(),30));
                                event.setAmount(Math.max(event.getAmount() - 18.F / 2F, 0));

                                if(event.getAmount() >15){
                                    standPower.stopHeldAction(false);
                                }

                            }
                            if(dmgSource instanceof StandDamageSource){
                                double conf = JojoModConfig.getCommonConfigInstance(false).standDamageMultiplier.get();
                                if(event.getAmount() >conf*15){
                                    standPower.stopHeldAction(false);
                                }
                                event.setAmount(Math.max(event.getAmount() - (float) conf*18F / 2F, 0));
                            }

                            if (dmgSource.isProjectile()) {
                                if(!InitTags.NO_REFLECT.contains(ent.getType())){
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



    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void blockSunDamange(LivingAttackEvent event){
        if(!event.getEntity().level.isClientSide){
            LivingEntity entity = event.getEntityLiving();
            IStandPower.getStandPowerOptional(entity).ifPresent(power -> {
                StandType<?> wa = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(power.getType() == wa && power.isActive()){
                    if(event.getSource() == DamageUtil.ULTRAVIOLET){
                        event.setCanceled(true);
                    }
                }
            });
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void betterGentlyWeeps(ProjectileImpactEvent event){
        Entity projectile = event.getEntity();
        if(!projectile.level.isClientSide){
            if (event.getRayTraceResult().getType() == RayTraceResult.Type.ENTITY){
                Entity target = ((EntityRayTraceResult) event.getRayTraceResult()).getEntity();
                if(!(projectile instanceof OwnerBoundProjectileEntity)){
                    if (target instanceof LivingEntity) {
                        LivingEntity livingTarget = (LivingEntity) target;
                        LazyOptional<LivingData> targetDataOptional = livingTarget.getCapability(LivingDataProvider.CAPABILITY);
                        if (targetDataOptional.map(LivingData::isGentlyWeeps).orElse(false)) {
                            projectile.setDeltaMovement(projectile.getDeltaMovement().reverse());
                            event.setCanceled(true);
                            IStandPower.getStandPowerOptional((LivingEntity) target).ifPresent(power -> power.consumeStamina(14));
                            MCUtil.runCommand(livingTarget,"/playsound rotp_zwa:wa_deflect player @s ~ ~ ~");
                            MCUtil.runCommand(livingTarget,"/playsound rotp_zwa:wa_gw player @s ~ ~ ~");
                        }
                    }
                }
            }
        }

    }


}
