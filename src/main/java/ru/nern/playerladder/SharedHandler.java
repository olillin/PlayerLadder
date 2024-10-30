package ru.nern.playerladder;

import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static ru.nern.playerladder.PlayerLadder.CONFIG;

public class SharedHandler {
    public static InteractionResult startRidingEntity(Player player, Entity newVehicle, Level level, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && canRide(newVehicle) && player.getItemInHand(hand).isEmpty()) {
            Entity vehicle = getHighestOrSelf(newVehicle, player, CONFIG.server.stepUpLimit);
            if(vehicle == null) return InteractionResult.FAIL;
            player.startRiding(vehicle);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult pickUpEntity(Player player, Entity newPassenger, Level level, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && canPickUp(newPassenger) && player.getItemInHand(hand).isEmpty()) {
            Entity vehicle = getHighestOrSelf(player, newPassenger, CONFIG.server.pickUpLimit);
            if(vehicle == null) return InteractionResult.FAIL;
            newPassenger.startRiding(vehicle, true);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static Entity getHighestOrSelf(Entity vehicle, Entity newPassenger, int limit) {
        int count = -1;
        while (vehicle.isVehicle()) {
            count++;
            vehicle = vehicle.getFirstPassenger();
            if(vehicle == newPassenger || count >= limit) return null;
        }
        return vehicle;
    }

    private static boolean canPickUp(Entity entity) {
        return PlayerLadder.CONFIG.server.interactWithAnyLiving && !(entity instanceof Saddleable) || entity instanceof Player;
    }

    private static boolean canRide(Entity entity) {
        return PlayerLadder.CONFIG.server.interactWithAnyLiving || entity instanceof Player;
    }

    public static void onMount(Entity vehicle, Entity passenger) {
        if(!vehicle.level().isClientSide && vehicle instanceof Player) {
            LOGGER.info(String.format("onMount %1$s %2$s", vehicle, passenger));
            ((ServerPlayer)vehicle).connection.send(new ClientboundSetPassengersPacket(vehicle));
        }
    }

    public static void onDismount(Entity vehicle) {
        if(!vehicle.level().isClientSide && vehicle instanceof Player)
            ((ServerPlayer) vehicle).connection.send(new ClientboundSetPassengersPacket(vehicle));
    }

    public static void onPlayerTick(Player player) {
        if(!player.level().isClientSide && player.onGround() && player.isVehicle() && player.isCrouching())
            player.getFirstPassenger().stopRiding();
    }

    public static void onLogOut(Player player) {
        if(player.isPassenger() && player.getVehicle() instanceof Player)
            player.stopRiding();
    }

    public static void onGameModeChange(Player player) {
        if(player.isVehicle())
            player.getFirstPassenger().stopRiding();
    }
}
