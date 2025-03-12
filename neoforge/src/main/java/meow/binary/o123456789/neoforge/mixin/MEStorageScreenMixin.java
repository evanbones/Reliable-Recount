package meow.binary.o123456789.neoforge.mixin;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.core.AEConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MEStorageScreen.class)
public class MEStorageScreenMixin {


    @Redirect(method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/me/common/StackSizeRenderer;renderSizeLabel(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Font;FFLjava/lang/String;Z)V",
                    ordinal = 0
            ),
            require = 0
    )
    private void redirected(GuiGraphics guiGraphics, Font fontRenderer, float xPos, float yPos, String text, boolean largeFonts) {
        O123456789.renderSizeLabel(guiGraphics, fontRenderer, xPos, yPos, Component.literal(text).withStyle(Style.EMPTY.withFont(O123456789.FONT)));
    }
}
