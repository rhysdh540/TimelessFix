package dev.rdh.timelessfix.mixin.allocation_rate;

import com.google.common.collect.Lists;
import dev.rdh.timelessfix.EntityQuery;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(World.class)
abstract class WorldCollisionMixin {
	@Shadow public abstract WorldBorder getWorldBorder();
	@Shadow public abstract boolean isInsideBorder(WorldBorder border, Entity entity);
	@Shadow public abstract boolean isBlockLoaded(BlockPos pos);
	@Shadow public abstract IBlockState getBlockState(BlockPos pos);

	@Overwrite
	public List<AxisAlignedBB> getCollidingBoundingBoxes(Entity entity, AxisAlignedBB box) {
		List<AxisAlignedBB> boxes = Lists.newArrayList();
		int minX = MathHelper.floor_double(box.minX);
		int maxX = MathHelper.floor_double(box.maxX + 1.0);
		int minY = MathHelper.floor_double(box.minY);
		int maxY = MathHelper.floor_double(box.maxY + 1.0);
		int minZ = MathHelper.floor_double(box.minZ);
		int maxZ = MathHelper.floor_double(box.maxZ + 1.0);
		WorldBorder border = this.getWorldBorder();
		boolean wasOutside = entity.isOutsideBorder();
		boolean isInside = this.isInsideBorder(border, entity);
		IBlockState borderState = Blocks.stone.getDefaultState();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = minX; x < maxX; x++) {
			for (int z = minZ; z < maxZ; z++) {
				if (this.isBlockLoaded(pos.set(x, 64, z))) {
					for (int y = minY - 1; y < maxY; y++) {
						pos.set(x, y, z);
						if (wasOutside && isInside) {
							entity.setOutsideBorder(false);
						} else if (!wasOutside && !isInside) {
							entity.setOutsideBorder(true);
						}

						IBlockState state = borderState;
						if (border.contains(pos) || !isInside) {
							state = this.getBlockState(pos);
						}
						state.getBlock().addCollisionBoxesToList((World) (Object) this, pos, state, box, boxes, entity);
					}
				}
			}
		}

		EntityQuery.addCollisionBoxes((World) (Object) this, entity, box.expand(0.25, 0.25, 0.25), box, boxes);
		return boxes;
	}
}
