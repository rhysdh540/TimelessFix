package dev.rdh.timelessfix;

import dev.rdh.timelessfix.mixin.allocation_rate.ChunkEntityListsAccessor;
import dev.rdh.timelessfix.mixin.allocation_rate.ClassInheritanceMultiMapAccessor;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public final class EntityQuery {
	private EntityQuery() {}

	public static void addCollisionBoxes(World world, Entity subject, AxisAlignedBB queryBox,
		AxisAlignedBB collisionBox, List<AxisAlignedBB> boxes) {
		scan(world, subject, queryBox, collisionBox, boxes);
	}

	public static void pushCollidingEntities(World world, Entity subject, AxisAlignedBB queryBox) {
		scan(world, subject, queryBox, null, null);
	}

	private static void scan(World world, Entity subject, AxisAlignedBB queryBox,
		AxisAlignedBB collisionBox, List<AxisAlignedBB> boxes) {
		int minChunkX = MathHelper.floor_double((queryBox.minX - 2.0) / 16.0);
		int maxChunkX = MathHelper.floor_double((queryBox.maxX + 2.0) / 16.0);
		int minChunkZ = MathHelper.floor_double((queryBox.minZ - 2.0) / 16.0);
		int maxChunkZ = MathHelper.floor_double((queryBox.maxZ + 2.0) / 16.0);

		for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
			for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
				if (!world.getChunkProvider().chunkExists(chunkX, chunkZ)) {
					continue;
				}

				Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
				ClassInheritanceMultiMap<Entity>[] entityLists =
					((ChunkEntityListsAccessor) chunk).timelessFix$getEntityLists();
				int minSection = MathHelper.clamp_int(
					MathHelper.floor_double((queryBox.minY - 2.0) / 16.0), 0, entityLists.length - 1
				);
				int maxSection = MathHelper.clamp_int(
					MathHelper.floor_double((queryBox.maxY + 2.0) / 16.0), 0, entityLists.length - 1
				);

				for (int section = minSection; section <= maxSection; section++) {
					List<Entity> entities =
						((ClassInheritanceMultiMapAccessor) entityLists[section]).timelessFix$getValues();
					for (int i = 0; i < entities.size(); i++) {
						Entity entity = entities.get(i);
						if (entity != subject && entity.getEntityBoundingBox().intersectsWith(queryBox)) {
							process(subject, entity, collisionBox, boxes);
							Entity[] parts = entity.getParts();
							if (parts != null) {
								for (Entity part : parts) {
									if (part != subject && part.getEntityBoundingBox().intersectsWith(queryBox)) {
										process(subject, part, collisionBox, boxes);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private static void process(Entity subject, Entity entity, AxisAlignedBB collisionBox,
		List<AxisAlignedBB> boxes) {
		if (!EntitySelectors.NOT_SPECTATING.apply(entity)) {
			return;
		}
		if (boxes == null) {
			if (entity.canBePushed()) {
				entity.applyEntityCollision(subject);
			}
			return;
		}
		if (subject.riddenByEntity == entity || subject.ridingEntity == entity) {
			return;
		}

		AxisAlignedBB box = entity.getCollisionBoundingBox();
		if (box != null && box.intersectsWith(collisionBox)) {
			boxes.add(box);
		}

		box = subject.getCollisionBox(entity);
		if (box != null && box.intersectsWith(collisionBox)) {
			boxes.add(box);
		}
	}
}
