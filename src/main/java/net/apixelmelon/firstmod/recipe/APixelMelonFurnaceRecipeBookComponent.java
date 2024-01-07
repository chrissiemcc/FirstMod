package net.apixelmelon.firstmod.recipe;

import net.apixelmelon.firstmod.item.ModItems;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Set;

public class APixelMelonFurnaceRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
    @Override
    protected Set<Item> getFuelItems() {
        return Set.of(ModItems.PINE_CONE.get(), ModItems.CORN.get(), Items.BLAZE_POWDER);
    }
}
