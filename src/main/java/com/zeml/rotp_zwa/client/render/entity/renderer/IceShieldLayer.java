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

public class IceShieldLayer<T extends LivingEntity, M extends BipedModel<T>> extends LayerRenderer<T, M> implements IFirstPersonHandLayer {
    private static final ResourceLocation ICE_LAYER = new ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/entity/stand/ice_shield.png");
    private static final ResourceLocation ICE_LAYER_SLIM = new  ResourceLocation(RotpWhiteAlbumAddon.MOD_ID,"textures/entity/stand/ice_shield_slim.png");
    private final M thornsModel;
    private final boolean slim;

    public IceShieldLayer(IEntityRenderer<T, M> renderer, M hermitoModel, boolean slim) {
        super(renderer);
        this.thornsModel = hermitoModel;
        this.slim = slim;
        PlayerAnimationHandler.getPlayerAnimator().onArmorLayerInit(this);
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ticks, float yRot, float xRot){
        if(!(entity.hasEffect(Effects.INVISIBILITY ) || entity.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()))){
            IStandPower.getStandPowerOptional(entity).ifPresent((stand)->{
                StandType<?> hm = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(stand.getType() == hm && stand.getStandManifestation()instanceof StandEntity && stand.getHeldAction()==InitStands.WA_BLOCK.get()){
                    M playerModel = getParentModel();
                    thornsModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
                    playerModel.copyPropertiesTo(thornsModel);
                    thornsModel.setupAnim(entity, limbSwing, limbSwingAmount, ticks, yRot, xRot);

                    thornsModel.leftArm.visible = playerModel.leftArm.visible;
                    thornsModel.rightArm.visible = playerModel.rightArm.visible;
                    ResourceLocation texture = getTexture();
                    texture = StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                            .getStandSkin(stand.getStandInstance().get()), texture);
                    IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), false, false);
                    thornsModel.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                }
            });
        }
    }

    private ResourceLocation getTexture() {
        return slim ? ICE_LAYER_SLIM : ICE_LAYER;
    }

    @Override
    public void renderHandFirstPerson(HandSide side, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
            AbstractClientPlayerEntity player, PlayerRenderer playerRenderer) {
        if(!(player.hasEffect(Effects.INVISIBILITY ) || player.hasEffect(ModStatusEffects.FULL_INVISIBILITY.get()))){
            IStandPower.getStandPowerOptional(player).ifPresent((stand)->{
                StandType<?> hm = InitStands.STAND_WHITE_ALBUM.getStandType();
                if(stand.getType() == hm && stand.getStandManifestation()instanceof StandEntity && stand.getHeldAction()==InitStands.WA_BLOCK.get()){
                    PlayerModel<AbstractClientPlayerEntity> model = (PlayerModel<AbstractClientPlayerEntity>) thornsModel;
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
