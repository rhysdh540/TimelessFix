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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
abstract class WorldLightingMixin {
	@Unique private static final EnumFacing[] timelessFix$facings = EnumFacing.values();
	@Unique private final BlockPos.MutableBlockPos timelessFix$neighborPosition = new BlockPos.MutableBlockPos();
	@Unique private final BlockPos.MutableBlockPos timelessFix$rawLightPosition = new BlockPos.MutableBlockPos();

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

		BlockPos.MutableBlockPos neighbor = this.timelessFix$rawLightPosition;
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

	@Redirect(
		method = {"getLight(Lnet/minecraft/util/BlockPos;Z)I", "getLightFromNeighborsFor"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockPos;up()Lnet/minecraft/util/BlockPos;")
	)
	private BlockPos reuseUpPosition(BlockPos pos) {
		return offset(pos, EnumFacing.UP);
	}

	@Redirect(
		method = {"getLight(Lnet/minecraft/util/BlockPos;Z)I", "getLightFromNeighborsFor"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockPos;east()Lnet/minecraft/util/BlockPos;")
	)
	private BlockPos reuseEastPosition(BlockPos pos) {
		return offset(pos, EnumFacing.EAST);
	}

	@Redirect(
		method = {"getLight(Lnet/minecraft/util/BlockPos;Z)I", "getLightFromNeighborsFor"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockPos;west()Lnet/minecraft/util/BlockPos;")
	)
	private BlockPos reuseWestPosition(BlockPos pos) {
		return offset(pos, EnumFacing.WEST);
	}

	@Redirect(
		method = {"getLight(Lnet/minecraft/util/BlockPos;Z)I", "getLightFromNeighborsFor"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockPos;south()Lnet/minecraft/util/BlockPos;")
	)
	private BlockPos reuseSouthPosition(BlockPos pos) {
		return offset(pos, EnumFacing.SOUTH);
	}

	@Redirect(
		method = {"getLight(Lnet/minecraft/util/BlockPos;Z)I", "getLightFromNeighborsFor"},
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/BlockPos;north()Lnet/minecraft/util/BlockPos;")
	)
	private BlockPos reuseNorthPosition(BlockPos pos) {
		return offset(pos, EnumFacing.NORTH);
	}

	@Unique
	private BlockPos offset(BlockPos pos, EnumFacing facing) {
		return this.timelessFix$neighborPosition.set(
			pos.getX() + facing.getFrontOffsetX(),
			pos.getY() + facing.getFrontOffsetY(),
			pos.getZ() + facing.getFrontOffsetZ()
		);
	}
}
