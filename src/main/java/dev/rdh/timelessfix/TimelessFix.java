package dev.rdh.timelessfix;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.Minecraft;

public class TimelessFix implements ModInitializer {
	@Override
	public void onInitialize() {
		Minecraft.getMinecraft();
	}
}
