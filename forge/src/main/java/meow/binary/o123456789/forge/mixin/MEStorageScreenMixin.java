package meow.binary.o123456789.forge.mixin;

import appeng.client.gui.me.common.MEStorageScreen;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MEStorageScreen.class)
public class MEStorageScreenMixin {


    @Redirect(method = "m_280092_",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/me/common/StackSizeRenderer;renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;Z)V",
                    ordinal = 0
            ),
            require = 0,
            remap = false
    )
    private void redirected(GuiGraphics guiGraphics, Font fontRenderer, float xPos, float yPos, String text, boolean largeFonts) {
        O123456789.renderSizeLabel(guiGraphics, fontRenderer, xPos, yPos, Component.literal(text).withStyle(Style.EMPTY.withFont(O123456789.FONT)));
    }
}
