package dev.rdh.timelessfix;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimelessFix implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger();
	public static final TimelessFixConfig CONFIG = TimelessFixConfig.load();

	@Override
	public void onInitialize() {
		if (CONFIG.releaseCrashReserve) {
			Minecraft.memoryReserve = new byte[0];
		}

		if (FabricLoader.getInstance().isModLoaded("celeritas")) {
			try {
				Class.forName("dev.rdh.timelessfix.celeritas.CeleritasConfigIntegration")
					.getMethod("register").invoke(null);
			} catch (ReflectiveOperationException exception) {
				LOGGER.warn("Could not register TimelessFix options with Celeritas", exception);
			}
		}
	}
}
