package net.apixelmelon.firstmod.block.entity;

import net.apixelmelon.firstmod.item.ModItems;
import net.apixelmelon.firstmod.recipe.APixelMelonFurnaceRecipe;
import net.apixelmelon.firstmod.screen.APixelMelonFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class APixelMelonFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private Map<Item, Integer> BURN_DURATION_MAP =
            Map.of(ModItems.PINE_CONE.get(), 100,
                    ModItems.CORN.get(), 200,
                    Items.BLAZE_POWDER, 800);

    public APixelMelonFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.APIXELMELON_FURNACE_BLOCK_ENTITY.get(), pPos, pBlockState, APixelMelonFurnaceRecipe.Type.INSTANCE);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.firstmod.apixelmelon_furnace");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new APixelMelonFurnaceMenu(pContainerId, pInventory, this, dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack pFuel) {
        return BURN_DURATION_MAP.getOrDefault(pFuel.getItem(), 0);
    }
}
