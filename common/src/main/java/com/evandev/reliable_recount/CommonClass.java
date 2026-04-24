package com.evandev.reliable_recount;

import com.evandev.reliable_recount.config.ModConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class CommonClass {
    public static final ResourceLocation FONT_CREATE = ResourceLocation.tryBuild(Constants.MOD_ID, "3x5");
    public static final ResourceLocation FONT_NOELLE = ResourceLocation.tryBuild(Constants.MOD_ID, "noelle");
    public static final ResourceLocation FONT_NOELLE_TINY = ResourceLocation.tryBuild(Constants.MOD_ID, "noelle_tiny");

    public static ResourceLocation getActiveFont() {
        return switch (ModConfig.get().fontStyle) {
            case CREATE -> FONT_CREATE;
            case NOELLE -> FONT_NOELLE;
            case NOELLE_TINY -> FONT_NOELLE_TINY;
            case VANILLA -> Style.DEFAULT_FONT;
        };
    }

    public static Component getStyledAmount(String text) {
        return Component.literal(text).withStyle(Style.EMPTY.withFont(getActiveFont()));
    }

    public static Component getStyledAmount(Component component) {
        return component.copy().withStyle(Style.EMPTY.withFont(getActiveFont()));
    }

    public static void renderSizeLabel(Matrix4f matrix, Font fontRenderer, float xPos, float yPos, Component text, float scaleFactor) {
        RenderSystem.disableBlend();
        float x = (xPos + 16.0F) / scaleFactor - fontRenderer.width(text);
        float y = (yPos + 16.0F) / scaleFactor - 7;
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        fontRenderer.drawInBatch(text, x, y, 16777215, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
        RenderSystem.enableBlend();
    }

    public static void renderSizeLabel(GuiGraphics guiGraphics, Font fontRenderer, float xPos, float yPos, Component text) {
        float scaleFactor = Math.min(1.0F, 15F / (float) fontRenderer.width(text));
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(0.0F, 0.0F, 200.0F);
        stack.scale(scaleFactor, scaleFactor, 1f);
        renderSizeLabel(stack.last().pose(), fontRenderer, xPos, yPos, text, scaleFactor);
        stack.popPose();
    }

    public static int drawStringWrapper(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color) {
        return guiGraphics.drawString(font, text, x, y, color);
    }

    public static void drawOutline(Font font, FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, Font.DisplayMode displayMode, int backgroundColor, int packedLightCoords, CallbackInfoReturnable<Integer> cir) {
        ResourceLocation[] rl = new ResourceLocation[1];
        text.accept(((i, style, j) -> {
            rl[0] = style.getFont();
            return false;
        }));

        if (rl[0] == null || !rl[0].equals(FONT_CREATE)) {
            return;
        }

        if (dropShadow) {
            int alpha = (color >> 24) & 0xFF;
            int red = (color >> 16) & 0xFF;
            int green = (color >> 8) & 0xFF;
            int blue = color & 0xFF;

            float darkenFactor = 0.22f;

            red = (int) (red * darkenFactor) & 0xFF;
            green = (int) (green * darkenFactor) & 0xFF;
            blue = (int) (blue * darkenFactor) & 0xFF;

            int shadowColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

            Matrix4f matrix4f = new Matrix4f(matrix);
            matrix4f.translate(0, 0, 0.1f);

            font.drawInBatch8xOutline(text, x, y, color, shadowColor, matrix4f, buffer, packedLightCoords);
            cir.setReturnValue(font.width(text) + 1);
        }
    }

    public static void init() {
        ModConfig.load();
    }
}