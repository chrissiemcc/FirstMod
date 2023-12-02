package net.apixelmelon.firstmod.worldgen.biome;

import net.apixelmelon.firstmod.FirstMod;
import net.minecraft.resources.ResourceLocation;
import terrablender.api.Regions;

public class ModTerrablender {
    public static void registerBiomes() {
        Regions.register(new ModOverworldRegion(new ResourceLocation(FirstMod.MOD_ID, "overworld"), 5));
    }
}
