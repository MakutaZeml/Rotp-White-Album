package com.zeml.rotp_zwa.init;

import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class InitTags {
    public static final Tags.IOptionalNamedTag<EntityType<?>> FIRE_PROJECTILE = EntityTypeTags.createOptional(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"fire_projectile"));

    public static final Tags.IOptionalNamedTag<Block> ICE_SPEED = BlockTags.createOptional(new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"ice_speed"));

    public static void iniTags(){}
}
