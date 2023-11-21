package net.apixelmelon.firstmod.event;

import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.entity.ModEntities;
import net.apixelmelon.firstmod.entity.client.ModModelLayers;
import net.apixelmelon.firstmod.entity.client.RhinoModel;
import net.apixelmelon.firstmod.entity.custom.RhinoEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayer(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
    }
}
