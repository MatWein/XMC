package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.fe.SystemProperties;

public class HomeDirectoryPathCalculatorCleanupController {
    public static void clearSystemProperties() {
        HomeDirectoryPathCalculator.cachedHomeDir = null;

        System.clearProperty(SystemProperties.XMC_HOME_TYPE);
        System.clearProperty(SystemProperties.XMC_HOME_PATH);
        System.clearProperty(SystemProperties.SYSTEM_HOME_DIR);
        System.clearProperty(SystemProperties.SPRING_CONFIG_ADDITIONAL_LOCATION);
        System.clearProperty(SystemProperties.SPRING_PROFILES_ACTIVE);
        System.clearProperty(SystemProperties.SYSTEM_HOME_LOG_DIR);
        System.clearProperty(SystemProperties.DERBY_STREAM_ERROR_FILE);
    }
}
