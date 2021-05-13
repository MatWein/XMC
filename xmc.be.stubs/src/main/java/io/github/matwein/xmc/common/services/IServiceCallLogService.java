package io.github.matwein.xmc.common.services;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;

public interface IServiceCallLogService {
	QueryResults<DtoServiceCallLogOvderview> loadOverview(IAsyncMonitor monitor, PagingParams<ServiceCallLogOverviewFields> pagingParams);
}
