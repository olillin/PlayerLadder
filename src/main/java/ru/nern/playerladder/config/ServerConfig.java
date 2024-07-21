package ru.nern.playerladder.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import ru.nern.playerladder.PlayerLadder;


@EventBusSubscriber(modid = PlayerLadder.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ServerConfig
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.EnumValue<ClickMode> MODE = BUILDER
            .comment("Action that occurs when you click on a player/entity.")
            .defineEnum("rightClickMode", ClickMode.RIDE);

    private static final ModConfigSpec.IntValue PICK_UP_LIMIT = BUILDER
            .comment("Limits how many entities a player can pick up.")
            .defineInRange("pickUpLimit", 16, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue STEP_UP_LIMIT = BUILDER
        .comment("Limits how many entities up a player can go if they click on an entity with passengers.")
        .defineInRange("stepUpLimit", 16, 1, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue INTERACT_WITH_ANY_LIVING = BUILDER
            .comment("If enabled, allows you to ride or pick up any living entity, not just players.")
            .define("interactWithAnyLiving", false);

    private static final ModConfigSpec.BooleanValue RIDE_EXTENSION = BUILDER
            .comment("Allows the /ride command to mount entities on top of players.")
            .define("rideCommandExtension", true);


    public static final ModConfigSpec SPEC = BUILDER.build();

    public static ClickMode mode;
    public static int pickUpLimit;
    public static int stepUpLimit;
    public static boolean interactWithAnyLiving;
    public static boolean rideExtension;


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if(event.getConfig().getType() == ModConfig.Type.COMMON) {
            mode = MODE.get();
            pickUpLimit = PICK_UP_LIMIT.get();
            stepUpLimit = STEP_UP_LIMIT.get();
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
