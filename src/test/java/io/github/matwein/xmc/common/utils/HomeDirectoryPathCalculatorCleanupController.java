package io.github.matwein.xmc.common.utils;

public class HomeDirectoryPathCalculatorCleanupController {
    public static void clearSystemProperties() {
        HomeDirectoryPathCalculator.cachedHomeDir = null;

        System.clearProperty("xmc.home.type");
        System.clearProperty("xmc.home.path");
        System.clearProperty("system.home.dir");
        System.clearProperty("spring.config.additional-location");
        System.clearProperty("spring.profiles.active");
        System.clearProperty("system.home.log.dir");
        System.clearProperty("derby.stream.error.file");
    }
}
