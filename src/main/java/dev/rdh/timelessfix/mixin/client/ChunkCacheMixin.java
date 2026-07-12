package dev.rdh.timelessfix.mixin.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ChunkCache.class)
abstract class ChunkCacheMixin {
	@Shadow protected World worldObj;
	@Shadow protected int chunkX;
	@Shadow protected int chunkZ;
	@Shadow protected Chunk[][] chunkArray;
	@Shadow public abstract IBlockState getBlockState(BlockPos pos);
	@Shadow public abstract int getLightFor(EnumSkyBlock type, BlockPos pos);

	/**
	 * @author embeddedt
	 * @reason Reuse one mutable position instead of allocating six neighbor positions.
	 */
	@Overwrite
	private int getLightForExt(EnumSkyBlock type, BlockPos pos) {
		if (type == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
			return 0;
		}

		if (pos.getY() < 0 || pos.getY() >= 256) {
			return type.defaultLightValue;
		}

		if (this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
			BlockPos.MutableBlockPos neighbor = new BlockPos.MutableBlockPos();
			int light = 0;
			for (EnumFacing facing : EnumFacing.values()) {
				neighbor.set(
					pos.getX() + facing.getFrontOffsetX(),
					pos.getY() + facing.getFrontOffsetY(),
					pos.getZ() + facing.getFrontOffsetZ()
				);
				light = Math.max(light, this.getLightFor(type, neighbor));
				if (light == 15) {
					return light;
				}
			}
			return light;
		}

		int x = (pos.getX() >> 4) - this.chunkX;
		int z = (pos.getZ() >> 4) - this.chunkZ;
		return this.chunkArray[x][z].getLightFor(type, pos);
	}
}
