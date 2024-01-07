package net.apixelmelon.firstmod.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.block.ModBlocks;
import net.apixelmelon.firstmod.recipe.APixelMelonFurnaceRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class APixelMelonFurnaceRecipeCategory implements IRecipeCategory<APixelMelonFurnaceRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FirstMod.MOD_ID, "apixelmelon_furnace");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FirstMod.MOD_ID,
            "textures/gui/apixelmelon_furnace.png");

    public static final RecipeType<APixelMelonFurnaceRecipe> APIXELMELON_FURNACE_TYPE =
            new RecipeType<>(UID, APixelMelonFurnaceRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public APixelMelonFurnaceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.APIXELMELON_FURNACE_BLOCK.get()));
    }

    @Override
    public RecipeType<APixelMelonFurnaceRecipe> getRecipeType() {
        return APIXELMELON_FURNACE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.firstmod.apixelmelon_furnace");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, APixelMelonFurnaceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 17).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getResultItem(null));
    }
}
