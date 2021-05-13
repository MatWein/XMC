package io.github.matwein.xmc.fe.common;

import javafx.application.HostServices;
import javafx.util.Callback;

public class XmcFrontendContext {
	public static HostServices hostServices;
	public static IApplicationContext applicationContext;
	
	public static Callback<Class<?>, Object> createNewInstanceFactory() {
		return type -> {
			if (applicationContext == null || applicationContext.get() == null) {
				return ReflectionUtil.createNewInstance(type);
			} else {
				return applicationContext.get().getBean(type);
			}
		};
	}
}
