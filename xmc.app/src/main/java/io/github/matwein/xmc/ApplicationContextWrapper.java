package io.github.matwein.xmc;

import io.github.matwein.xmc.fe.common.IApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationContextWrapper implements IApplicationContext {
	@Override
	public ConfigurableApplicationContext get() {
		return XmcApplication.applicationContext;
	}
	
	@Override
	public String[] getArgs() {
		return XmcApplication.args;
	}
	
	@Override
	public void start() {
		XmcApplication.start();
	}
	
	@Override
	public void destroy() {
		XmcApplication.destroy();
	}
}
