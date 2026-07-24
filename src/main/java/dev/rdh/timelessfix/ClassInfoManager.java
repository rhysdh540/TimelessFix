package dev.rdh.timelessfix;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.logging.LoggerAdapterDefault;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

public final class ClassInfoManager {
	private static final Logger LOGGER = LogManager.getLogger();
	private static boolean cleared;

	private ClassInfoManager() {
	}

	@SuppressWarnings("unchecked")
	public static void clear() {
		if (cleared) {
			return;
		}
		cleared = true;

		try {
			Field cacheField = accessible(ClassInfo.class.getDeclaredField("cache"));
			Field mixinField = accessible(ClassInfo.class.getDeclaredField("mixin"));
			Class<?> mixinInfoClass = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo");
			Field stateField = accessible(mixinInfoClass.getDeclaredField("state"));
			Class<?> stateClass = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo$State");
			Field classNodeField = accessible(stateClass.getDeclaredField("classNode"));

			Class<?> mixinLoggerClass = Class.forName("net.fabricmc.loader.impl.launch.knot.MixinLogger");
			Field loggerMapField = accessible(mixinLoggerClass.getDeclaredField("LOGGER_MAP"));
			Map<String, ILogger> loggers = (Map<String, ILogger>) loggerMapField.get(null);
			ILogger auditLogger = loggers.put("mixin.audit", new LoggerAdapterDefault("mixin.audit"));
			try {
				MixinEnvironment.getDefaultEnvironment().audit();
			} finally {
				if (auditLogger == null) {
					loggers.remove("mixin.audit");
				} else {
					loggers.put("mixin.audit", auditLogger);
				}
			}

			Map<String, ClassInfo> cache = (Map<String, ClassInfo>) cacheField.get(null);
			ClassNode emptyNode = new ClassNode();
			for (ClassInfo info : cache.values()) {
				if (info != null && info.isMixin()) {
					Object state = stateField.get(mixinField.get(info));
					if (state != null) {
						classNodeField.set(state, emptyNode);
					}
				}
			}
			cache.keySet().removeIf(name -> !"java/lang/Object".equals(name));
			LOGGER.info("Cleared Mixin class metadata");
		} catch (ReflectiveOperationException | RuntimeException exception) {
			LOGGER.warn("Could not clear Mixin class metadata", exception);
		}
	}

	private static Field accessible(Field field) {
		field.setAccessible(true);
		return field;
	}
}
