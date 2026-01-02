package meow.binary.o123456789.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiGraphics.class, priority = 4096)
public abstract class GuiGraphicsMixin {

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderFullyCustom(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (!stack.isEmpty()) {
            GuiGraphics self = (GuiGraphics) (Object) this;
            PoseStack pose = self.pose();

            String stringToRender = text;
            if (stringToRender == null && stack.getCount() != 1) {
                stringToRender = String.valueOf(stack.getCount());
            }

            if (stringToRender != null) {
                pose.pushPose();
                pose.translate(0.0F, 0.0F, 200.0F);

                Component component = Component.literal(stringToRender).withStyle(Style.EMPTY.withFont(O123456789.FONT));
                self.drawString(font, component, x + 19 - 2 - font.width(component), y + 6 + 3, 16777215, true);

                pose.popPose();
            }

            if (stack.isBarVisible()) {
                int barWidth = stack.getBarWidth();
                int barColor = stack.getBarColor();
                int bx = x + 2;
                int by = y + 13;
                self.fill(RenderType.guiOverlay(), bx, by, bx + 13, by + 2, -16777216);
                self.fill(RenderType.guiOverlay(), bx, by, bx + barWidth, by + 1, barColor | -16777216);
            }

            var player = Minecraft.getInstance().player;
            float cooldown = player == null ? 0.0F : player.getCooldowns().getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            if (cooldown > 0.0F) {
                int cy1 = y + Mth.floor(16.0F * (1.0F - cooldown));
                int cy2 = cy1 + Mth.ceil(16.0F * cooldown);
                self.fill(RenderType.guiOverlay(), x, cy1, x + 16, cy2, Integer.MAX_VALUE);
            }
        }

        ci.cancel();
    }
}