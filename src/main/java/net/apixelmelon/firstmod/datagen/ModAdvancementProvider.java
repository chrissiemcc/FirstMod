package net.apixelmelon.firstmod.datagen;

import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement rootAdvancement = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.SAPPHIRE.get()),
                        Component.literal("First Mod"), Component.literal("The Power lies in the Sapphire!"),
                        new ResourceLocation(FirstMod.MOD_ID, "textures/block/sapphire_ore.png"), FrameType.TASK,
                        true, true, false))
                .addCriterion("has_sapphire", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SAPPHIRE.get()))
                .save(saver, new ResourceLocation(FirstMod.MOD_ID, "firstmod"), existingFileHelper);


        Advancement metalDetector = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ModItems.METAL_DETECTOR.get()),
                        Component.literal("Metal Detector"), Component.literal("Batteries not included! (Actually doesn't need batteries)"),
                        null, FrameType.TASK,
                        true, true, false))
                .parent(rootAdvancement)
                .addCriterion("has_metal_detector", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.METAL_DETECTOR.get()))
                .save(saver, new ResourceLocation(FirstMod.MOD_ID, "metal_detector"), existingFileHelper);
    }
}
