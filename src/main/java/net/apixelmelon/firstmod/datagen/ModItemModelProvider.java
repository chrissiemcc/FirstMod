package net.apixelmelon.firstmod.datagen;

import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.block.ModBlocks;
import net.apixelmelon.firstmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FirstMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SAPPHIRE);
        simpleItem(ModItems.RAW_SAPPHIRE);

        simpleItem(ModItems.METAL_DETECTOR);
        simpleItem(ModItems.PINE_CONE);
        simpleItem(ModItems.STRAWBERRY);

        simpleBlockItem(ModBlocks.SAPPHIRE_DOOR);
        simpleBlockItem(ModBlocks.SAPPHIRE_STAIRS);
        simpleBlockItem(ModBlocks.SAPPHIRE_SLAB);
        simpleBlockItem(ModBlocks.SAPPHIRE_PRESSURE_PLATE);
        simpleBlockItem(ModBlocks.SAPPHIRE_FENCE_GATE);

        fenceItem(ModBlocks.SAPPHIRE_FENCE, ModBlocks.SAPPHIRE_BLOCK);
        buttonItem(ModBlocks.SAPPHIRE_BUTTON, ModBlocks.SAPPHIRE_BLOCK);
        wallItem(ModBlocks.SAPPHIRE_WALL, ModBlocks.SAPPHIRE_BLOCK);

        trapdoorItem(ModBlocks.SAPPHIRE_TRAPDOOR);

        handheldItem(ModItems.SAPPHIRE_SWORD);
        handheldItem(ModItems.SAPPHIRE_PICKAXE);
        handheldItem(ModItems.SAPPHIRE_AXE);
        handheldItem(ModItems.SAPPHIRE_SHOVEL);
        handheldItem(ModItems.SAPPHIRE_HOE);
    }

    private void simpleItem(RegistryObject<Item> item) {
        this.withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(FirstMod.MOD_ID, "item/" + item.getId().getPath()));
    }//Generates item model

    private void handheldItem(RegistryObject<Item> item) {
        this.withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(FirstMod.MOD_ID, "item/" + item.getId().getPath()));
    }

    private void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", new ResourceLocation(FirstMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(FirstMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", new ResourceLocation(FirstMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(FirstMod.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    private void simpleBlockItem(RegistryObject<Block> block) {
        try {
            this.withExistingParent(block.getId().getPath(),
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation(FirstMod.MOD_ID, "item/" + block.getId().getPath()));
        }
        catch(IllegalArgumentException e) {
            this.withExistingParent(FirstMod.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                    modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
        }//For stairs, slab, pressure plate and fence gate that require a different .json file
    }
}
