package ru.nern.playerladder;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;


@EventBusSubscriber(modid = PlayerLadder.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.EnumValue<ClickMode> MODE = BUILDER
            .comment("Action on Right Click")
            .defineEnum("rightClickMode", ClickMode.RIDE);

    private static final ModConfigSpec.BooleanValue INTERACT_WITH_ANY_LIVING = BUILDER
            .comment("Allows you to ride or pick up any living entity(dependending on the mode selected)")
            .define("interactWithAnyLiving", false);

    private static final ModConfigSpec.BooleanValue RIDE_EXTENSION = BUILDER
            .comment("Allows the /ride command to mount entities on top of players")
            .define("rideCommandExtension", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static ClickMode mode;
    public static boolean interactWithAnyLiving;
    public static boolean rideExtension;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if(event.getConfig().getType() == ModConfig.Type.COMMON) {
            mode = MODE.get();
            interactWithAnyLiving = INTERACT_WITH_ANY_LIVING.get();
            rideExtension = RIDE_EXTENSION.get();
        }
    }

    public enum ClickMode {
        RIDE,
        PICK_UP,
        DO_NOTHING
    }
}
