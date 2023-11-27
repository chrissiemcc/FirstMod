package net.apixelmelon.firstmod.util;

import net.apixelmelon.firstmod.FirstMod;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType PINE = WoodType.register(new WoodType(FirstMod.MOD_ID + ":pine", BlockSetType.OAK));
}
