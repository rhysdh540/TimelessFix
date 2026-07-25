package dev.rdh.timelessfix.celeritas;

import dev.rdh.timelessfix.TimelessFix;
import dev.rdh.timelessfix.TimelessFixConfig;
import java.util.List;
import org.embeddedt.embeddium.impl.gui.framework.TextComponent;
import org.taumc.celeritas.api.OptionGUIConstructionEvent;
import org.taumc.celeritas.api.options.OptionIdentifier;
import org.taumc.celeritas.api.options.control.TickBoxControl;
import org.taumc.celeritas.api.options.structure.OptionFlag;
import org.taumc.celeritas.api.options.structure.OptionGroup;
import org.taumc.celeritas.api.options.structure.OptionImpact;
import org.taumc.celeritas.api.options.structure.OptionImpl;
import org.taumc.celeritas.api.options.structure.OptionPage;
import org.taumc.celeritas.api.options.structure.OptionStorage;

public final class CeleritasConfigIntegration implements OptionStorage<TimelessFixConfig> {
	private static final CeleritasConfigIntegration INSTANCE = new CeleritasConfigIntegration();

	@Override
	public TimelessFixConfig getData() {
		return TimelessFix.CONFIG;
	}

	@Override
	public void save() {
		TimelessFix.CONFIG.save();
	}

	private CeleritasConfigIntegration() {
	}

	public static void register() {
		OptionGUIConstructionEvent.BUS.addListener(event -> event.addPage(createPage()));
	}

	private static OptionPage createPage() {
		OptionGroup memory = OptionGroup.createBuilder()
			.setId(id("memory"))
			.add(OptionImpl.createBuilder(boolean.class, INSTANCE)
				.setId(id("release_crash_reserve"))
				.setControl(TickBoxControl::new)
				.setBinding((config, value) -> config.releaseCrashReserve = value,
					config -> config.releaseCrashReserve)
				.setImpact(OptionImpact.HIGH)
				.setFlags(OptionFlag.REQUIRES_GAME_RESTART)
				.build())
			.build();

		return new OptionPage(id("options"), text("pages.timelessfix"), List.of(memory));
	}

	private static <T> OptionIdentifier<T> id(String path) {
		return OptionIdentifier.create("timelessfix", path).cast();
	}

	private static TextComponent text(String path) {
		return TextComponent.translatable("timelessfix.options." + path);
	}
}
