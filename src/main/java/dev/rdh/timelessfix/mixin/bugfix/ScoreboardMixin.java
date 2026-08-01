package dev.rdh.timelessfix.mixin.bugfix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

@Mixin(Scoreboard.class)
public class ScoreboardMixin {
	@Inject(method = "removeTeam", at = @At("HEAD"), cancellable = true)
	private void dontRemoveNull(ScorePlayerTeam team, CallbackInfo ci) {
		if (team == null) {
			ci.cancel();
		}
	}
}
