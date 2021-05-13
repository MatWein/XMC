package io.github.matwein.xmc.be.config.interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ErrorLoggingInterceptor implements MethodInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorLoggingInterceptor.class);
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		} catch (Throwable e) {
			throw handleError(e);
		}
	}

	private Throwable handleError(Throwable e) {
		if (e instanceof RuntimeException) {
			LOGGER.error("Unexpected runtime exception was thrown.", e);
		} else {
			LOGGER.info("Returning checked exception: {}: {}", 
					e.getClass().getSimpleName(), 
					StringUtils.defaultIfBlank(e.getMessage(), "<no message>"));
		}
		
		return e;
	}
}
