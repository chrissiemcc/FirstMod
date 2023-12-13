package net.apixelmelon.firstmod;

import com.mojang.logging.LogUtils;
import net.apixelmelon.firstmod.block.ModBlocks;
import net.apixelmelon.firstmod.block.entity.ModBlockEntities;
import net.apixelmelon.firstmod.effect.ModEffects;
import net.apixelmelon.firstmod.enchantment.ModEnchantments;
import net.apixelmelon.firstmod.entity.ModEntities;
import net.apixelmelon.firstmod.entity.client.ModBoatRenderer;
import net.apixelmelon.firstmod.entity.client.RhinoRenderer;
import net.apixelmelon.firstmod.item.ModCreativeModeTabs;
import net.apixelmelon.firstmod.item.ModItemProperties;
import net.apixelmelon.firstmod.item.ModItems;
import net.apixelmelon.firstmod.loot.ModLootModifiers;
import net.apixelmelon.firstmod.painting.ModPaintings;
import net.apixelmelon.firstmod.potion.BetterBrewingRecipe;
import net.apixelmelon.firstmod.potion.ModPotions;
import net.apixelmelon.firstmod.recipe.ModRecipes;
import net.apixelmelon.firstmod.screen.GemPolishingStationScreen;
import net.apixelmelon.firstmod.screen.ModMenuTypes;
import net.apixelmelon.firstmod.sound.ModSounds;
import net.apixelmelon.firstmod.util.ModWoodTypes;
import net.apixelmelon.firstmod.villager.ModVillagers;
import net.apixelmelon.firstmod.worldgen.biome.ModTerrablender;
import net.apixelmelon.firstmod.worldgen.biome.surface.ModSurfaceRules;
import net.apixelmelon.firstmod.worldgen.tree.ModFoliagePlacers;
import net.apixelmelon.firstmod.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FirstMod.MOD_ID)
public class FirstMod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "firstmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public FirstMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModTrunkPlacerTypes.register(modEventBus);
        ModFoliagePlacers.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModPaintings.register(modEventBus);
        ModEffects.register(modEventBus);
        ModPotions.register(modEventBus);

        ModTerrablender.registerBiomes();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.CATMINT.getId(), ModBlocks.POTTED_CATMINT);

            ComposterBlock.COMPOSTABLES.put(ModItems.STRAWBERRY.get(), 0.35f); //35% chance a strawberry adds to the composter pile
            ComposterBlock.COMPOSTABLES.put(ModItems.STRAWBERRY_SEEDS.get(), 0.20f); //20% chance strawberry seeds adds to the composter pile

            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());

            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION.get()));
            //adds the potion recipe to the registry
        });
    }

    // Add the items to the ingredients tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.SAPPHIRE);
            event.accept(ModItems.RAW_SAPPHIRE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            Sheets.addWoodType(ModWoodTypes.PINE);

            EntityRenderers.register(ModEntities.RHINO.get(), RhinoRenderer::new);
            EntityRenderers.register(ModEntities.MOD_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false));
            EntityRenderers.register(ModEntities.MOD_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true));

            EntityRenderers.register(ModEntities.DICE_PROJECTILE.get(), ThrownItemRenderer::new);

            MenuScreens.register(ModMenuTypes.GEM_POLISHING_MENU.get(), GemPolishingStationScreen::new);

            event.enqueueWork(() -> {
                ModItemProperties.addCustomItemProperties();
            });
        }
    }
}
