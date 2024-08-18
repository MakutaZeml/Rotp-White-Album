package com.zeml.rotp_zwa.epicfight;

import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.client.render.entity.renderer.IceShieldLayer;
import com.zeml.rotp_zwa.client.render.entity.renderer.WhiteAlbumLayer;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import yesman.epicfight.api.client.model.ClientModel;
import yesman.epicfight.api.client.model.ClientModels;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class IceShieldLayerEpicFight  <E extends LivingEntity, T extends LivingEntityPatch<E>, M extends BipedModel<E>> extends PatchedLayer<E, T, M, IceShieldLayer<E, M>> {
    public static ResourceLocation WHITE_ALBUM = new  ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"/textures/entity/stand/ice_shield.png");

    @Override
    public void renderLayer(T t, E e, IceShieldLayer<E, M> emIceShieldLayer, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, OpenMatrix4f[] openMatrix4fs, float v, float v1, float v2) {
        IStandPower.getStandPowerOptional(e).ifPresent((stand) -> {
            StandType<?> hm = InitStands.STAND_WHITE_ALBUM.getStandType();
            if (stand.getType() == hm && stand.getStandManifestation() instanceof StandEntity && stand.getHeldAction() == InitStands.WA_BLOCK.get()) {
                matrixStack.pushPose();
                ClientModel model = ClientModels.LOGICAL_CLIENT.biped;
                WHITE_ALBUM = StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                        .getStandSkin(stand.getStandInstance().get()), WHITE_ALBUM);
                model.drawAnimatedModel(matrixStack, iRenderTypeBuffer.getBuffer(EpicFightRenderTypes.animatedModel(WHITE_ALBUM)), i, 1.0F, 1.0F, 1.0F, 1.0F, LivingRenderer.getOverlayCoords(e, 0.0F), openMatrix4fs);
                matrixStack.popPose();
            }
        });
    }
}
