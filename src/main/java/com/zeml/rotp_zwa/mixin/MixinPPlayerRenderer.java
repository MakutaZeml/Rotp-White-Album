package com.zeml.rotp_zwa.mixin;

import com.zeml.rotp_zwa.client.render.entity.renderer.IceShieldLayer;
import com.zeml.rotp_zwa.client.render.entity.renderer.WhiteAlbumLayer;
import com.zeml.rotp_zwa.epicfight.IceShieldLayerEpicFight;
import com.zeml.rotp_zwa.epicfight.WhiteAlbumLayerEpicFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.client.renderer.patched.entity.PPlayerRenderer;

@Mixin(value = PPlayerRenderer.class)
public class MixinPPlayerRenderer {


    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        PPlayerRenderer renderer = (PPlayerRenderer)((Object) this);
        renderer.addPatchedLayer(WhiteAlbumLayer.class, new WhiteAlbumLayerEpicFight<>());
        renderer.addPatchedLayer(IceShieldLayer.class, new IceShieldLayerEpicFight<>());
    }
}
