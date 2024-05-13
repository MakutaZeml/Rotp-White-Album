package com.zeml.rotp_zwa.init;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.StandEntityLightAttack;
import com.github.standobyte.jojo.power.impl.stand.StandInstance;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.action.stand.FreezFloor;
import com.zeml.rotp_zwa.action.stand.FrozenBlock;
import com.zeml.rotp_zwa.action.stand.ReflectProyectile;
import com.zeml.rotp_zwa.action.stand.projectile.FreezeProjectile;
import com.zeml.rotp_zwa.action.stand.projectile.UnFreezeWater;
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.github.standobyte.jojo.init.power.stand.ModStandsInit;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), RotpWhiteAlbumAddon.MOD_ID);
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<StandType<?>> STANDS = DeferredRegister.create(
            (Class<StandType<?>>) ((Class<?>) StandType.class), RotpWhiteAlbumAddon.MOD_ID);

    // ======================================== White Album ========================================

    public static final RegistryObject<StandEntityAction> WHITE_ALBUM_PUNCH = ACTIONS.register("wa_punch",
            () -> new StandEntityLightAttack(new StandEntityLightAttack.Builder()
                    .punchSound(InitSounds.WHITE_ALBUM_PUNCH_LIGHT).swingHand().standOffsetFromUser(0,0,.5)
                    .standSound(StandEntityAction.Phase.WINDUP, InitSounds.GHACCIO_MAN_ORA)));

    public static final RegistryObject<StandEntityAction> WA_BLOCK = ACTIONS.register("wa_block",
            () -> new FrozenBlock(new StandEntityAction.Builder().holdType().standSound(InitSounds.WHITE_ALBUM_BLOCK)
                    .cooldown(60).staminaCostTick(6)
                    ));

    public static final RegistryObject<StandEntityAction> FREEZE_SHOT = ACTIONS.register("wa_freeze",
            () -> new FreezeProjectile(new StandEntityAction.Builder().holdType().staminaCostTick(2)
                    .standUserWalkSpeed(1.0F).cooldown(30).resolveLevelToUnlock(2)
                    .standSound(InitSounds.WHITE_ALBUM_FREEZE).standSound(InitSounds.USER_FREEZE)
            ));

    public static final RegistryObject<StandEntityAction> FREEZE_WATER = ACTIONS.register("wa_freeze_water",
            () -> new FreezFloor(new StandEntityAction.Builder()
                    .standUserWalkSpeed(1.0F).cooldown(30).resolveLevelToUnlock(1)
            ));

    public static final RegistryObject<StandEntityAction> UNFREEZE = ACTIONS.register("wa_unfreeze",
            () -> new UnFreezeWater(new StandEntityAction.Builder().staminaCost(60).standSound(InitSounds.USER_UNFREEZE)
                    .cooldown(40).shiftVariationOf(FREEZE_SHOT).standSound(InitSounds.WHITE_ALBUM_UNFREEZE)
            ));

    public static final RegistryObject<StandEntityAction> REFLECT_PRO = ACTIONS.register("wa_reflec",
            () -> new ReflectProyectile(new StandEntityAction.Builder()
                    .standUserWalkSpeed(1.0F).cooldown(20).resolveLevelToUnlock(4)
            ));



    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<WhiteAlbumEntity>> STAND_WHITE_ALBUM =
            new EntityStandRegistryObject<>("white_album",
                    STANDS,
                    () -> new EntityStandType.Builder<StandStats>()
                            .color(0xD1E7F2)
                            .storyPartName(ModStandsInit.PART_5_NAME)
                            .leftClickHotbar(
                                    WHITE_ALBUM_PUNCH.get(),
                                    FREEZE_SHOT.get()
                            )
                            .rightClickHotbar(
                                    WA_BLOCK.get(),
                                    FREEZE_WATER.get(),
                                    REFLECT_PRO.get()

                            )
                            .defaultStats(StandStats.class, new StandStats.Builder()
                                    .tier(6)
                                    .power(14.0)
                                    .speed(10.0)
                                    .range(10.0)
                                    .durability(18.0)
                                    .precision(3.0)
                                    .randomWeight(1)
                            )
                            .addOst(InitSounds.GACCHIO_OST)
                            .disableManualControl().addSummonShout(InitSounds.USER_WA)
                            .build(),

                    InitEntities.ENTITIES,
                    () -> new StandEntityType<WhiteAlbumEntity>(WhiteAlbumEntity::new, 0.65F, 1.95F)
                            .summonSound(InitSounds.WHITE_ALBUM_SUMMON)
                            .unsummonSound(InitSounds.WHITE_ALBUM_UNSUMMON))
                    .withDefaultStandAttributes();
}
