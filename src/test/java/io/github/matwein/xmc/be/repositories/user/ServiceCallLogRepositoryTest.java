package io.github.matwein.xmc.be.repositories.user;

import com.google.common.collect.Sets;
import com.querydsl.jpa.hibernate.HibernateQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.IntegrationTest;
import io.github.matwein.xmc.be.entities.user.ServiceCallLog;

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
}