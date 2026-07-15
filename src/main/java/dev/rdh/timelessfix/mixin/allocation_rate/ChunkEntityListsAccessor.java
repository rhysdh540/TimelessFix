package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Chunk.class)
public interface ChunkEntityListsAccessor {
	@Accessor("entityLists")
	ClassInheritanceMultiMap<Entity>[] timelessFix$getEntityLists();
}
