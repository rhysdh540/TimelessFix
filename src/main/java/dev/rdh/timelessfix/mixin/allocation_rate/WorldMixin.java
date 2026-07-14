package dev.rdh.timelessfix.mixin.allocation_rate;

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
	@Shadow @Final public List<EntityPlayer> playerEntities;
	@Shadow protected abstract int getRenderDistanceChunks();

	@Unique private int timelessFix$chunkX = Integer.MIN_VALUE;
	@Unique private int timelessFix$chunkZ = Integer.MIN_VALUE;
	@Unique private int timelessFix$renderDistance = Integer.MIN_VALUE;
	@Unique private int[] timelessFix$playerChunkXs = new int[0];
	@Unique private int[] timelessFix$playerChunkZs = new int[0];
	@Unique private boolean timelessFix$skipActiveChunkBuild;

	@Inject(method = "setActivePlayerChunksAndCheckLight", at = @At("HEAD"))
	private void checkActiveChunkBuild(CallbackInfo ci) {
		int renderDistance = this.getRenderDistanceChunks();
		if (!this.isRemote) {
			int playerCount = this.playerEntities.size();
			this.timelessFix$skipActiveChunkBuild = renderDistance == this.timelessFix$renderDistance
				&& playerCount == this.timelessFix$playerChunkXs.length;
			if (playerCount != this.timelessFix$playerChunkXs.length) {
				this.timelessFix$playerChunkXs = new int[playerCount];
				this.timelessFix$playerChunkZs = new int[playerCount];
			}

			for (int i = 0; i < playerCount; i++) {
				EntityPlayer player = this.playerEntities.get(i);
				int chunkX = MathHelper.floor_double(player.posX / 16.0D);
				int chunkZ = MathHelper.floor_double(player.posZ / 16.0D);
				this.timelessFix$skipActiveChunkBuild &= chunkX == this.timelessFix$playerChunkXs[i]
					&& chunkZ == this.timelessFix$playerChunkZs[i];
				this.timelessFix$playerChunkXs[i] = chunkX;
				this.timelessFix$playerChunkZs[i] = chunkZ;
			}
			this.timelessFix$renderDistance = renderDistance;
			return;
		}

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player == null) {
			this.timelessFix$skipActiveChunkBuild = false;
			return;
		}

		int chunkX = MathHelper.floor_double(player.posX / 16.0D);
		int chunkZ = MathHelper.floor_double(player.posZ / 16.0D);
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
		if (!this.timelessFix$skipActiveChunkBuild) {
			chunks.clear();
		}
	}

	@Redirect(
		method = "setActivePlayerChunksAndCheckLight",
		at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0)
	)
	private int activeChunkPlayerCount(List<EntityPlayer> players) {
		if (this.timelessFix$skipActiveChunkBuild) {
			return 0;
		}
		if (!this.isRemote) {
			return players.size();
		}
		return Minecraft.getMinecraft().thePlayer == null ? 0 : 1;
	}

	@Redirect(
		method = "setActivePlayerChunksAndCheckLight",
		at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0)
	)
	private Object useLocalPlayer(List<EntityPlayer> players, int index) {
		return this.isRemote ? Minecraft.getMinecraft().thePlayer : players.get(index);
	}
}
