package org.xmc.be.services.login.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.login.DtoBootstrapFile;
import org.xmc.common.utils.Crypter;
import org.xmc.common.utils.HomeDirectoryPathCalculator;

import java.io.File;
import java.util.Optional;

@Component
public class BootstrapFileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapFileController.class);

    public void writeBootstrapFile(DtoBootstrapFile dtoBootstrapFile) {
        try {
            File credentialFile = new File(HomeDirectoryPathCalculator.calculateCredentialFilePath());
            if (dtoBootstrapFile.isSaveCredentials()) {
                Crypter crypter = new Crypter();
                DtoBootstrapFile encryptedDto = new DtoBootstrapFile(
                        crypter.encrypt(dtoBootstrapFile.getUsername()),
                        crypter.encrypt(dtoBootstrapFile.getPassword()),
                        dtoBootstrapFile.isSaveCredentials(),
                        dtoBootstrapFile.isAutoLogin());
                byte[] bytes = crypter.encrypt(SerializationUtils.serialize(encryptedDto));
                FileUtils.writeByteArrayToFile(credentialFile, bytes);
            } else {
                credentialFile.delete();
            }
        } catch (Throwable e) {
            LOGGER.error("Error on writing bootstrap file.", e);
        }
    }

    public static Optional<DtoBootstrapFile> readBootstrapFile() {
        try {
            File credentialFile = new File(HomeDirectoryPathCalculator.calculateCredentialFilePath());
            if (credentialFile.isFile()) {
                Crypter crypter = new Crypter();
                byte[] bytes = crypter.decrypt(FileUtils.readFileToByteArray(credentialFile));
                DtoBootstrapFile encryptedDto = SerializationUtils.deserialize(bytes);
                return Optional.of(new DtoBootstrapFile(
                        crypter.decrypt(encryptedDto.getUsername()),
                        crypter.decrypt(encryptedDto.getPassword()),
                        encryptedDto.isSaveCredentials(),
                        encryptedDto.isAutoLogin()));
            }
        } catch (Throwable e) {
            LOGGER.error("Error on reading bootstrap file.", e);
        }

        return Optional.empty();
    }
}
