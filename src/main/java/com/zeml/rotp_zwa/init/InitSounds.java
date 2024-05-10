package com.zeml.rotp_zwa.init;

import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.github.standobyte.jojo.util.mc.OstSoundList;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RotpWhiteAlbumAddon.MOD_ID);

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



    public static final RegistryObject<SoundEvent> USER_WA = SOUNDS.register("ghaccio_wa",
            ()->new SoundEvent(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "ghaccio_wa"))
            );


	
    static final OstSoundList GACCHIO_OST = new OstSoundList(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "wa_ost"), SOUNDS);
}
