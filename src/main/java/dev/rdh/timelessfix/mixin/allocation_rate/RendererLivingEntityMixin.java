package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
abstract class RendererLivingEntityMixin {
	@Redirect(
		method = "rotateCorpse",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getName()Ljava/lang/String;")
	)
	private String skipDefaultName(EntityLivingBase entity) {
		return entity instanceof EntityPlayer || entity.hasCustomName() ? entity.getName() : null;
	}
}
