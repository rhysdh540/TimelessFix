package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// Credits to Axolotl Client's Old Animations mod for this
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	@ModifyExpressionValue(method = "getRotationVec", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/living/LivingEntity;lastHeadYaw:F"))
	private float timelessFix$usePrevYaw(float original) {
		return lastYaw;
	}

	@ModifyExpressionValue(method = "getRotationVec", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/living/LivingEntity;headYaw:F"))
	private float timelessFix$useYaw(float original) {
		return yaw;
	}
}
