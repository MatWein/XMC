package io.github.matwein.xmc.be.common.pagination.mapper;

import io.github.matwein.xmc.be.common.pagination.IPagingFieldMapper;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.springframework.stereotype.Component;

@Component
public class ServiceCallLogOverviewFieldsMapper implements IPagingFieldMapper<ServiceCallLogOverviewFields> {
	@Override
	public String map(ServiceCallLogOverviewFields pagingField) {
		return switch (pagingField) {
			case CREATION_DATE -> "scl.creationDate";
			case SERVICE_CLASS -> "scl.serviceClass";
			case SERVICE_METHOD -> "scl.serviceMethod";
			case RETURN_VALUE -> "scl.returnValue";
			case PARAMETER_VALUES -> "scl.parameterValues";
			case ERROR -> "scl.error";
			case CALL_DURATION -> "scl.callDuration";
		};
	}
}
