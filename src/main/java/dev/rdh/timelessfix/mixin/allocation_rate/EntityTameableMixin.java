package dev.rdh.timelessfix.mixin.allocation_rate;

import java.util.UUID;
import net.minecraft.entity.passive.EntityTameable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityTameable.class)
abstract class EntityTameableMixin {
	@Unique private String timelessFix$ownerId;
	@Unique private UUID timelessFix$ownerUuid;

	@Redirect(
		method = "getOwner",
		at = @At(value = "INVOKE", target = "Ljava/util/UUID;fromString(Ljava/lang/String;)Ljava/util/UUID;")
	)
	private UUID cacheOwnerUuid(String ownerId) {
		if (ownerId.equals(this.timelessFix$ownerId)) {
			return this.timelessFix$ownerUuid;
		}

		this.timelessFix$ownerId = ownerId;
		try {
			return this.timelessFix$ownerUuid = UUID.fromString(ownerId);
		} catch (IllegalArgumentException ignored) {
			return this.timelessFix$ownerUuid = null;
		}
	}
}
