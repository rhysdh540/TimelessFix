package dev.rdh.timelessfix.mixin.client;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(VisGraph.class)
abstract class VisGraphMixin {
	@Shadow(aliases = "f_59236195", remap = false) @Final private BitSet bitSet;
	@Shadow(aliases = "m_39542554", remap = false) private void addEdges(int pos, Set<EnumFacing> facings) {}
	@Shadow(aliases = "m_12069087", remap = false) private int getNeighborIndexAtFace(int pos, EnumFacing facing) { return 0; }

	@Unique private static final ThreadLocal<int[]> timelessFix$queue = ThreadLocal.withInitial(() -> new int[4096]);

	/**
	 * @author embeddedt
	 * @reason Avoid linked-list nodes and boxed integers during chunk visibility flood fills.
	 */
	@Overwrite(aliases = "m_22760464", remap = false)
	private Set<EnumFacing> floodFill(int start) {
		Set<EnumFacing> facings = EnumSet.noneOf(EnumFacing.class);
		int[] queue = timelessFix$queue.get();
		int read = 0;
		int write = 1;
		queue[0] = start;
		this.bitSet.set(start);

		while (read < write) {
			int pos = queue[read++];
			this.addEdges(pos, facings);

			for (EnumFacing facing : EnumFacing.values()) {
				int neighbor = this.getNeighborIndexAtFace(pos, facing);
				if (neighbor >= 0 && !this.bitSet.get(neighbor)) {
					this.bitSet.set(neighbor);
					queue[write++] = neighbor;
				}
			}
		}

		return facings;
	}
}
