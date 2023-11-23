package net.apixelmelon.firstmod.event;

import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.block.entity.ModBlockEntities;
import net.apixelmelon.firstmod.block.entity.renderer.GemPolishingBlockEntityRenderer;
import net.apixelmelon.firstmod.entity.client.ModModelLayers;
import net.apixelmelon.firstmod.entity.client.RhinoModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RHINO_LAYER, RhinoModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GEM_POLISHING_BE.get(), GemPolishingBlockEntityRenderer::new);
    }
}
