package org.xmc.common.stubs.protocol;

import com.querydsl.core.annotations.QueryProjection;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DtoServiceCallLogOvderview implements Serializable {
	private LocalDateTime creationDate;
	private String serviceClass;
	private String serviceMethod;
	private String returnValue;
	private String parameterValues;
	private String error;
	private long callDuration;
	
	public DtoServiceCallLogOvderview() {
	}

	@QueryProjection
	public DtoServiceCallLogOvderview(
			LocalDateTime creationDate,
			String serviceClass,
			String serviceMethod,
			String returnValue,
			String parameterValues,
			String error,
			long callDuration) {
		
		this.creationDate = creationDate;
		this.serviceClass = serviceClass;
		this.serviceMethod = serviceMethod;
		this.returnValue = returnValue;
		this.parameterValues = parameterValues;
		this.error = error;
		this.callDuration = callDuration;
	}
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
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
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
	
	public long getCallDuration() {
		return callDuration;
	}
	
	public void setCallDuration(long callDuration) {
		this.callDuration = callDuration;
	}
}
