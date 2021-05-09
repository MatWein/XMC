package io.github.matwein.xmc.config.interceptors;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.annotations.DisableServiceCallLogging;
import io.github.matwein.xmc.be.entities.user.ServiceCallLog;
import io.github.matwein.xmc.be.repositories.user.ServiceCallLogJpaRepository;
import io.github.matwein.xmc.fe.async.AsyncMonitor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServiceCallLogInterceptor implements MethodInterceptor {
	private final Gson gson;
	private final ServiceCallLogJpaRepository serviceCallLogJpaRepository;

	@Autowired
	public ServiceCallLogInterceptor(Gson gson, ServiceCallLogJpaRepository serviceCallLogJpaRepository) {
		this.gson = gson;
		this.serviceCallLogJpaRepository = serviceCallLogJpaRepository;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		var method = invocation.getMethod();
		
		if (method.getAnnotation(DisableServiceCallLogging.class) != null) {
			return invocation.proceed();
		}
		
		List<Object> arguments = Arrays.stream(invocation.getArguments())
				.filter(arg -> !(arg instanceof AsyncMonitor))
				.collect(Collectors.toList());

		String serviceClass = StringUtils.abbreviate(method.getDeclaringClass().getName(), ServiceCallLog.SMALL_LENGTH);
		String qualifiedNameOfMethod = StringUtils.abbreviate(method.getName(), ServiceCallLog.SMALL_LENGTH);
		String parameterValues = StringUtils.abbreviate(gson.toJson(arguments), ServiceCallLog.DEFAULT_LENGTH);

		var serviceCallLog = new ServiceCallLog();
		serviceCallLog.setServiceClass(serviceClass);
		serviceCallLog.setServiceMethod(qualifiedNameOfMethod);
		serviceCallLog.setParameterValues(parameterValues);
		
		long start = System.currentTimeMillis();
		
		try {
			Object result = invocation.proceed();
			
			if (result != null) {
				String returnValueAsString = StringUtils.abbreviate(gson.toJson(result), ServiceCallLog.DEFAULT_LENGTH);
				serviceCallLog.setReturnValue(returnValueAsString);
			}
			
			return result;
		} catch (Throwable e) {
			serviceCallLog.setError(StringUtils.abbreviate(Throwables.getStackTraceAsString(e), ServiceCallLog.ERROR_LENGTH));
			
			throw e;
		} finally {
			long end = System.currentTimeMillis();
			long duration = end - start;
			
			serviceCallLog.setCallDuration(duration);
			
			serviceCallLogJpaRepository.save(serviceCallLog);
		}
	}
}
