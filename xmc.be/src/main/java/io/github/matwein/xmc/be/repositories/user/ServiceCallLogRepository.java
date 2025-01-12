package io.github.matwein.xmc.be.repositories.user;

import io.github.matwein.xmc.be.entities.user.ServiceCallLog;
import io.github.matwein.xmc.common.stubs.Order;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.QueryResults;
import io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOverview;
import io.github.matwein.xmc.common.stubs.protocol.ServiceCallLogOverviewFields;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

import static io.github.matwein.xmc.be.common.QueryUtil.fromPage;
import static io.github.matwein.xmc.be.common.QueryUtil.toPageable;

public interface ServiceCallLogRepository extends JpaRepository<ServiceCallLog, Long> {
	default int cleanupServiceCallLogs(@Param("maxServicecalllogLifetimeInDays") int maxServicecalllogLifetimeInDays) {
		return cleanupServiceCallLogs(LocalDateTime.now().minusDays(maxServicecalllogLifetimeInDays));
	}
	
    @Modifying
	@Query("""
        delete from ServiceCallLog scl
        where scl.creationDate < :maxCreationDate
    """)
    int cleanupServiceCallLogs(@Param("maxCreationDate") LocalDateTime maxCreationDate);
	
	default QueryResults<DtoServiceCallLogOverview> loadOverview(PagingParams<ServiceCallLogOverviewFields> pagingParams) {
		return fromPage(loadOverview$(
				toPageable(pagingParams, ServiceCallLogOverviewFields.CREATION_DATE, Order.DESC),
				StringUtils.defaultString(pagingParams.getFilter())));
	}
	
	@Query("""
		select new io.github.matwein.xmc.common.stubs.protocol.DtoServiceCallLogOverview(
			scl.creationDate, scl.serviceClass, scl.serviceMethod, scl.returnValue, scl.parameterValues, scl.error, scl.callDuration
		)
		from ServiceCallLog scl
		where (
			scl.serviceClass ilike '%' || :filter || '%'
			or scl.serviceMethod ilike '%' || :filter || '%'
			or scl.parameterValues ilike '%' || :filter || '%'
			or scl.returnValue ilike '%' || :filter || '%'
			or scl.error ilike '%' || :filter || '%'
		)
	""")
	Page<DtoServiceCallLogOverview> loadOverview$(Pageable pageable, @Param("filter") String filter);
}
