package dev.rdh.timelessfix.mixin.memory_management;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.timelessfix.EnchantmentReferenceCleaner;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
	@WrapMethod(method = "applyThornEnchantments")
	private static void clearHurtIterator(
		EntityLivingBase user,
		Entity attacker,
		Operation<Void> original
	) {
		try {
			original.call(user, attacker);
		} finally {
			EnchantmentReferenceCleaner.clearHurtIterator();
		}
	}

	@WrapMethod(method = "applyArthropodEnchantments")
	private static void clearDamageIterator(
		EntityLivingBase user,
		Entity target,
		Operation<Void> original
	) {
		try {
			original.call(user, target);
		} finally {
			EnchantmentReferenceCleaner.clearDamageIterator();
		}
	}
}
