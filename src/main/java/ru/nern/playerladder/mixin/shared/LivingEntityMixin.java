package ru.nern.playerladder.mixin.shared;

import net.minecraft.world.entity.Attackable;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin implements Attackable {
}
