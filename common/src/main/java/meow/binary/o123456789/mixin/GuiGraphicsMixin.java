package meow.binary.o123456789.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Redirect(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I")
    )
    private int redirected(GuiGraphics instance, Font font, String text, int x, int y, int color, boolean dropShadow,
                           @Local(ordinal = 0, argsOnly = true) int itemX,
                           @Local(ordinal = 1, argsOnly = true) int itemY
    ) {
        Component component = Component.literal(text).withStyle(Style.EMPTY.withFont(O123456789.FONT));
        return O123456789.drawStringWrapper(instance, font, component, itemX + 16 - font.width(component), y, color);
    }
}