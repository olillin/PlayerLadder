package ru.nern.playerladder.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static ru.nern.playerladder.PlayerLadder.CONFIG;

public class ClothConfigIntegration {
    public static Screen generateConfigScreen(Screen parent)
    {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.playerladder.config"));

        builder.setSavingRunnable(ConfigurationManager::saveConfig);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory serverCategory = builder.getOrCreateCategory(Component.translatable("server.playerladder.config"));

        serverCategory.addEntry(entryBuilder.startEnumSelector(Component.translatable("rightClickMode.playerladder.config"), ConfigurationManager.ClickMode.class, CONFIG.server.mode)
                .setTooltip(Component.translatable("rightClickMode.playerladder.description"))
                .setSaveConsumer(clickMode -> CONFIG.server.mode = clickMode).build());

        serverCategory.addEntry(entryBuilder.startIntField(Component.translatable("pickUpLimit.playerladder.config"), CONFIG.server.pickUpLimit)
                .setMin(1)
                .setTooltip(Component.translatable("pickUpLimit.playerladder.description"))
                .setSaveConsumer(value -> CONFIG.server.pickUpLimit = value).build());

        serverCategory.addEntry(entryBuilder.startIntField(Component.translatable("stepUpLimit.playerladder.config"), CONFIG.server.stepUpLimit)
                .setMin(1)
                .setTooltip(Component.translatable("stepUpLimit.playerladder.description"))
                .setSaveConsumer(value -> CONFIG.server.stepUpLimit = value).build());

        serverCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("interactWithAnyLiving.playerladder.config"), CONFIG.server.interactWithAnyLiving)
                .setTooltip(Component.translatable("interactWithAnyLiving.playerladder.description"))
                .setSaveConsumer(value -> CONFIG.server.interactWithAnyLiving = value).build());
        serverCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("rideExtension.playerladder.config"), CONFIG.server.rideExtension)
                .setTooltip(Component.translatable("rideExtension.playerladder.description"))
                .setSaveConsumer(value -> CONFIG.server.rideExtension = value).build());


        ConfigCategory clientCategory = builder.getOrCreateCategory(Component.translatable("client.playerladder.config"));

        clientCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("allowInteractions.playerladder.config"), CONFIG.client.allowInteractions)
                .setTooltip(Component.translatable("allowInteractions.playerladder.description"))
                .setSaveConsumer(value -> CONFIG.client.allowInteractions = value).build());

        return builder.build();
    }
}
