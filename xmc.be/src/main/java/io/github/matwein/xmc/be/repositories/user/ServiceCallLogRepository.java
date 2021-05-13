package io.github.matwein.xmc.be.repositories.user;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOvderview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.github.matwein.xmc.be.entities.user.QServiceCallLog.serviceCallLog;

@Repository
public class ServiceCallLogRepository {
    private final QueryUtil queryUtil;

    @Autowired
    public ServiceCallLogRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
    }

    public long cleanupServiceCallLogs(int maxServicecalllogLifetimeInDays) {
        return queryUtil.createDeleteClause(serviceCallLog)
                .where(serviceCallLog.creationDate.before(LocalDateTime.now().minus(maxServicecalllogLifetimeInDays, ChronoUnit.DAYS)))
                .execute();
    }
	
	public QueryResults<DtoServiceCallLogOvderview> loadOverview(PagingParams<ServiceCallLogOverviewFields> pagingParams) {
		String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";
		BooleanExpression predicate = serviceCallLog.serviceClass.likeIgnoreCase(filter)
				.or(serviceCallLog.serviceMethod.likeIgnoreCase(filter))
				.or(serviceCallLog.parameterValues.likeIgnoreCase(filter))
				.or(serviceCallLog.returnValue.likeIgnoreCase(filter))
				.or(serviceCallLog.error.likeIgnoreCase(filter));
		
		return queryUtil.createPagedQuery(pagingParams, ServiceCallLogOverviewFields.CREATION_DATE, Order.DESC)
				.select(Projections.constructor(DtoServiceCallLogOvderview.class,
						serviceCallLog.creationDate, serviceCallLog.serviceClass, serviceCallLog.serviceMethod,
						serviceCallLog.returnValue, serviceCallLog.parameterValues, serviceCallLog.error,
						serviceCallLog.callDuration))
				.from(serviceCallLog)
				.where(predicate)
				.fetchResults();
	}
}
