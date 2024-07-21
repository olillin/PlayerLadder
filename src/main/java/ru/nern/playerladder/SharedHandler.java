package ru.nern.playerladder;

import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SharedHandler {
    public static InteractionResult startRidingEntity(Player player, Entity newVehicle, Level level, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && canInteractWith(newVehicle) && player.getItemInHand(hand).isEmpty()) {
            Entity vehicle = newVehicle;

            while (vehicle.isVehicle() && vehicle.getFirstPassenger() != player)
                vehicle = vehicle.getFirstPassenger();
            player.startRiding(vehicle);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public static InteractionResult pickUpEntity(Player player, Entity newPassenger, Level level, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND && canInteractWith(newPassenger) && player.getItemInHand(hand).isEmpty() && player.getFirstPassenger() != newPassenger) {
            Entity vehicle = player;

            while (vehicle.isVehicle() && vehicle.getFirstPassenger() != player)
                vehicle = vehicle.getFirstPassenger();
            newPassenger.startRiding(vehicle);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static boolean canInteractWith(Entity entity) {
        return Config.interactWithAnyLiving && !(entity instanceof Saddleable) || entity instanceof Player;
    }

    public static void onMount(Entity vehicle, Entity passenger) {
        if(!vehicle.level().isClientSide && vehicle instanceof Player) {
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
