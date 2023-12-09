package net.apixelmelon.firstmod.item;

import net.apixelmelon.firstmod.FirstMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.DATA_TABLET.get(), new ResourceLocation(FirstMod.MOD_ID, "on"),
                (pStack, pLevel, pEntity, pSeed) -> pStack.hasTag() ? 1f : 0f);
    }//return 1 if the itemstack has a tag and 0 if it does not have a tag
}
