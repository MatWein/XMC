package io.github.matwein.xmc.be.services;

import io.github.matwein.xmc.be.annotations.DisableServiceCallLogging;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.mapper.QueryResultsMapper;
import io.github.matwein.xmc.be.repositories.user.ServiceCallLogRepository;
import io.github.matwein.xmc.common.services.IServiceCallLogService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOverview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceCallLogService implements IServiceCallLogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCallLogService.class);
	
	private final ServiceCallLogRepository serviceCallLogRepository;
	private final QueryResultsMapper queryResultsMapper;
	
	@Autowired
	public ServiceCallLogService(
			ServiceCallLogRepository serviceCallLogRepository,
			QueryResultsMapper queryResultsMapper) {
		
		this.serviceCallLogRepository = serviceCallLogRepository;
		this.queryResultsMapper = queryResultsMapper;
	}
	
	@Override
	@DisableServiceCallLogging
	public QueryResults<DtoServiceCallLogOverview> loadOverview(IAsyncMonitor monitor, PagingParams<ServiceCallLogOverviewFields> pagingParams) {
		LOGGER.info("Loading service call logs overview with params: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_SERVICECALLLOGS));
		
		var results = serviceCallLogRepository.loadOverview(pagingParams);
		return queryResultsMapper.map(results);
	}
}
