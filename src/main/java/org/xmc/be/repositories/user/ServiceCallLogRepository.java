package org.xmc.be.repositories.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xmc.be.common.QueryUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.xmc.be.entities.user.QServiceCallLog.serviceCallLog;

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
}
