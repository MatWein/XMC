package org.xmc.be.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.repositories.user.ServiceCallLogRepository;
import org.xmc.config.properties.XmcProperties;

@Component
public class CleanupCron {
    private static final Logger LOGGER = LoggerFactory.getLogger(CleanupCron.class);

    private static final int DELAY_IN_MILLIS = 5 * 60 * 60 * 1000; // every 5 hours
    private static final int INITIAL_DELAY = 5 * 60 * 1000; // wait 5 minutes before run first time
    private static final int DISABLED = -1;

    private final XmcProperties xmcProperties;
    private final ServiceCallLogRepository serviceCallLogRepository;

    @Autowired
    public CleanupCron(XmcProperties xmcProperties, ServiceCallLogRepository serviceCallLogRepository) {
        this.xmcProperties = xmcProperties;
        this.serviceCallLogRepository = serviceCallLogRepository;
    }

    @Transactional
    @Scheduled(fixedDelay = DELAY_IN_MILLIS, initialDelay = INITIAL_DELAY)
    public void invalidateTokens() {
        LOGGER.info("Starting: {}.", getClass().getSimpleName());

        int maxServicecalllogLifetimeInDays = xmcProperties.getMaxServicecalllogLifetimeInDays();
        if (maxServicecalllogLifetimeInDays != DISABLED) {
            LOGGER.info("Cleaning up service call logs...");
            serviceCallLogRepository.cleanupServiceCallLogs(maxServicecalllogLifetimeInDays);
            LOGGER.info("Finished.");
        }

        LOGGER.info("Finished.");
    }
}
