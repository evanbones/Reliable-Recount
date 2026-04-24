package com.evandev.reliable_recount.mixin;

import com.evandev.reliable_recount.CommonClass;
import com.evandev.reliable_recount.config.ModConfig;
import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.runtime.EmiDrawContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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

        if (style == ModConfig.FontStyle.CREATE) {
            CommonClass.renderSizeLabel(context.raw(), Minecraft.getInstance().font, x, y, amount);
        } else {
            Component styled = CommonClass.getStyledAmount(amount);
            CommonClass.drawStringWrapper(context.raw(), Minecraft.getInstance().font, styled, x + 17 - Minecraft.getInstance().font.width(styled), y + 9, 0xFFFFFF);
        }

        ci.cancel();
    }
}