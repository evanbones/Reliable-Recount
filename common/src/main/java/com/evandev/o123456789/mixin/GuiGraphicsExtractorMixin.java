package com.evandev.o123456789.mixin;

import com.evandev.o123456789.client.SmallFontRenderer;
import com.evandev.o123456789.config.ModConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

    @WrapOperation(
            method = "itemCount",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V")
    )
    private void redirected(GuiGraphicsExtractor instance, Font font, String str, int x, int y, int color, boolean dropShadow, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) int itemX) {
        if (!ModConfig.get().enabled) {
            original.call(instance, font, str, x, y, color, dropShadow);
            return;
        }

        MutableComponent component = Component.literal(str).withStyle(Style.EMPTY.withFont(SmallFontRenderer.FONT));
        MutableComponent boldComponent = Component.literal(str).setStyle(Style.EMPTY.withFont(SmallFontRenderer.FONT));

        int left = itemX + 15 - font.width(component);
        int center = itemX + 16 - font.width(component);
        int right = itemX + 17 - font.width(component);
        int shadow = ARGB.opaque(0x3a3a3a);

        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, left, y, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, right, y, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, left, y + 1, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, center, y + 1, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, right, y + 1, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, left, y - 1, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, center, y - 1, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, boldComponent, right, y - 1, shadow);
        SmallFontRenderer.drawStringWrapper(instance, font, component, center, y, color);
    }
}