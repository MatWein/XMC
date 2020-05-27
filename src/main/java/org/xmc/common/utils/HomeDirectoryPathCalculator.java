package org.xmc.common.utils;

import org.springframework.util.DigestUtils;

import java.io.File;

public class HomeDirectoryPathCalculator {
    public static String calculateHomeDir() {
        return System.getProperty("user.dir");
    }

    public static String calculateLogDir() {
        return new File(calculateHomeDir(), "logs").getAbsolutePath();
    }

    public static String calculateDerbyLogFilePath() {
        return new File(calculateLogDir(), "derby.log").getAbsolutePath();
    }

    public static String calculateDatabaseDir() {
        return new File(calculateHomeDir(), "database").getAbsolutePath();
    }

    public static String calculateDatabaseDirForUser(String username) {
        String usernameHash = DigestUtils.md5DigestAsHex(username.getBytes());
        return new File(calculateDatabaseDir(), usernameHash).getAbsolutePath();
    }

    public static String calculateCredentialFilePath() {
        return new File(calculateHomeDir(), ".bootstrap").getAbsolutePath();
    }
}
