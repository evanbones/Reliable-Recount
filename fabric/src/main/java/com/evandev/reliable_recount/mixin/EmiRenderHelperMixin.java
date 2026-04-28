package com.evandev.reliable_recount.mixin;

import com.evandev.reliable_recount.CommonClass;
import com.evandev.reliable_recount.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiRenderHelper.class)
public class EmiRenderHelperMixin {
    @Inject(method = "renderAmount", at = @At("HEAD"), require = 0, cancellable = true, remap = false)
    private static void changeFont(EmiDrawContext context, int x, int y, Component amount, CallbackInfo ci) {
        ModConfig.FontStyle style = ModConfig.get().fontStyle;

        if (style == ModConfig.FontStyle.VANILLA) {
            return;
        }

        Component styledAmount = CommonClass.getStyledAmount(amount);
        Font font = Minecraft.getInstance().font;
        int logicalWidth = font.width(styledAmount);

        float scaleFactor = Math.min(1.0F, 15F / (float) logicalWidth);
        float xOffset = (style == ModConfig.FontStyle.CREATE) ? 16.0F : 17.0F;

        PoseStack stack = context.raw().pose();
        stack.pushPose();
        stack.translate(x, y, 200.0F);
        stack.scale(scaleFactor, scaleFactor, 1.0F);

        float drawX = (xOffset / scaleFactor) - logicalWidth;
        float drawY = 9.0F / scaleFactor;

        RenderSystem.disableBlend();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        font.drawInBatch(styledAmount, drawX, drawY, 0xFFFFFF, true, stack.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
        RenderSystem.enableBlend();

        stack.popPose();
        ci.cancel();
    }
}