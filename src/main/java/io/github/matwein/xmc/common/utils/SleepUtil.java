package io.github.matwein.xmc.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SleepUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SleepUtil.class);

    public static void sleep(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (Throwable e) {
            String message = String.format("Error on trying to sleep for %s milliseconds.", milliSeconds);
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
