package io.github.matwein.xmc.common.stubs.protocol;

import io.github.matwein.xmc.common.stubs.IPagingField;

public enum ServiceCallLogOverviewFields implements IPagingField {
	CREATION_DATE,
	SERVICE_CLASS,
	SERVICE_METHOD,
	RETURN_VALUE,
	PARAMETER_VALUES,
	ERROR,
	CALL_DURATION
}
