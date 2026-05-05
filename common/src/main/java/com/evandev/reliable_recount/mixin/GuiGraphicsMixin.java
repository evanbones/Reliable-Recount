package com.evandev.reliable_recount.mixin;

import com.evandev.reliable_recount.CommonClass;
import com.evandev.reliable_recount.config.ModConfig;
import com.evandev.reliable_recount.platform.Services;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @WrapOperation(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I")
    )
    private int wrapDraw(GuiGraphics instance, Font font, String text, int strX, int strY, int color, boolean dropShadow, Operation<Integer> original, @Local(ordinal = 0, argsOnly = true) int itemX) {
        ModConfig.FontStyle style = ModConfig.get().fontStyle;
        if (style == ModConfig.FontStyle.VANILLA) {
            return original.call(instance, font, text, strX, strY, color, dropShadow);
        }

        Component styled = CommonClass.getStyledAmount(text);

        int vanillaWidth = font.width(text);
        int customWidth = font.width(styled);
        int widthDiff = vanillaWidth - customWidth;

        if (Services.PLATFORM.isModLoaded("sensible_stackables")) {
            boolean isModifiedByStackables = (text.length() > 2 || !text.chars().allMatch(Character::isDigit));

            if (isModifiedByStackables) {
                return CommonClass.drawStringWrapper(instance, font, styled, strX + widthDiff, strY, color);
            }
        }

        int xOffset = (style == ModConfig.FontStyle.CREATE) ? 16 : 17;
        return CommonClass.drawStringWrapper(instance, font, styled, itemX + xOffset - customWidth, strY, color);
    }
}