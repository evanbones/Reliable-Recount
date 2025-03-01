package meow.binary.o123456789.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.PoseStack;
import meow.binary.o123456789.O123456789;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Shadow
    public abstract void blit(ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight);

    @Shadow @Final
    private PoseStack pose;
    @Unique
    private static final ResourceLocation NUMBERS = ResourceLocation.fromNamespaceAndPath(O123456789.MOD_ID, "textures/gui/numbers.png");

    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V")
    )
    private void renderCount(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci, @Local(ordinal = 1) LocalRef<String> string) {
        if (!string.get().equals(String.valueOf(stack.getCount()))) {
            return;
        }

        pose.pushPose();
        pose.translate(0,0,200);
        char[] chars = string.get().toCharArray();
        for (int i = 0; i < string.get().length(); i++) {
            blit(NUMBERS, x + 11 - (string.get().length()-1) * 4 + i * 4, y+9, Character.getNumericValue(chars[i])*5, 0, 5, 7, 54, 7);
        }
        string.set("");
        pose.popPose();
    }
}