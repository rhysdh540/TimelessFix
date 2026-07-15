package dev.rdh.timelessfix.mixin.allocation_rate;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClassInheritanceMultiMap.class)
public interface ClassInheritanceMultiMapAccessor {
	@Accessor("values")
	List<Entity> timelessFix$getValues();
}
