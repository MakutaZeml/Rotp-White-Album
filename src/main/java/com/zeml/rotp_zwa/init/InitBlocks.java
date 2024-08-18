package com.zeml.rotp_zwa.init;

import com.github.standobyte.jojo.JojoMod;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.block.WAFrostedIce;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RotpWhiteAlbumAddon.MOD_ID);

    public static final RegistryObject<WAFrostedIce> WHITE_ALBUM_ICE_LAYER =BLOCKS.register("wa_ice_layer",
            ()->new WAFrostedIce(AbstractBlock.Properties.copy(Blocks.FROSTED_ICE).friction(0.98F).isValidSpawn((state, reader, pos, entityType) ->false)));
}
