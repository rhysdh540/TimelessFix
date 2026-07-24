package dev.rdh.timelessfix;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import dev.rdh.timelessfix.mixin.textures.AbstractClientPlayerAccessor;
import dev.rdh.timelessfix.mixin.textures.NetworkPlayerInfoAccessor;

public final class SkinTextureCleaner {
	private SkinTextureCleaner() {
	}

	public static void releaseIfUnused(NetworkPlayerInfo removed) {
		NetworkPlayerInfoAccessor textures = (NetworkPlayerInfoAccessor) removed;
		releaseIfUnused(textures.timelessfix$getCachedSkin());
		releaseIfUnused(textures.timelessfix$getCachedCape());
	}

	private static void releaseIfUnused(ResourceLocation location) {
		if (location == null || isUsed(location)) {
			return;
		}
		((TextureManagerExtension) Minecraft.getMinecraft().getTextureManager()).timelessfix$releaseTexture(location);
	}

	private static boolean isUsed(ResourceLocation location) {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (minecraft.getNetHandler() != null) {
			for (NetworkPlayerInfo info : minecraft.getNetHandler().getPlayerInfoMap()) {
				if (uses(info, location)) {
					return true;
				}
			}
		}
		if (minecraft.theWorld != null) {
			for (EntityPlayer player : minecraft.theWorld.playerEntities) {
				if (player instanceof AbstractClientPlayer) {
					NetworkPlayerInfo info = ((AbstractClientPlayerAccessor) player).timelessfix$getCachedPlayerInfo();
					if (info != null && uses(info, location)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean uses(NetworkPlayerInfo info, ResourceLocation location) {
		NetworkPlayerInfoAccessor textures = (NetworkPlayerInfoAccessor) info;
		return Objects.equals(textures.timelessfix$getCachedSkin(), location)
			|| Objects.equals(textures.timelessfix$getCachedCape(), location);
	}
}
