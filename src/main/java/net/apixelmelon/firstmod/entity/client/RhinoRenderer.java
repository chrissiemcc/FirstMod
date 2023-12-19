package net.apixelmelon.firstmod.entity.client;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.apixelmelon.firstmod.FirstMod;
import net.apixelmelon.firstmod.entity.custom.RhinoEntity;
import net.apixelmelon.firstmod.entity.layers.ModModelLayers;
import net.apixelmelon.firstmod.entity.variant.RhinoVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class RhinoRenderer extends MobRenderer<RhinoEntity, RhinoModel<RhinoEntity>> {
    private static final Map<RhinoVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(RhinoVariant.class), map -> {
                map.put(RhinoVariant.DEFAULT,
                        new ResourceLocation(FirstMod.MOD_ID, "textures/entity/rhino.png"));
                map.put(RhinoVariant.WHITE,
                        new ResourceLocation(FirstMod.MOD_ID, "textures/entity/white_rhino.png"));
            }); // Maps each variant to the correct texture

    public RhinoRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new RhinoModel<>(pContext.bakeLayer(ModModelLayers.RHINO_LAYER)), 2f);
    }

    @Override
    public ResourceLocation getTextureLocation(RhinoEntity pEntity) {
        return LOCATION_BY_VARIANT.get(pEntity.getVariant());
    }

    @Override
    public void render(RhinoEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
