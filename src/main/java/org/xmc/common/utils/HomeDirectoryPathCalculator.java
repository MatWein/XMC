package org.xmc.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.util.Arrays;

public class HomeDirectoryPathCalculator {
    private static final String HOME_TYPE = "home.type";
    private static final String HOME_PATH = "home.path";

    private static final String HOME_HOME = "home";
    private static final String HOME_WORKINGDIR = "workingdir";
    private static final String HOME_CUSTOM = "custom";

    private static String cachedHomeDir;

    public static void initializeSystemProperties() {
        String homeDir = calculateHomeDir();
        System.setProperty("system.home.dir", homeDir);

        String logDir = calculateLogDir();
        System.setProperty("system.home.log.dir", logDir);

        String derbyLogFilePath = calculateDerbyLogFilePath();
        System.setProperty("derby.stream.error.file", derbyLogFilePath);

        LoggerFactory.getLogger(HomeDirectoryPathCalculator.class).info("Using home directory '{}'.", homeDir);
    }

    public static String calculateHomeDir() {
        if (cachedHomeDir != null) {
            return cachedHomeDir;
        }

        return cachedHomeDir = calculateHomeDirUncached();
    }

    private static String calculateHomeDirUncached() {
        String homeType = StringUtils.defaultString(System.getProperty(HOME_TYPE), HOME_HOME);

        switch (homeType) {
            case HOME_HOME:
                return new File(System.getProperty("user.home"), ".xmc").getAbsolutePath();
            case HOME_WORKINGDIR:
                return System.getProperty("user.dir");
            case HOME_CUSTOM:
                String homePath = System.getProperty(HOME_PATH);
                if (StringUtils.isNotBlank(homePath) && new File(homePath).isDirectory()) {
                    return new File(homePath).getAbsolutePath();
                } else {
                    String message = String.format("Try to use custom home directory, but got invalid home path '%s'.", homePath);
                    throw new RuntimeException(message);
                }
            default:
                String message = String.format("Got invalid home type '%s'. Please use one of the following: %s",
                        homeType, Arrays.asList(HOME_HOME, HOME_WORKINGDIR, HOME_CUSTOM));
                throw new RuntimeException(message);
        }
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
