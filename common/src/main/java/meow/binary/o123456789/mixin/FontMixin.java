package meow.binary.o123456789.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Font.class)
public abstract class FontMixin {
    @Shadow
    public abstract void drawInBatch8xOutline(FormattedCharSequence text, float x, float y, int color, int backgroundColor, Matrix4f matrix, MultiBufferSource bufferSource, int packedLightCoords);


    @Shadow public abstract int width(FormattedCharSequence text);

    @Inject(method = "drawInternal(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;" +
            "Lnet/minecraft/client/gui/Font$DisplayMode;II)I",
            at = @At("HEAD"),
            cancellable = true)
    private void injected(FormattedCharSequence text, float x, float y, int color, boolean dropShadow, Matrix4f matrix, MultiBufferSource buffer, Font.DisplayMode displayMode, int backgroundColor, int packedLightCoords, CallbackInfoReturnable<Integer> cir) {
        ResourceLocation[] rl = new ResourceLocation[1];
        text.accept(((i, style, j) -> {
            rl[0] = style.getFont();
            return false;
        }));
        if (rl[0] == null || !(rl[0].equals(O123456789.FONT) || rl[0].equals(O123456789.FONT_TINY))) {
            return;
        }

        if (dropShadow) {
            int alpha = (color >> 24) & 0xFF;
            int red   = (color >> 16) & 0xFF;
            int green = (color >> 8)  & 0xFF;
            int blue  = color         & 0xFF;

            float darkenFactor = 0.22f;

            red   = (int) (red   * darkenFactor) & 0xFF;
            green = (int) (green * darkenFactor) & 0xFF;
            blue  = (int) (blue  * darkenFactor) & 0xFF;

            int shadowColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

            Matrix4f matrix4f = new Matrix4f(matrix);
            matrix4f.translate(0,0,0.1f);
            drawInBatch8xOutline(text, x, y, color, shadowColor, matrix4f, buffer, packedLightCoords);
            cir.setReturnValue(width(text)+1);
        }
    }
}
