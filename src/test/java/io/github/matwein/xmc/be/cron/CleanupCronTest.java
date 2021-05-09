package io.github.matwein.xmc.be.cron;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.be.repositories.user.ServiceCallLogRepository;
import io.github.matwein.xmc.config.properties.XmcProperties;

import static org.mockito.Mockito.*;

class CleanupCronTest extends JUnitTestBase {
    private CleanupCron cron;

    @Mock private XmcProperties xmcProperties;
    @Mock private ServiceCallLogRepository serviceCallLogRepository;

    @BeforeEach
    void setUp() {
        cron = new CleanupCron(xmcProperties, serviceCallLogRepository);
    }

    @Test
    void testInvalidateTokens() {
        int maxServicecalllogLifetimeInDays = 12;
        when(xmcProperties.getMaxServicecalllogLifetimeInDays()).thenReturn(maxServicecalllogLifetimeInDays);

        cron.invalidateTokens();

        verify(serviceCallLogRepository).cleanupServiceCallLogs(maxServicecalllogLifetimeInDays);
    }

    @Test
    void testInvalidateTokens_ServiceCallCleanupDisabled() {
        int maxServicecalllogLifetimeInDays = -1;
        when(xmcProperties.getMaxServicecalllogLifetimeInDays()).thenReturn(maxServicecalllogLifetimeInDays);

        cron.invalidateTokens();

        verify(serviceCallLogRepository, never()).cleanupServiceCallLogs(maxServicecalllogLifetimeInDays);
    }
}