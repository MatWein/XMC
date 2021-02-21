package org.xmc.be.services;

import com.querydsl.core.QueryResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.annotations.DisableServiceCallLogging;
import org.xmc.be.repositories.user.ServiceCallLogRepository;
import org.xmc.common.stubs.PagingParams;
import org.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import org.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

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
