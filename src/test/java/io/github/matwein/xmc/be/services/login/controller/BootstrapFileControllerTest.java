package io.github.matwein.xmc.be.services.login.controller;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import io.github.matwein.xmc.common.utils.HomeDirectoryPathCalculator;
import io.github.matwein.xmc.common.utils.HomeDirectoryPathCalculatorCleanupController;

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
        Assertions.assertEquals(Optional.empty(), firstReadResult);

        DtoBootstrapFile dto = testObjectFactory.createRandom(DtoBootstrapFile.class);
        dto.setSaveCredentials(true);
        new BootstrapFileController().writeBootstrapFile(dto);

        try {
            Optional<DtoBootstrapFile> secondReadResult = BootstrapFileController.readBootstrapFile();
            DtoBootstrapFile result = secondReadResult.get();

            Assertions.assertEquals(dto.getUsername(), result.getUsername());
            Assertions.assertEquals(dto.getPassword(), result.getPassword());
            Assertions.assertEquals(dto.isSaveCredentials(), result.isSaveCredentials());
            Assertions.assertEquals(dto.isAutoLogin(), result.isAutoLogin());
        } finally {
            new File(HomeDirectoryPathCalculator.calculateCredentialFilePath()).delete();
        }
    }

    @Test
    void testReadAndWriteBootstrapFile_NoSaving() {
        Optional<DtoBootstrapFile> firstReadResult = BootstrapFileController.readBootstrapFile();
        Assertions.assertEquals(Optional.empty(), firstReadResult);

        DtoBootstrapFile dto = testObjectFactory.createRandom(DtoBootstrapFile.class);
        dto.setSaveCredentials(false);
        new BootstrapFileController().writeBootstrapFile(dto);

        Optional<DtoBootstrapFile> secondReadResult = BootstrapFileController.readBootstrapFile();
        Assertions.assertEquals(Optional.empty(), secondReadResult);
    }
}