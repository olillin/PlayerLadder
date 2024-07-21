package ru.nern.playerladder;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = PlayerLadder.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ALLOW_INTERACTIONS = BUILDER
            .comment("When enabled, allows you to interact with the world when carrying someone on your head")
            .define("allowInteractions", true);


    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean allowInteractions;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if(event.getConfig().getType() == ModConfig.Type.CLIENT) {
            allowInteractions = ALLOW_INTERACTIONS.get();
        }
    }
}
