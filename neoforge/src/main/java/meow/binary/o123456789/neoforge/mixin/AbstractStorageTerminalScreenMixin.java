package meow.binary.o123456789.neoforge.mixin;

import com.hollingsworth.arsnouveau.client.container.AbstractStorageTerminalScreen;
import com.hollingsworth.arsnouveau.client.container.NumberFormatUtil;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractStorageTerminalScreen.class)
public class AbstractStorageTerminalScreenMixin {
    @Inject(method = "drawStackSize", at = @At("HEAD"), require = 0, cancellable = true)
    private void injected(GuiGraphics graphics, Font fr, long size, int x, int y, CallbackInfo ci) {
        String stackSize = NumberFormatUtil.formatNumber(size);
        O123456789.renderSizeLabel(graphics, fr, x, y, Component.literal(stackSize).withStyle(Style.EMPTY.withFont(O123456789.FONT)));
        ci.cancel();
    }
}
