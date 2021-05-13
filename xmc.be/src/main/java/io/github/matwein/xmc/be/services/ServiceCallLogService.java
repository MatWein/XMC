package io.github.matwein.xmc.be.services;

import com.querydsl.core.QueryResults;
import io.github.matwein.xmc.be.annotations.DisableServiceCallLogging;
import io.github.matwein.xmc.be.repositories.user.ServiceCallLogRepository;
import io.github.matwein.xmc.common.services.IServiceCallLogService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
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
	
	@Autowired
	public ServiceCallLogService(ServiceCallLogRepository serviceCallLogRepository) {
		this.serviceCallLogRepository = serviceCallLogRepository;
	}
	
	@Override
	@DisableServiceCallLogging
	public QueryResults<DtoServiceCallLogOvderview> loadOverview(IAsyncMonitor monitor, PagingParams<ServiceCallLogOverviewFields> pagingParams) {
		LOGGER.info("Loading service call logs overview with params: {}", pagingParams);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_SERVICECALLLOGS));
		
		return serviceCallLogRepository.loadOverview(pagingParams);
	}
}
