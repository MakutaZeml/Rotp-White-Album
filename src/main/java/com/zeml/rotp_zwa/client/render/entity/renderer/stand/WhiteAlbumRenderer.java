package com.zeml.rotp_zwa.client.render.entity.renderer.stand;

import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.client.render.entity.model.stand.WhiteAlbumModel;
import com.zeml.rotp_zwa.entity.stand.stands.WhiteAlbumEntity;
import com.github.standobyte.jojo.client.render.entity.renderer.stand.StandEntityRenderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WhiteAlbumRenderer extends StandEntityRenderer<WhiteAlbumEntity, WhiteAlbumModel> {

    public WhiteAlbumRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WhiteAlbumModel(), new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID, "textures/entity/stand/white_album_stand.png"), 0);
    }

}
