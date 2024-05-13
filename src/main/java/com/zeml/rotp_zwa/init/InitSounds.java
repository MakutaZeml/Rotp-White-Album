package com.zeml.rotp_zwa.init;

import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpWhiteAlbumAddon.MOD_ID);

    public static final Supplier<SoundEvent> WHITE_ALBUM_PUNCH_LIGHT = SOUNDS.register("wa_punch",
            ()-> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"wa_punch")));

    public static final Supplier<SoundEvent> WHITE_ALBUM_PUNCH_HEAVY = SOUNDS.register("wa_heavy_punch",
            ()-> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"wa_heavy_punch")));

    public static final RegistryObject<SoundEvent> GHACCIO_MAN_ORA = SOUNDS.register("wa_ora",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_ora")));

    public static final RegistryObject<SoundEvent> GACCHIO_MAN_ORA_LONG = SOUNDS.register("wa_ora_long",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_ora_long")));
    public static final RegistryObject<SoundEvent> WHITE_ALBUM_SUMMON = SOUNDS.register("wa_summon",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_summon")));

    public static final RegistryObject<SoundEvent> HP_GRAPPLE_CATCH =SOUNDS.register("wa_grap",
            ()->new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"wa_grap")));

    public static final RegistryObject<SoundEvent> HP_GRAPPLE_ENT =SOUNDS.register("wa_grp_ent",
            ()->new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"wa_grp_ent")));

    public static final RegistryObject<SoundEvent> VINE_TRHOW =SOUNDS.register("wa_throw",
            ()->new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"wa_throw")));

    public static final RegistryObject<SoundEvent> VOID =SOUNDS.register("void",
            ()->new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"void")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_UNSUMMON = SOUNDS.register("wa_unummon",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_unsummon")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_BLOCK = SOUNDS.register("ice_shield",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "ice_shield")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_DEFLECT = SOUNDS.register("wa_deflect",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_deflect")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_WEEPS = SOUNDS.register("wa_gw",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_gw")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_WEEPS_SUMMON = SOUNDS.register("wa_gw_su",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_gw_su")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_WEEPS_UNSUMMON = SOUNDS.register("wa_gw_usu",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_gw_usu")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_UNFREEZE = SOUNDS.register("wa_unfreeze",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_unfreeze")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_FREEZE = SOUNDS.register("wa_freeze",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_freeze")));

    public static final RegistryObject<SoundEvent> WHITE_ALBUM_WALKER = SOUNDS.register("wa_walker",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_walker")));

    public static final RegistryObject<SoundEvent> USER_WEEPS_SUMMON = SOUNDS.register("us_gw_su",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "us_gw_su")));

    public static final RegistryObject<SoundEvent> USER_WEEPS_UNSUMMON = SOUNDS.register("us_gw_usu",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "us_gw_usu")));

    public static final RegistryObject<SoundEvent> USER_WA = SOUNDS.register("ghaccio_wa",
            ()->new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "ghaccio_wa"))
            );

    public static final RegistryObject<SoundEvent> USER_FREEZE = SOUNDS.register("us_freeze",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "us_freeze")));

    public static final RegistryObject<SoundEvent> USER_UNFREEZE = SOUNDS.register("us_unfreeze",
            () -> new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "us_unfreeze")));
	
    static final OstSoundList GACCHIO_OST = new OstSoundList(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_ost"), SOUNDS);
}
