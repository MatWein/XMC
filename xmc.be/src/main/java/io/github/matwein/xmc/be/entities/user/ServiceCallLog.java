package io.github.matwein.xmc.be.entities.user;

import io.github.matwein.xmc.be.entities.PersistentObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;


@Entity(name = ServiceCallLog.TABLE_NAME)
public class ServiceCallLog extends PersistentObject {
	public static final String TABLE_NAME = "SERVICE_CALL_LOGS";
	
	public static final int SMALL_LENGTH = 255;
	public static final int DEFAULT_LENGTH = 1000;
	public static final int ERROR_LENGTH = 10000;

	@Column(name = "SERVICE_CLASS", nullable = false, length = SMALL_LENGTH)
	private String serviceClass;

	@Column(name = "SERVICE_METHOD", nullable = false, length = SMALL_LENGTH)
	private String serviceMethod;

	@Column(name = "RETURN_VALUE", nullable = true, length = DEFAULT_LENGTH)
	private String returnValue;

	@Column(name = "PARAMETER_VALUES", nullable = true, length = DEFAULT_LENGTH)
	private String parameterValues;

	@Column(name = "ERROR", nullable = true, length = ERROR_LENGTH)
	private String error;

	@Column(name = "CALL_DURATION", nullable = false)
	private long callDuration;

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceMethod() {
		return serviceMethod;
	}

	public void setServiceMethod(String serviceMethod) {
		this.serviceMethod = serviceMethod;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(String parameterValues) {
		this.parameterValues = parameterValues;
	}

	public long getCallDuration() {
		return callDuration;
	}

	public void setCallDuration(long callDuration) {
		this.callDuration = callDuration;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
