package dev.rdh.timelessfix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TimelessFixConfig {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("timelessfix.json");

	public boolean releaseCrashReserve;

	public static TimelessFixConfig load() {
		if (Files.isRegularFile(PATH)) {
			try (Reader reader = Files.newBufferedReader(PATH)) {
				TimelessFixConfig config = GSON.fromJson(reader, TimelessFixConfig.class);
				if (config != null) {
					return config;
				}
			} catch (IOException | JsonParseException exception) {
				LOGGER.warn("Could not read TimelessFix config", exception);
			}
		}

		TimelessFixConfig config = new TimelessFixConfig();
		config.save();
		return config;
	}

	public void save() {
		try {
			Files.createDirectories(PATH.getParent());
			try (Writer writer = Files.newBufferedWriter(PATH)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException exception) {
			LOGGER.warn("Could not save TimelessFix config", exception);
		}
	}
}
