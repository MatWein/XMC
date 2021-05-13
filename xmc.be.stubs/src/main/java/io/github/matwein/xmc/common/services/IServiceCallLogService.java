package io.github.matwein.xmc.common.services;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOverview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;

public interface IServiceCallLogService {
	QueryResults<DtoServiceCallLogOverview> loadOverview(IAsyncMonitor monitor, PagingParams<ServiceCallLogOverviewFields> pagingParams);
}
