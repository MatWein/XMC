package io.github.matwein.xmc.fe.common;

import org.springframework.context.ConfigurableApplicationContext;

public interface IApplicationContext {
	ConfigurableApplicationContext get();
	
	default String[] getArgs() { return new String[] {}; }
	
	default void start() {}
	default void destroy() {}
}
