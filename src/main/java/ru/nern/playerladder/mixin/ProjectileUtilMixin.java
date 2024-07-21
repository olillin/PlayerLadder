package ru.nern.playerladder.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import ru.nern.playerladder.ClientConfig;

@Mixin(ProjectileUtil.class)
public final class ProjectileUtilMixin {

    @ModifyExpressionValue(
            method = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;contains(Lnet/minecraft/world/phys/Vec3;)Z", ordinal = 0)
    )
    private static boolean playerladder$allowInteractions(boolean original, Entity pShooter, @Local(ordinal = 2) Entity hitEntity) {
        return ClientConfig.allowInteractions ? hitEntity.getVehicle() != pShooter : original;
    }
}
