package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
abstract class RendererLivingEntityMixin {
	@Unique private static final IChatComponent timelessFix$emptyName = new ChatComponentText("");
	@Unique private String timelessFix$renderedName;

	@Redirect(
		method = "renderName",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getDisplayName()Lnet/minecraft/util/IChatComponent;")
	)
	private IChatComponent prepareRenderedName(EntityLivingBase entity) {
		String name = entity.getName();
		this.timelessFix$renderedName = entity instanceof EntityPlayer ? ScorePlayerTeam.formatPlayerName(entity.getTeam(), name) : name;
		return timelessFix$emptyName;
	}

	@Redirect(
		method = "renderName",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IChatComponent;getFormattedText()Ljava/lang/String;")
	)
	private String usePreparedName(IChatComponent ignored) {
		return this.timelessFix$renderedName;
	}

	@Redirect(
		method = "rotateCorpse",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;getName()Ljava/lang/String;")
	)
	private String skipDefaultName(EntityLivingBase entity) {
		return entity instanceof EntityPlayer || entity.hasCustomName() ? entity.getName() : null;
	}
}
