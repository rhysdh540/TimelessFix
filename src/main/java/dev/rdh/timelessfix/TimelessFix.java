package dev.rdh.timelessfix;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.Minecraft;

public class TimelessFix implements ModInitializer {
	public static final TimelessFixConfig CONFIG = TimelessFixConfig.load();

	@Override
	public void onInitialize() {
		if (CONFIG.releaseCrashReserve) {
			Minecraft.memoryReserve = new byte[0];
		}
	}
}
