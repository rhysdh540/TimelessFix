package dev.rdh.timelessfix.mixin.memory_management;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = "net.minecraft.block.state.BlockState$StateImplementation")
abstract class BlockStateStateImplementationMixin {
	@Shadow @Final private ImmutableMap<IProperty, Comparable> properties;
	@Unique private IBlockState[] timelessfix$transitions;

	@Overwrite
	public void buildPropertyValueTable(Map<Map<IProperty, Comparable>, ?> states) {
		int size = 0;
		for (IProperty<?> property : this.properties.keySet()) {
			size += property.getAllowedValues().size();
		}

		this.timelessfix$transitions = new IBlockState[size];
		Map<IProperty, Comparable> values = new HashMap<>(this.properties);
		int index = 0;

		for (IProperty<? extends Comparable> property : this.properties.keySet()) {
			Comparable current = this.properties.get(property);
			for (Comparable value : property.getAllowedValues()) {
				values.put(property, value);
				IBlockState state = (IBlockState) states.get(values);
				if (state == null) {
					throw new IllegalStateException();
				}
				this.timelessfix$transitions[index++] = state;
			}
			values.put(property, current);
		}
	}

	@Overwrite
	public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
		if (!this.properties.containsKey(property)) {
			throw new IllegalArgumentException("Cannot get property " + property);
		}
		if (!property.getAllowedValues().contains(value)) {
			throw new IllegalArgumentException("Cannot set property " + property + " to " + value);
		}
		if (this.properties.get(property) == value) {
			return (IBlockState) (Object) this;
		}

		int index = 0;
		for (IProperty<?> candidate : this.properties.keySet()) {
			for (Object allowed : candidate.getAllowedValues()) {
				if (candidate.equals(property) && allowed.equals(value)) {
					return this.timelessfix$transitions[index];
				}
				index++;
			}
		}

		throw new IllegalStateException();
	}
}
