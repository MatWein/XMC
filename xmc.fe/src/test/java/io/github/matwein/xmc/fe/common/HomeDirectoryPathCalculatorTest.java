package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.fe.SystemProperties;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

class HomeDirectoryPathCalculatorTest {
    @BeforeEach
    void setUp() {
        HomeDirectoryPathCalculatorCleanupController.clearSystemProperties();
    }

    @AfterEach
    void tearDown() {
        HomeDirectoryPathCalculatorCleanupController.clearSystemProperties();
    }

    @Test
    void testInitializeSystemProperties() {
        System.setProperty(SystemProperties.XMC_HOME_TYPE, SystemProperties.HOME_HOME);

        HomeDirectoryPathCalculator.initializeSystemProperties();

        String dir = System.getProperty(SystemProperties.USER_HOME);
        Assertions.assertEquals(new File(dir, ".xmc"), new File(HomeDirectoryPathCalculator.calculateHomeDir()));
        Assertions.assertEquals(new File(dir, ".xmc/.bootstrap"), new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()));
        Assertions.assertEquals(new File(dir, ".xmc/database"), new File(HomeDirectoryPathCalculator.calculateDatabaseDir()));
        Assertions.assertEquals(new File(dir, ".xmc/database/24c9e15e52afc47c225b757e7bee1f9d"), new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser("user1")));
        Assertions.assertEquals(new File(dir, ".xmc/logs"), new File(HomeDirectoryPathCalculator.calculateLogDir()));
        Assertions.assertEquals(new File(dir, ".xmc/logs/derby.log"), new File(HomeDirectoryPathCalculator.calculateDerbyLogFilePath()));
    }

    @Test
    void testInitializeSystemProperties_workingDir() {
        System.setProperty(SystemProperties.XMC_HOME_TYPE, SystemProperties.HOME_WORKINGDIR);

        HomeDirectoryPathCalculator.initializeSystemProperties();

        String dir = System.getProperty(SystemProperties.USER_DIR);
        Assertions.assertEquals(new File(dir), new File(HomeDirectoryPathCalculator.calculateHomeDir()));
        Assertions.assertEquals(new File(dir, ".bootstrap"), new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()));
        Assertions.assertEquals(new File(dir, "database"), new File(HomeDirectoryPathCalculator.calculateDatabaseDir()));
        Assertions.assertEquals(new File(dir, "database/24c9e15e52afc47c225b757e7bee1f9d"), new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser("user1")));
        Assertions.assertEquals(new File(dir, "logs"), new File(HomeDirectoryPathCalculator.calculateLogDir()));
        Assertions.assertEquals(new File(dir, "logs/derby.log"), new File(HomeDirectoryPathCalculator.calculateDerbyLogFilePath()));
    }

    @Test
    void testInitializeSystemProperties_custom() {
        File tempDirectory = FileUtils.getTempDirectory();

        System.setProperty(SystemProperties.XMC_HOME_TYPE, SystemProperties.HOME_CUSTOM);
        System.setProperty(SystemProperties.XMC_HOME_PATH, tempDirectory.getAbsolutePath());

        HomeDirectoryPathCalculator.initializeSystemProperties();

        Assertions.assertEquals(tempDirectory, new File(HomeDirectoryPathCalculator.calculateHomeDir()));
        Assertions.assertEquals(new File(tempDirectory, ".bootstrap"), new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()));
        Assertions.assertEquals(new File(tempDirectory, "database"), new File(HomeDirectoryPathCalculator.calculateDatabaseDir()));
        Assertions.assertEquals(new File(tempDirectory, "database/24c9e15e52afc47c225b757e7bee1f9d"), new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser("user1")));
        Assertions.assertEquals(new File(tempDirectory, "logs"), new File(HomeDirectoryPathCalculator.calculateLogDir()));
        Assertions.assertEquals(new File(tempDirectory, "logs/derby.log"), new File(HomeDirectoryPathCalculator.calculateDerbyLogFilePath()));
    }
}