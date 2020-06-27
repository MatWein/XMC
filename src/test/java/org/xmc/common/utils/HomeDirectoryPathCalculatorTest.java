package org.xmc.common.utils;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

import java.io.File;

class HomeDirectoryPathCalculatorTest extends JUnitTestBase {
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
        System.setProperty("xmc.home.type", "home");

        HomeDirectoryPathCalculator.initializeSystemProperties();

        String dir = System.getProperty("user.home");
        Assert.assertEquals(new File(dir, ".xmc"), new File(HomeDirectoryPathCalculator.calculateHomeDir()));
        Assert.assertEquals(new File(dir, ".xmc/.bootstrap"), new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()));
        Assert.assertEquals(new File(dir, ".xmc/database"), new File(HomeDirectoryPathCalculator.calculateDatabaseDir()));
        Assert.assertEquals(new File(dir, ".xmc/database/24c9e15e52afc47c225b757e7bee1f9d"), new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser("user1")));
        Assert.assertEquals(new File(dir, ".xmc/logs"), new File(HomeDirectoryPathCalculator.calculateLogDir()));
        Assert.assertEquals(new File(dir, ".xmc/logs/derby.log"), new File(HomeDirectoryPathCalculator.calculateDerbyLogFilePath()));
    }

    @Test
    void testInitializeSystemProperties_workingDir() {
        System.setProperty("xmc.home.type", "workingdir");

        HomeDirectoryPathCalculator.initializeSystemProperties();

        String dir = System.getProperty("user.dir");
        Assert.assertEquals(new File(dir), new File(HomeDirectoryPathCalculator.calculateHomeDir()));
        Assert.assertEquals(new File(dir, ".bootstrap"), new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()));
        Assert.assertEquals(new File(dir, "database"), new File(HomeDirectoryPathCalculator.calculateDatabaseDir()));
        Assert.assertEquals(new File(dir, "database/24c9e15e52afc47c225b757e7bee1f9d"), new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser("user1")));
        Assert.assertEquals(new File(dir, "logs"), new File(HomeDirectoryPathCalculator.calculateLogDir()));
        Assert.assertEquals(new File(dir, "logs/derby.log"), new File(HomeDirectoryPathCalculator.calculateDerbyLogFilePath()));
    }

    @Test
    void testInitializeSystemProperties_custom() {
        File tempDirectory = FileUtils.getTempDirectory();

        System.setProperty("xmc.home.type", "custom");
        System.setProperty("xmc.home.path", tempDirectory.getAbsolutePath());

        HomeDirectoryPathCalculator.initializeSystemProperties();

        Assert.assertEquals(tempDirectory, new File(HomeDirectoryPathCalculator.calculateHomeDir()));
        Assert.assertEquals(new File(tempDirectory, ".bootstrap"), new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()));
        Assert.assertEquals(new File(tempDirectory, "database"), new File(HomeDirectoryPathCalculator.calculateDatabaseDir()));
        Assert.assertEquals(new File(tempDirectory, "database/24c9e15e52afc47c225b757e7bee1f9d"), new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser("user1")));
        Assert.assertEquals(new File(tempDirectory, "logs"), new File(HomeDirectoryPathCalculator.calculateLogDir()));
        Assert.assertEquals(new File(tempDirectory, "logs/derby.log"), new File(HomeDirectoryPathCalculator.calculateDerbyLogFilePath()));
    }
}