package net.apixelmelon.firstmod.screen;

import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.recipe.APixelMelonFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class APixelMelonFurnaceScreen extends AbstractFurnaceScreen<APixelMelonFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(FirstMod.MOD_ID, "textures/gui/apixelmelon_furnace.png");

    public APixelMelonFurnaceScreen(APixelMelonFurnaceMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, new APixelMelonFurnaceRecipeBookComponent(), pPlayerInventory, pTitle, TEXTURE);
    }
}
