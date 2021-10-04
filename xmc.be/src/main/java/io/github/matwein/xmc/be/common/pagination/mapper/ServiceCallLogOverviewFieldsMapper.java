package io.github.matwein.xmc.be.common.pagination.mapper;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.be.entities.user.QServiceCallLog;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallLogOverviewFieldsMapper implements IPagingFieldMapper<ServiceCallLogOverviewFields> {
	@Override
	public Expression<?> map(ServiceCallLogOverviewFields pagingField) {
		return switch (pagingField) {
			case CREATION_DATE -> QServiceCallLog.serviceCallLog.creationDate;
			case SERVICE_CLASS -> QServiceCallLog.serviceCallLog.serviceClass;
			case SERVICE_METHOD -> QServiceCallLog.serviceCallLog.serviceMethod;
			case RETURN_VALUE -> QServiceCallLog.serviceCallLog.returnValue;
			case PARAMETER_VALUES -> QServiceCallLog.serviceCallLog.parameterValues;
			case ERROR -> QServiceCallLog.serviceCallLog.error;
			case CALL_DURATION -> QServiceCallLog.serviceCallLog.callDuration;
		};
	}
}
