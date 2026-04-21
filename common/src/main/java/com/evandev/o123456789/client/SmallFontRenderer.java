package com.evandev.o123456789.client;

import com.evandev.o123456789.Constants;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;

public final class SmallFontRenderer {
    public static final Identifier FONT_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "3x5");
    public static final Identifier FONT_TINY_ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "3x5_tiny");
    public static final FontDescription FONT = new FontDescription.Resource(FONT_ID);
    public static final FontDescription FONT_TINY = new FontDescription.Resource(FONT_TINY_ID);

    public static void drawStringWrapper(GuiGraphicsExtractor guiGraphics, Font font, Component text, int x, int y, int color) {
        guiGraphics.text(font, text, x, y, color, false);
    }
}