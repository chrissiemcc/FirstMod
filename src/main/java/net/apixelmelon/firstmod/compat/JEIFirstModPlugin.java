package net.apixelmelon.firstmod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.recipe.APixelMelonFurnaceRecipe;
import net.apixelmelon.firstmod.recipe.GemPolishingRecipe;
import net.apixelmelon.firstmod.screen.APixelMelonFurnaceScreen;
import net.apixelmelon.firstmod.screen.GemPolishingStationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIFirstModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FirstMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GemPolishingCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new APixelMelonFurnaceRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<GemPolishingRecipe> polishingRecipes = recipeManager.getAllRecipesFor(GemPolishingRecipe.Type.INSTANCE);
        registration.addRecipes(GemPolishingCategory.GEM_POLISHING_TYPE, polishingRecipes);

        List<APixelMelonFurnaceRecipe> apixelmelonFurnaceRecipes = recipeManager.getAllRecipesFor(APixelMelonFurnaceRecipe.Type.INSTANCE);
        registration.addRecipes(APixelMelonFurnaceRecipeCategory.APIXELMELON_FURNACE_TYPE, apixelmelonFurnaceRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GemPolishingStationScreen.class, 60, 30, 20, 30,
                GemPolishingCategory.GEM_POLISHING_TYPE);

        registration.addRecipeClickArea(APixelMelonFurnaceScreen.class, 60, 30, 20, 30,
                APixelMelonFurnaceRecipeCategory.APIXELMELON_FURNACE_TYPE);
    }
}
