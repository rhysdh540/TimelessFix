package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {

	private EntityLivingBaseMixin() { super(null); }

	@ModifyExpressionValue(method = "getLook", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/EntityLivingBase;prevRotationYawHead:F"))
	private float tf$usePrevYaw(float original) {
		return this.prevRotationYaw;
	}

	@ModifyExpressionValue(method = "getLook", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/EntityLivingBase;rotationYawHead:F"))
	private float tf$useYaw(float original) {
		return this.rotationYaw;
	}
}
