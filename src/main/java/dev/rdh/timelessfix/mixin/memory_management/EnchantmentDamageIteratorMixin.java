package dev.rdh.timelessfix.mixin.memory_management;

import dev.rdh.timelessfix.EnchantmentReferenceCleaner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.enchantment.EnchantmentHelper$DamageIterator")
abstract class EnchantmentDamageIteratorMixin implements EnchantmentReferenceCleaner.Clearable {
	@Shadow public EntityLivingBase user;
	@Shadow public Entity target;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void register(CallbackInfo ci) {
		EnchantmentReferenceCleaner.registerDamageIterator(this);
	}

	@Override
	public void clearReferences() {
		this.user = null;
		this.target = null;
	}
}
