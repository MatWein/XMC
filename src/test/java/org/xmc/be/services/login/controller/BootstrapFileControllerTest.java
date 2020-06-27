package org.xmc.be.services.login.controller;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.common.stubs.login.DtoBootstrapFile;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.common.utils.HomeDirectoryPathCalculatorCleanupController;

import java.io.File;
import java.util.Optional;

class BootstrapFileControllerTest extends JUnitTestBase {
    @BeforeEach
    void setUp() {
        HomeDirectoryPathCalculatorCleanupController.clearSystemProperties();

        File tempDirectory = FileUtils.getTempDirectory();

        System.setProperty("xmc.home.type", "custom");
        System.setProperty("xmc.home.path", tempDirectory.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        HomeDirectoryPathCalculatorCleanupController.clearSystemProperties();
    }

    @Test
    void testReadAndWriteBootstrapFile() {
        Optional<DtoBootstrapFile> firstReadResult = BootstrapFileController.readBootstrapFile();
        Assert.assertEquals(Optional.empty(), firstReadResult);

        DtoBootstrapFile dto = enhancedRandom.nextObject(DtoBootstrapFile.class);
        dto.setSaveCredentials(true);
        new BootstrapFileController().writeBootstrapFile(dto);

        try {
            Optional<DtoBootstrapFile> secondReadResult = BootstrapFileController.readBootstrapFile();
            DtoBootstrapFile result = secondReadResult.get();

            Assert.assertEquals(dto.getUsername(), result.getUsername());
            Assert.assertEquals(dto.getPassword(), result.getPassword());
            Assert.assertEquals(dto.isSaveCredentials(), result.isSaveCredentials());
            Assert.assertEquals(dto.isAutoLogin(), result.isAutoLogin());
        } finally {
            new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()).delete();
        }
    }

    @Test
    void testReadAndWriteBootstrapFile_NoSaving() {
        Optional<DtoBootstrapFile> firstReadResult = BootstrapFileController.readBootstrapFile();
        Assert.assertEquals(Optional.empty(), firstReadResult);

        DtoBootstrapFile dto = enhancedRandom.nextObject(DtoBootstrapFile.class);
        dto.setSaveCredentials(false);
        new BootstrapFileController().writeBootstrapFile(dto);

        Optional<DtoBootstrapFile> secondReadResult = BootstrapFileController.readBootstrapFile();
        Assert.assertEquals(Optional.empty(), secondReadResult);
    }
}