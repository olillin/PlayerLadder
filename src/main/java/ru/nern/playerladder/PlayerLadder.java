package ru.nern.playerladder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nern.playerladder.config.ConfigurationManager;

public class PlayerLadder implements ModInitializer {
	public static final String MOD_ID = "playerladder";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigurationManager.Config CONFIG = new ConfigurationManager.Config();

	@Override
	public void onInitialize() {
		ConfigurationManager.onInit();

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
				SharedHandler.onLogOut(handler.player));

		UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> switch (PlayerLadder.CONFIG.server.mode) {
            case RIDE -> SharedHandler.startRidingEntity(player, entity, level, hand);
            case PICK_UP -> SharedHandler.pickUpEntity(player, entity, level, hand);
            case DO_NOTHING -> InteractionResult.PASS;
        });
	}
}