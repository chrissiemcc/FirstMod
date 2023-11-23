package net.apixelmelon.firstmod.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.apixelmelon.firstmod.block.entity.GemPolishingStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class GemPolishingBlockEntityRenderer implements BlockEntityRenderer<GemPolishingStationBlockEntity> {
    public GemPolishingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(GemPolishingStationBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderStack();

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.75, 0.5f); //sets the position of the item being rendered
        pPoseStack.scale(0.35f, 0.35f, 0.35f); //sets the scale of the item being rendered
        pPoseStack.mulPose(Axis.XP.rotationDegrees(270)); //rotates the item

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 1);
        pPoseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
