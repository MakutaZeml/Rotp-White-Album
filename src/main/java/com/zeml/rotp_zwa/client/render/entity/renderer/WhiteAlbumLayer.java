package com.zeml.rotp_zwa.client.render.entity.renderer;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.playeranim.PlayerAnimationHandler;
import com.github.standobyte.jojo.client.render.entity.layerrenderer.IFirstPersonHandLayer;
import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.init.ModStatusEffects;
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
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class WhiteAlbumLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> implements IFirstPersonHandLayer {
    private static final ResourceLocation WHITE_LAYER = new  ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/entity/stand/white_album.png");
    private static final ResourceLocation WHITE_LAYER_SLIM = new  ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/entity/stand/white_album_slim.png");
    private final M glovesModel;
    private final boolean slim;

    public WhiteAlbumLayer(IEntityRenderer<T, M> renderer, M hermitoModel, boolean slim) {
        super(renderer);
        this.glovesModel = hermitoModel;
        this.slim = slim;
        PlayerAnimationHandler.getPlayerAnimator().onArmorLayerInit(this);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot){
        if (!ClientUtil.canSeeStands()) {
            return;
        }

        if(!(entity.hasEffect(Effects.INVISIBILITY ) || entity.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()))){
            IStandPower.getStandPowerOptional(entity).ifPresent((stand)->{
                StandType<?>  hm = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(stand.getType() == hm && stand.getStandManifestation()instanceof StandEntity && stand.getHeldAction() != InitStands.WA_BLOCK.get()){
                    M playerModel = getParentModel();
                    glovesModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
                    playerModel.copyPropertiesTo(glovesModel);
                    glovesModel.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);

                    glovesModel.leftArm.visible = playerModel.leftArm.visible;
                    glovesModel.rightArm.visible = playerModel.rightArm.visible;
                    ResourceLocation texture = getTexture();
                    texture = StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                            .getStandSkin(stand.getStandInstance().get()), texture);
                    IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer,
                            RenderType.armorCutoutNoCull(texture),
                            false, false);
                    glovesModel.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                }
            });
        }
    }

    private ResourceLocation getTexture() {
        return slim ? WHITE_LAYER_SLIM : WHITE_LAYER;
    }

    @Override
    public void renderHandFirstPerson(HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
            AbstractClientPlayerEntity player, PlayerRenderer playerRenderer) {
        if (!ClientUtil.canSeeStands()) {
            return;
        }

        if(!(player.hasEffect(Effects.INVISIBILITY ) || player.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()))){
            IStandPower.getStandPowerOptional(player).ifPresent((stand)->{
                StandType<?>  hm = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(stand.getType() == hm && stand.getStandManifestation()instanceof StandEntity && stand.getHeldAction() != InitStands.WA_BLOCK.get()){
                    PlayerModel<AbstractClientPlayerEntity> model = (PlayerModel<AbstractClientPlayerEntity>) glovesModel;
                    ResourceLocation texture = getTexture();
                    texture = StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                            .getStandSkin(stand.getStandInstance().get()), texture);
                    ClientUtil.setupForFirstPersonRender(model, player);
                    IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), false, false);
                    ModelRenderer glove = ClientUtil.getArm(model, side);
                    ModelRenderer gloveOuter = ClientUtil.getArmOuter(model, side);
                    glove.xRot = 0.0F;
                    glove.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
                    gloveOuter.xRot = 0.0F;
                    gloveOuter.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY);
                }
            });
        }
    }

}
