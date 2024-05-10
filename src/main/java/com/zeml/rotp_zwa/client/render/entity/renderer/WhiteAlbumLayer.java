package com.zeml.rotp_zwa.client.render.entity.renderer;

import com.github.standobyte.jojo.client.playeranim.PlayerAnimationHandler;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class WhiteAlbumLayer<T extends LivingEntity, M extends PlayerModel<T>> extends LayerRenderer<T, M> {
    private static final Map<PlayerRenderer, WhiteAlbumLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>> RENDERER_LAYERS = new HashMap<>();
    private final M glovesModel;
    private final boolean slim;
    private boolean playerAnimHandled = false;

    public WhiteAlbumLayer(IEntityRenderer<T, M> renderer, M hermitoModel, boolean slim) {
        super(renderer);
        if(renderer instanceof PlayerRenderer){
            RENDERER_LAYERS.put((PlayerRenderer) renderer,(WhiteAlbumLayer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>>) this);
        }
        this.glovesModel = hermitoModel;
        this.slim = slim;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot){
        if (!playerAnimHandled) {
            PlayerAnimationHandler.getPlayerAnimator().onArmorLayerInit(this);
            playerAnimHandled = true;
        }

        IStandPower.getStandPowerOptional(entity).ifPresent((stand)->{
            StandType<?>  hm = InitStands.STAND_WHITE_ALBUM.getStandType();
            if(stand.getType() == hm && stand.getStandManifestation()instanceof StandEntity){
                M playerModel = getParentModel();
                glovesModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
                playerModel.copyPropertiesTo(glovesModel);
                glovesModel.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);

                glovesModel.leftArm.visible = playerModel.leftArm.visible;
                glovesModel.leftSleeve.visible = playerModel.leftArm.visible;
                glovesModel.rightArm.visible = playerModel.rightArm.visible;
                glovesModel.rightSleeve.visible = playerModel.rightArm.visible;
                ResourceLocation texture = new  ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"/textures/entity/stand/white_album"+(slim ? "_slim" : "") + ".png");
                IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), false, false);
                glovesModel.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }

        });
    }


}
