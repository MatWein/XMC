package io.github.matwein.xmc.be.common.pagination;

import com.querydsl.core.types.Expression;
import io.github.matwein.xmc.be.entities.user.QServiceCallLog;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallLogOverviewFieldsMapper implements IPagingFieldMapper<ServiceCallLogOverviewFields> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCallLogOverviewFieldsMapper.class);
	
	@Override
	public Expression<?> map(ServiceCallLogOverviewFields pagingField) {
		switch (pagingField) {
			case CREATION_DATE:
				return QServiceCallLog.serviceCallLog.creationDate;
			case SERVICE_CLASS:
				return QServiceCallLog.serviceCallLog.serviceClass;
			case SERVICE_METHOD:
				return QServiceCallLog.serviceCallLog.serviceMethod;
			case RETURN_VALUE:
				return QServiceCallLog.serviceCallLog.returnValue;
			case PARAMETER_VALUES:
				return QServiceCallLog.serviceCallLog.parameterValues;
			case ERROR:
				return QServiceCallLog.serviceCallLog.error;
			case CALL_DURATION:
				return QServiceCallLog.serviceCallLog.callDuration;
			default:
				String message = String.format("Could not map enum value '%s' of type '%s' to expression.", pagingField, pagingField.getClass().getSimpleName());
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
