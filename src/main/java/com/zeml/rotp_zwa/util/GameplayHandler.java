package com.zeml.rotp_zwa.util;

import com.github.standobyte.jojo.JojoModConfig;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModStatusEffects;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import com.github.standobyte.jojo.util.mc.damage.StandDamageSource;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.action.stand.FreezFloor;
import com.zeml.rotp_zwa.action.stand.ReflectProyectile;
import com.zeml.rotp_zwa.init.InitStands;
import com.zeml.rotp_zwa.init.InitTags;
import com.zeml.rotp_zwa.network.AddonPackets;
import com.zeml.rotp_zwa.network.server.ActiveFreezFloorPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Mod.EventBusSubscriber(modid = RotpWhiteAlbumAddon.MOD_ID)
public class GameplayHandler {
    static boolean once = false;
    static double dmg;
    static double dig;


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
                        player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED,20,1));
                    }


                }else {
                    Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).setBaseValue(0);
                    ReflectProyectile.active = false;
                    FreezFloor.active = false;
                    if(player instanceof ServerPlayerEntity){
                        AddonPackets.sendToClient(new ActiveFreezFloorPacket(player.getId(),false), (ServerPlayerEntity) player);
                    }
                }

            }else {
                Objects.requireNonNull(player.getAttribute(Attributes.ARMOR)).setBaseValue(0);
                ReflectProyectile.active = false;
                FreezFloor.active = false;
                if(player instanceof ServerPlayerEntity){
                    AddonPackets.sendToClient(new ActiveFreezFloorPacket(player.getId(),false), (ServerPlayerEntity) player);
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
                if(power.getType() == wa) player.removeEffect(ModStatusEffects.FREEZE.get());
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
                        if(standPower.getHeldAction()==InitStands.FREEZE_SHOT.get() && event.getAmount() > 0){
                            standPower.stopHeldAction(false);
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

}
