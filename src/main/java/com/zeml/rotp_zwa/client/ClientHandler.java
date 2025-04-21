package com.zeml.rotp_zwa.client;

import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.init.InitStands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderArmEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RotpWhiteAlbumAddon.MOD_ID,value = Dist.CLIENT)
public class ClientHandler {



    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if(!ClientUtil.canSeeStands()) return;
        PlayerEntity player = event.getPlayer();
        AbstractClientPlayerEntity abstractClientPlayer = (AbstractClientPlayerEntity) player;
        PlayerModel<AbstractClientPlayerEntity> model = event.getRenderer().getModel();
        IStandPower.getStandPowerOptional(player).ifPresent(power -> {
            StandType<?> WA = InitStands.STAND_WHITE_ALBUM.getStandType();
            if (power.getType() == WA) {
                if (power.isActive()) {
                    model.hat.visible = false;
                    model.jacket.visible = false;
                    model.leftPants.visible = false;
                    model.rightPants.visible = false;
                    model.rightSleeve.visible = false;
                    model.leftSleeve.visible = false;
                }
            } else {
                model.hat.visible = abstractClientPlayer.isModelPartShown(PlayerModelPart.HAT);
                model.jacket.visible =  abstractClientPlayer.isModelPartShown(PlayerModelPart.JACKET);
                model.leftPants.visible = abstractClientPlayer.isModelPartShown(PlayerModelPart.LEFT_PANTS_LEG);
                model.rightPants.visible =  abstractClientPlayer.isModelPartShown(PlayerModelPart.RIGHT_PANTS_LEG);
                model.rightSleeve.visible =  abstractClientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
                model.leftSleeve.visible =  abstractClientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
            }
        });
    }



}
