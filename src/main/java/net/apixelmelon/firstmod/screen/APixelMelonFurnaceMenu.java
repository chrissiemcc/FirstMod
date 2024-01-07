package net.apixelmelon.firstmod.screen;

import net.apixelmelon.firstmod.recipe.APixelMelonFurnaceRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;

public class APixelMelonFurnaceMenu extends AbstractFurnaceMenu {
    protected APixelMelonFurnaceMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, pPlayerInventory);
    }

    public APixelMelonFurnaceMenu(int pContainerId, Inventory pPlayerInventory, Container container, ContainerData data) {
        super(ModMenuTypes.APIXELMELON_FURNACE_MENU.get(), APixelMelonFurnaceRecipe.Type.INSTANCE, RecipeBookType.FURNACE, pContainerId, pPlayerInventory, container, data);
    }

    public APixelMelonFurnaceMenu(int pContainerId, Inventory pPlayerInventory) {
        super(ModMenuTypes.APIXELMELON_FURNACE_MENU.get(), APixelMelonFurnaceRecipe.Type.INSTANCE, RecipeBookType.FURNACE, pContainerId, pPlayerInventory);
    }

    @Override
    protected boolean isFuel(ItemStack pStack) {
        return true;
    }
}
