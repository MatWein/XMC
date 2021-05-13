package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.XmcApplication;
import javafx.application.HostServices;
import javafx.util.Callback;

public class XmcContext {
	public static HostServices hostServices;
	
	public static Callback<Class<?>, Object> createNewInstanceFactory() {
		return type -> {
			if (XmcApplication.applicationContext == null) {
				return ReflectionUtil.createNewInstance(type);
			} else {
				return XmcApplication.applicationContext.getBean(type);
			}
		};
	}
}
