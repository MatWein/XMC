package io.github.matwein.xmc.be.services;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.matwein.xmc.be.annotations.DisableServiceCallLogging;
import io.github.matwein.xmc.be.repositories.user.ServiceCallLogRepository;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

@Service
@Transactional
public class ServiceCallLogService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCallLogService.class);
	
	private final ServiceCallLogRepository serviceCallLogRepository;
	
	@Autowired
	public ServiceCallLogService(ServiceCallLogRepository serviceCallLogRepository) {
		this.serviceCallLogRepository = serviceCallLogRepository;
	}
	
	@DisableServiceCallLogging
	public QueryResults<DtoServiceCallLogOvderview> loadOverview(AsyncMonitor monitor, PagingParams<ServiceCallLogOverviewFields> pagingParams) {
		LOGGER.info("Loading service call logs overview with params: {}", pagingParams);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_SERVICECALLLOGS);
		
		return serviceCallLogRepository.loadOverview(pagingParams);
	}
}
