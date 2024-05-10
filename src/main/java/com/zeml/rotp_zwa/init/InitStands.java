package com.zeml.rotp_zwa.init;

import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.power.impl.stand.StandInstance;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.action.stand.FrozenBlock;
import com.zeml.rotp_zwa.action.stand.ReflectProyectile;
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

    // ======================================== Hermit Purple ========================================

    public static final RegistryObject<StandEntityAction> WA_BLOCK = ACTIONS.register("wa_block",
            () -> new FrozenBlock(new StandEntityAction.Builder().holdType().standSound(InitSounds.WHITE_ALBUM_BLOCK)
                    ));

    public static final RegistryObject<StandEntityAction> REFLECT_PRO = ACTIONS.register("wa_reflec",
            () -> new ReflectProyectile(new StandEntityAction.Builder().holdType().staminaCostTick(5)
                    .standSound(InitSounds.WHITE_ALBUM_BLOCK).standUserWalkSpeed(1.0F).cooldown(120)
            ));



    public static final EntityStandRegistryObject<EntityStandType<StandStats>, StandEntityType<WhiteAlbumEntity>> STAND_WHITE_ALBUM =
            new EntityStandRegistryObject<>("white_album",
                    STANDS,
                    () -> new EntityStandType.Builder<StandStats>()
                            .color(0xD1E7F2)
                            .storyPartName(ModStandsInit.PART_5_NAME)
                            .leftClickHotbar(


                            )
                            .rightClickHotbar(
                                    WA_BLOCK.get(),
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
                    () -> new StandEntityType<WhiteAlbumEntity>(WhiteAlbumEntity::new, 0.1F, 0.1F)
                            .summonSound(InitSounds.WHITE_ALBUM_SUMMON)
                            .unsummonSound(InitSounds.WHITE_ALBUM_UNSUMMON))
                    .withDefaultStandAttributes();
}
