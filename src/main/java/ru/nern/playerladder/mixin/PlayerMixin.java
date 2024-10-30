package ru.nern.playerladder.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.playerladder.SharedHandler;
import ru.nern.playerladder.mixin.shared.EntityMixin;
import ru.nern.playerladder.mixin.shared.LivingEntityMixin;

import java.util.stream.StreamSupport;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntityMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo callbackInfo) {
        SharedHandler.onPlayerTick((Player) (Object) this);
    }

    @Override
    public boolean startRiding(Entity entity, boolean bl) {
        if (entity == this.playerLadder$getVehicle()) {
            return false;
        } else if (!((EntityMixin)(Object)entity).invokeCouldAcceptPassenger()) {
            return false;
        } else {
            for(Entity entity2 = entity; ((EntityMixin)(Object)entity2).playerLadder$getVehicle() != null; entity2 = ((EntityMixin)(Object)entity2).playerLadder$getVehicle()) {
                if (((EntityMixin)(Object)entity2).playerLadder$getVehicle() == (Object)this) {
                    return false;
                }
            }

            if (bl || this.invokeCanRide(entity) && ((EntityMixin)(Object)entity).invokeCanAddPassenger((Entity)(Object)this)) {
                if (this.invokeIsPassenger()) {
                    this.invokeStopRiding();
                }

                this.invokeSetPose(Pose.STANDING);
                this.setVehicle(entity);
                ((EntityMixin)(Object)this.playerLadder$getVehicle()).addPassenger((Entity)(Object)this);
                StreamSupport.stream(((EntityMixin)(Object)entity).invokeGetIndirectPassengers().spliterator(), false).filter((entityx) -> {
                    return entityx instanceof ServerPlayer;
                }).forEach((entityx) -> {
                    CriteriaTriggers.START_RIDING_TRIGGER.trigger((ServerPlayer)entityx);
                });
                return true;
            } else {
                return false;
            }
        }
    }
}
