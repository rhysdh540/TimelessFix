package dev.rdh.timelessfix.mixin.allocation_rate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
abstract class WorldLightingMixin {
	@Unique private static final EnumFacing[] timelessFix$facings = EnumFacing.values();

	@Shadow public abstract boolean canSeeSky(BlockPos pos);
	@Shadow public abstract IBlockState getBlockState(BlockPos pos);
	@Shadow public abstract int getLightFor(EnumSkyBlock type, BlockPos pos);

	@Inject(method = "getRawLight", at = @At("HEAD"), cancellable = true)
	private void reuseNeighborPosition(BlockPos pos, EnumSkyBlock lightType, CallbackInfoReturnable<Integer> cir) {
		if (lightType == EnumSkyBlock.SKY && this.canSeeSky(pos)) {
			cir.setReturnValue(15);
			return;
		}

		Block block = this.getBlockState(pos).getBlock();
		int light = lightType == EnumSkyBlock.SKY ? 0 : block.getLightValue();
		int opacity = block.getLightOpacity();
		if (opacity >= 15 && block.getLightValue() > 0) {
			opacity = 1;
		}
		opacity = Math.max(1, opacity);

		if (opacity >= 15 || light >= 14) {
			cir.setReturnValue(opacity >= 15 ? 0 : light);
			return;
		}

		BlockPos.MutableBlockPos neighbor = new BlockPos.MutableBlockPos();
		for (EnumFacing facing : timelessFix$facings) {
			neighbor.set(
				pos.getX() + facing.getFrontOffsetX(),
				pos.getY() + facing.getFrontOffsetY(),
				pos.getZ() + facing.getFrontOffsetZ()
			);
			light = Math.max(light, this.getLightFor(lightType, neighbor) - opacity);
			if (light >= 14) {
				break;
			}
		}
		cir.setReturnValue(light);
	}
}
