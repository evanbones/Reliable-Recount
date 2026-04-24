package com.evandev.reliable_recount.client.integration;

import com.evandev.reliable_recount.config.ModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigIntegration {

    public static Screen createScreen(Screen parent) {
        ModConfig config = ModConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.reliable_recount.title"));

        builder.setSavingRunnable(ModConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.reliable_recount.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startEnumSelector(
                        Component.translatable("config.reliable_recount.font_style"),
                        ModConfig.FontStyle.class,
                        config.fontStyle
                ).setDefaultValue(ModConfig.FontStyle.CREATE)
                .setSaveConsumer(newValue -> config.fontStyle = newValue)
                .build());

        return builder.build();
    }
}