package io.github.matwein.xmc.be.repositories.user;

import com.google.common.collect.Sets;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.hibernate.HibernateQuery;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.user.ServiceCallLog;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOverview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static io.github.matwein.xmc.be.entities.user.QServiceCallLog.serviceCallLog;

class ServiceCallLogRepositoryTest extends IntegrationTest {
    @Autowired
    private ServiceCallLogRepository repository;

    @Test
    void testCleanupServiceCallLogs() {
        ServiceCallLog serviceCallLog1 = graphGenerator.createServiceCallLog();
        serviceCallLog1.setCreationDate(LocalDateTime.now().minus(2, ChronoUnit.MINUTES));
        session().saveOrUpdate(serviceCallLog1);

        ServiceCallLog serviceCallLog2 = graphGenerator.createServiceCallLog();
        serviceCallLog2.setCreationDate(LocalDateTime.now().minus(366, ChronoUnit.DAYS));
        session().saveOrUpdate(serviceCallLog2);

        ServiceCallLog serviceCallLog3 = graphGenerator.createServiceCallLog();
        serviceCallLog3.setCreationDate(LocalDateTime.now().minus(5, ChronoUnit.DAYS));
        session().saveOrUpdate(serviceCallLog3);

        flushAndClear();

        repository.cleanupServiceCallLogs(365);

        flushAndClear();

        Set<ServiceCallLog> serviceCallLogs = Sets.newHashSet(new HibernateQuery<>(session())
                .select(serviceCallLog)
                .from(serviceCallLog)
                .fetch());

        Assertions.assertEquals(Sets.newHashSet(serviceCallLog1, serviceCallLog3), serviceCallLogs);
    }
	
	@Test
	void testLoadOverview() {
		PagingParams<ServiceCallLogOverviewFields> pagingParams = new PagingParams<>();
		pagingParams.setLimit(2);
		pagingParams.setFilter("error");
		pagingParams.setOffset(0);
		pagingParams.setSortBy(ServiceCallLogOverviewFields.ERROR);
		pagingParams.setOrder(Order.DESC);
		
		ServiceCallLog serviceCallLog1 = graphGenerator.createServiceCallLog();
		serviceCallLog1.setError("error 1");
		session().saveOrUpdate(serviceCallLog1);
		
		ServiceCallLog serviceCallLog2 = graphGenerator.createServiceCallLog();
		serviceCallLog2.setError("error 2");
		session().saveOrUpdate(serviceCallLog2);
		
		ServiceCallLog serviceCallLog3 = graphGenerator.createServiceCallLog();
		serviceCallLog3.setError("error 3");
		session().saveOrUpdate(serviceCallLog3);
		
		flushAndClear();
		
		QueryResults<DtoServiceCallLogOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(3, result.getTotal());
		Assertions.assertEquals(2, result.getResults().size());
		Assertions.assertEquals("error 3", result.getResults().get(0).getError());
		Assertions.assertEquals("error 2", result.getResults().get(1).getError());
	}
	
	@Test
	void testLoadOverview_CheckAllSortFields() {
		graphGenerator.createServiceCallLog();
		graphGenerator.createServiceCallLog();
		graphGenerator.createServiceCallLog();
		
		flushAndClear();
		
		for (ServiceCallLogOverviewFields field : ServiceCallLogOverviewFields.values()) {
			QueryResults<DtoServiceCallLogOverview> result = repository.loadOverview(new PagingParams<>(0, 10, field, Order.ASC, null));
			
			Assertions.assertEquals(3, result.getTotal());
			Assertions.assertEquals(3, result.getResults().size());
		}
	}
	
	@Test
	void testLoadOverview_CheckFields() {
		PagingParams<ServiceCallLogOverviewFields> pagingParams = new PagingParams<>();
		
		ServiceCallLog serviceCallLog = graphGenerator.createServiceCallLog();
		serviceCallLog.setError("Error");
		serviceCallLog.setCallDuration(1000L);
		serviceCallLog.setParameterValues("ParameterValues");
		serviceCallLog.setReturnValue("ReturnValue");
		serviceCallLog.setServiceClass("ServiceClass");
		serviceCallLog.setServiceMethod("ServiceMethod");
		session().saveOrUpdate(serviceCallLog);
		
		flushAndClear();
		
		QueryResults<DtoServiceCallLogOverview> result = repository.loadOverview(pagingParams);
		
		Assertions.assertEquals(1, result.getTotal());
		Assertions.assertEquals(1, result.getResults().size());
		
		var dtoServiceCallLogOverview = result.getResults().get(0);
		Assertions.assertEquals(serviceCallLog.getError(), dtoServiceCallLogOverview.getError());
		Assertions.assertEquals(serviceCallLog.getCallDuration(), dtoServiceCallLogOverview.getCallDuration());
		Assertions.assertEquals(serviceCallLog.getCreationDate(), dtoServiceCallLogOverview.getCreationDate());
		Assertions.assertEquals(serviceCallLog.getParameterValues(), dtoServiceCallLogOverview.getParameterValues());
		Assertions.assertEquals(serviceCallLog.getReturnValue(), dtoServiceCallLogOverview.getReturnValue());
		Assertions.assertEquals(serviceCallLog.getServiceClass(), dtoServiceCallLogOverview.getServiceClass());
		Assertions.assertEquals(serviceCallLog.getServiceMethod(), dtoServiceCallLogOverview.getServiceMethod());
	}
}