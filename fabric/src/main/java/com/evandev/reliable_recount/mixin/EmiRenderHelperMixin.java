package com.evandev.reliable_recount.mixin;

import com.evandev.reliable_recount.CommonClass;
import com.evandev.reliable_recount.config.ModConfig;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiRenderHelper.class)
public class EmiRenderHelperMixin {
    @Inject(method = "renderAmount", at = @At("HEAD"), require = 0, cancellable = true, remap = false)
    private static void changeFont(EmiDrawContext context, int x, int y, Component amount, CallbackInfo ci) {
        ModConfig.FontStyle currentStyle = ModConfig.get().fontStyle;

        if (currentStyle == ModConfig.FontStyle.VANILLA) {
            return;
        }

        ResourceLocation fontLocation = CommonClass.getActiveFont();
        Component styledAmount = amount.copy().withStyle(Style.EMPTY.withFont(fontLocation));

        CommonClass.renderSizeLabel(
                context.raw(),
                Minecraft.getInstance().font,
                x,
                y,
                styledAmount
        );

        ci.cancel();
    }
}