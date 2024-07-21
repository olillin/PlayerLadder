package ru.nern.playerladder.mixin.shared;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.playerladder.SharedHandler;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "removePassenger", at = @At("TAIL"))
    private void playerladder$removePassenger(Entity passenger, CallbackInfo ci) {
        SharedHandler.onDismount((Entity) (Object) this);
    }

    @Inject(method = "addPassenger", at = @At("TAIL"))
    private void playerladder$onAddPassenger(Entity passenger, CallbackInfo ci) {
        SharedHandler.onMount((Entity) (Object) this, passenger);
    }
}
