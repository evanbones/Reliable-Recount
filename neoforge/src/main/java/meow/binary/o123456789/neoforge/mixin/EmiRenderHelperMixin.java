package meow.binary.o123456789.neoforge.mixin;

import dev.emi.emi.EmiRenderHelper;
import dev.emi.emi.runtime.EmiDrawContext;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiRenderHelper.class)
public class EmiRenderHelperMixin {
    @Inject(method = "renderAmount", at = @At("HEAD"), require = 0, cancellable = true)
    private static void changeFont(EmiDrawContext context, int x, int y, Component amount, CallbackInfo ci) {
        O123456789.renderSizeLabel(context.raw(), Minecraft.getInstance().font, x, y, amount.copy().withStyle(Style.EMPTY.withFont(O123456789.FONT)));
        ci.cancel();
    }
}
