package meow.binary.o123456789.neoforge.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StorageScreenBase.class)
public class StorageScreenBaseMixin {
    @Inject(method = "renderStackCount", at = @At("HEAD"), cancellable = true, require = 0)
    private void redirected(GuiGraphics guiGraphics, String count, int x, int y, CallbackInfo ci) {
        Component component = Component.literal(count).withStyle(Style.EMPTY.withFont(O123456789.FONT));
        PoseStack poseStack = guiGraphics.pose();
        Font font = Minecraft.getInstance().font;
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 200.0F);
        float scale = Math.min(1.0F, 15F / (float)font.width(component));
        if (scale < 1.0F) {
            poseStack.scale(scale, scale, 1.0F);
        }

        guiGraphics.drawString(font, component,
                (int) ((x + 16)/scale - font.width(component)),
                (int) ((y + 16)/scale - 7),
                16777215
        );
        poseStack.popPose();
        ci.cancel();
    }
}
