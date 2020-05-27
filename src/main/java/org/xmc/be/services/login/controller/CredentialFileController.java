package org.xmc.be.services.login.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmc.be.services.login.dto.DtoCredentials;
import org.xmc.common.utils.Crypter;
import org.xmc.common.utils.HomeDirectoryPathCalculator;

import java.io.File;
import java.util.Optional;

public class CredentialFileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialFileController.class);

    public static void writeCredentialFile(String username, String password, boolean saveCredentials) {
        try {
            File credentialFile = new File(HomeDirectoryPathCalculator.calculateCredentialFilePath());
            if (saveCredentials) {
                Crypter crypter = new Crypter();
                DtoCredentials dtoCredentials = new DtoCredentials(
                        crypter.encrypt(username),
                        crypter.encrypt(password));
                byte[] bytes = crypter.encrypt(SerializationUtils.serialize(dtoCredentials));
                FileUtils.writeByteArrayToFile(credentialFile, bytes);
            } else {
                credentialFile.delete();
            }
        } catch (Throwable e) {
            LOGGER.error("Error on writing bootstrap file.", e);
        }
    }

    public static Optional<DtoCredentials> readCredentialFile() {
        try {
            File credentialFile = new File(HomeDirectoryPathCalculator.calculateCredentialFilePath());
            if (credentialFile.isFile()) {
                Crypter crypter = new Crypter();
                byte[] bytes = crypter.decrypt(FileUtils.readFileToByteArray(credentialFile));
                DtoCredentials encryptedDto = SerializationUtils.deserialize(bytes);
                return Optional.of(new DtoCredentials(
                        crypter.decrypt(encryptedDto.getUsername()),
                        crypter.decrypt(encryptedDto.getPassword())));
            }
        } catch (Throwable e) {
            LOGGER.error("Error on reading bootstrap file.", e);
        }

        return Optional.empty();
    }
}
