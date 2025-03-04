package meow.binary.o123456789.neoforge.mixin;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.core.AEConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MEStorageScreen.class)
public class MEStorageScreenMixin {
    @Unique
    private static void o123456789$renderSizeLabel(Matrix4f matrix, Font fontRenderer, float xPos, float yPos, Component text, float scaleFactor) {
        RenderSystem.disableBlend();
        float x = (xPos + 16.0F) / scaleFactor - fontRenderer.width(text);
        float y = (yPos + 16.0F) / scaleFactor - 7;
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        fontRenderer.drawInBatch(text, x, y, 16777215, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
        RenderSystem.enableBlend();
    }

    @Unique
    private static void o123456789$renderSizeLabel(GuiGraphics guiGraphics, Font fontRenderer, float xPos, float yPos, Component text) {
        float scaleFactor = Math.min(1.0F, 15F / (float)fontRenderer.width(text));
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(0.0F, 0.0F, 200.0F);
        stack.scale(scaleFactor, scaleFactor, 1f);
        o123456789$renderSizeLabel(stack.last().pose(), fontRenderer, xPos, yPos, text, scaleFactor);
        stack.popPose();
    }

    @Redirect(method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/me/common/StackSizeRenderer;renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;Z)V",
                    ordinal = 0
            ),
            require = 0
    )
    private void redirected(GuiGraphics guiGraphics, Font fontRenderer, float xPos, float yPos, String text, boolean largeFonts) {
        o123456789$renderSizeLabel(guiGraphics, fontRenderer, xPos, yPos, Component.literal(text).withStyle(Style.EMPTY.withFont(O123456789.FONT)));
    }
}
