package com.zeml.rotp_zwa;

import com.zeml.rotp_zwa.init.InitEntities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zeml.rotp_zwa.init.InitSounds;
import com.zeml.rotp_zwa.init.InitStands;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RotpWhiteAlbumAddon.MOD_ID)
public class RotpWhiteAlbumAddon {
    // The value here should match an entry in the META-INF/mods.toml file
    public static final String MOD_ID = "rotp_zwa";
    public static final Logger LOGGER = LogManager.getLogger();

    public RotpWhiteAlbumAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitEntities.ENTITIES.register(modEventBus);
        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitStands.STANDS.register(modEventBus);

    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
