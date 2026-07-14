package dev.rdh.timelessfix.mixin.client;

import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
abstract class WorldMixin {
	@Shadow @Final public boolean isRemote;
	@Shadow protected abstract int getRenderDistanceChunks();

	@Unique private int timelessFix$chunkX = Integer.MIN_VALUE;
	@Unique private int timelessFix$chunkZ = Integer.MIN_VALUE;
	@Unique private int timelessFix$renderDistance = Integer.MIN_VALUE;
	@Unique private boolean timelessFix$skipActiveChunkBuild;

	@Inject(method = "setActivePlayerChunksAndCheckLight", at = @At("HEAD"))
	private void checkActiveChunkBuild(CallbackInfo ci) {
		if (!this.isRemote) {
			this.timelessFix$skipActiveChunkBuild = false;
			return;
		}

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player == null) {
			this.timelessFix$skipActiveChunkBuild = false;
			return;
		}

		int chunkX = MathHelper.floor_double(player.posX / 16.0D);
		int chunkZ = MathHelper.floor_double(player.posZ / 16.0D);
		int renderDistance = this.getRenderDistanceChunks();
		this.timelessFix$skipActiveChunkBuild = chunkX == this.timelessFix$chunkX
			&& chunkZ == this.timelessFix$chunkZ
			&& renderDistance == this.timelessFix$renderDistance;
		this.timelessFix$chunkX = chunkX;
		this.timelessFix$chunkZ = chunkZ;
		this.timelessFix$renderDistance = renderDistance;
	}

	@Redirect(
		method = "setActivePlayerChunksAndCheckLight",
		at = @At(value = "INVOKE", target = "Ljava/util/Set;clear()V")
	)
	private void keepActiveChunks(Set<ChunkCoordIntPair> chunks) {
		if (!this.isRemote || !this.timelessFix$skipActiveChunkBuild) {
			chunks.clear();
		}
	}

	@Redirect(
		method = "setActivePlayerChunksAndCheckLight",
		at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0)
	)
	private int activeChunkPlayerCount(List<EntityPlayer> players) {
		if (!this.isRemote) {
			return players.size();
		}
		return this.timelessFix$skipActiveChunkBuild || Minecraft.getMinecraft().thePlayer == null ? 0 : 1;
	}

	@Redirect(
		method = "setActivePlayerChunksAndCheckLight",
		at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0)
	)
	private Object useLocalPlayer(List<EntityPlayer> players, int index) {
		return this.isRemote ? Minecraft.getMinecraft().thePlayer : players.get(index);
	}
}
