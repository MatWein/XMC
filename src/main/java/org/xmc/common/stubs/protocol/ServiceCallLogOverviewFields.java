package org.xmc.common.stubs.protocol;

import com.querydsl.core.types.Expression;
import org.xmc.common.stubs.IPagingField;

import static org.xmc.be.entities.user.QServiceCallLog.serviceCallLog;

public enum ServiceCallLogOverviewFields implements IPagingField {
	CREATION_DATE(serviceCallLog.creationDate),
	SERVICE_CLASS(serviceCallLog.serviceClass),
	SERVICE_METHOD(serviceCallLog.serviceMethod),
	RETURN_VALUE(serviceCallLog.returnValue),
	PARAMETER_VALUES(serviceCallLog.parameterValues),
	ERROR(serviceCallLog.error),
	CALL_DURATION(serviceCallLog.callDuration)
	;
	
	private final Expression<?> expression;
	
	ServiceCallLogOverviewFields(Expression<?> expression) {
		this.expression = expression;
	}
	
	@Override
	public Expression<?> getExpression() {
		return expression;
	}
}
