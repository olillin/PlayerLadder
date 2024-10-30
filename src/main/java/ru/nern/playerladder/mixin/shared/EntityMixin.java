package ru.nern.playerladder.mixin.shared;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.playerladder.SharedHandler;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Invoker
    public abstract boolean invokeCouldAcceptPassenger();

    @Accessor("type")
    public abstract EntityType<?> playerLadder$getType();

    @Accessor("vehicle")
    public abstract Entity playerLadder$getVehicle();

    @Accessor
    protected abstract Level getLevel();

    @Invoker
    protected abstract boolean invokeCanRide(Entity entity);

    @Invoker
    public abstract boolean invokeCanAddPassenger(Entity entity);

    @Invoker
    protected abstract boolean invokeIsPassenger();

    @Invoker
    protected abstract void invokeStopRiding();

    @Invoker
    protected abstract void invokeSetPose(Pose pose);

    @Accessor
    protected abstract void setVehicle(Entity entity);

    @Invoker
    public abstract Iterable<Entity> invokeGetIndirectPassengers();

    @Shadow
    public abstract void addPassenger(Entity passenger);

    public abstract boolean startRiding(Entity entity, boolean bl);

    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void playerladder$removePassenger(Entity passenger, CallbackInfo ci) {
        SharedHandler.onDismount((Entity) (Object) this);
    }

    @Inject(method = "addPassenger", at = @At("TAIL"))
    public void playerladder$onAddPassenger(Entity passenger, CallbackInfo ci) {
        SharedHandler.onMount((Entity) (Object) this, passenger);
    }
}
