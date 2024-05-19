package com.zeml.rotp_zwa.init;

import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class InitTags {
    public static final Tags.IOptionalNamedTag<EntityType<?>> NO_REFLECT = EntityTypeTags.createOptional(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"irreflectable"));

    public static final Tags.IOptionalNamedTag<Block> ICE_SPEED = BlockTags.createOptional(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"ice_speed"));

    public static void iniTags(){}
}
