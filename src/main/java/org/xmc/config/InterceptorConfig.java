package org.xmc.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.xmc.be.annotations.DisableServiceCallLogging;
import org.xmc.config.interceptors.ErrorLoggingInterceptor;
import org.xmc.config.interceptors.ServiceCallLogInterceptor;

@Configuration
public class InterceptorConfig {
	private static final String AND = " && ";
	private static final String POINTCUT_ANY_PUBLIC_METHOD = "execution(public * *(..))";
	private static final String POINTCUT_WITHIN_SERVICE = "@within(" + Service.class.getName() + ")";
	private static final String POINTCUT_EXCLUDE_DB_LOGGING_DISABLED = "!(@annotation(" + DisableServiceCallLogging.class.getName() + "))";

	private static final String POINTCUT_SERVICECALLLOGS = POINTCUT_ANY_PUBLIC_METHOD
			+ AND + POINTCUT_WITHIN_SERVICE
			+ AND + POINTCUT_EXCLUDE_DB_LOGGING_DISABLED;

	private static final String POINTCUT_ERROR_LOGGING = POINTCUT_ANY_PUBLIC_METHOD + AND + POINTCUT_WITHIN_SERVICE;

	@Bean
	public Advisor serviceCallLogInterceptorAdvisor(ServiceCallLogInterceptor serviceCallLogInterceptor) {
		var pointcut = new AspectJExpressionPointcut();
	    pointcut.setExpression(POINTCUT_SERVICECALLLOGS);
	    return new DefaultPointcutAdvisor(pointcut, serviceCallLogInterceptor);
	}
	
	@Bean
	public Advisor errorLoggingInterceptorAdvisor(ErrorLoggingInterceptor errorLoggingInterceptor) {
		var pointcut = new AspectJExpressionPointcut();
	    pointcut.setExpression(POINTCUT_ERROR_LOGGING);
	    return new DefaultPointcutAdvisor(pointcut, errorLoggingInterceptor);
	}
}
