package net.apixelmelon.firstmod.entity.client;

import net.apixelmelon.firstmod.FirstMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(FirstMod.MOD_ID, "rhino_layer"), "main");

    public static final ModelLayerLocation PINE_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(FirstMod.MOD_ID, "boat/pine"), "main");
    public static final ModelLayerLocation PINE_CHEST_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(FirstMod.MOD_ID, "chest_boat/pine"), "main");
}
