package com.zeml.rotp_zwa.client;

import com.github.standobyte.jojo.client.render.entity.layerrenderer.HamonBurnLayer;
import com.zeml.rotp_zwa.RotpWhiteAlbumAddon;
import com.zeml.rotp_zwa.client.render.entity.model.stand.WhiteAlbumUserModel;
import com.zeml.rotp_zwa.client.render.entity.renderer.IceShieldLayer;
import com.zeml.rotp_zwa.client.render.entity.renderer.stand.WhiteAlbumRenderer;
import com.zeml.rotp_zwa.client.render.entity.renderer.WhiteAlbumLayer;
import com.zeml.rotp_zwa.init.AddonStands;

import com.zeml.rotp_zwa.init.InitEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@EventBusSubscriber(modid = RotpWhiteAlbumAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();;

        RenderingRegistry.registerEntityRenderingHandler(AddonStands.WHITE_ALBUM.getEntityType(), WhiteAlbumRenderer::new);


        event.enqueueWork(() -> {
            Map<String, PlayerRenderer> skinMap = mc.getEntityRenderDispatcher().getSkinMap();
            addLayers(skinMap.get("default"), false);
            addLayers(skinMap.get("slim"), true);
            mc.getEntityRenderDispatcher().renderers.values().forEach(ClientInit::addLayersToEntities);
        });


    }

    private static void addLayers(PlayerRenderer renderer, boolean slim){
        renderer.addLayer(new WhiteAlbumLayer<>(renderer,new WhiteAlbumUserModel<>(0.2F,slim),slim));
        renderer.addLayer(new IceShieldLayer<>(renderer,new WhiteAlbumUserModel<>(0.6F,slim),slim));
        addLivingLayers(renderer);
        addBipedLayers(renderer);
    }

    private static <T extends LivingEntity, M extends BipedModel<T>> void addLayersToEntities(EntityRenderer<?> renderer) {
        if (renderer instanceof LivingRenderer<?, ?>) {
            addLivingLayers((LivingRenderer<T, ?>) renderer);
            if (((LivingRenderer<?, ?>) renderer).getModel() instanceof BipedModel<?>) {
                addBipedLayers((LivingRenderer<T, M>) renderer);
            }
        }
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void addLivingLayers(@NotNull LivingRenderer<T, M> renderer) {

        renderer.addLayer(new HamonBurnLayer<>(renderer));
    }

    private static <T extends LivingEntity, M extends BipedModel<T>> void addBipedLayers(LivingRenderer<T, M> renderer) {


    }

}
