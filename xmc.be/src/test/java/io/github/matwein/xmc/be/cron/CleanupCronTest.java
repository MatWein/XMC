package io.github.matwein.xmc.be.cron;

import io.github.matwein.xmc.be.config.properties.XmcProperties;
import io.github.matwein.xmc.be.repositories.user.ServiceCallLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CleanupCronTest {
    private CleanupCron cron;

    @Mock private XmcProperties xmcProperties;
    @Mock private ServiceCallLogRepository serviceCallLogRepository;

    @BeforeEach
    void setUp() {
	    MockitoAnnotations.openMocks(this);
	    
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