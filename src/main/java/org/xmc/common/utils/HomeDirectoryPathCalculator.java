package org.xmc.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.util.Arrays;

import static org.xmc.common.SystemProperties.*;

public class HomeDirectoryPathCalculator {
	static String cachedHomeDir;

    public static void initializeSystemProperties() {
        String homeDir = calculateHomeDir();
        System.setProperty(SYSTEM_HOME_DIR, homeDir);
        System.setProperty(SPRING_CONFIG_ADDITIONAL_LOCATION, "optional:" + homeDir + "/");
        System.setProperty(JDK_GTK_VERSION, "3");

        if (StringUtils.isBlank(System.getProperty(SPRING_PROFILES_ACTIVE))) {
            System.setProperty(SPRING_PROFILES_ACTIVE, "prod");
        }

        String logDir = calculateLogDir();
        System.setProperty(SYSTEM_HOME_LOG_DIR, logDir);

        String derbyLogFilePath = calculateDerbyLogFilePath();
        System.setProperty(DERBY_STREAM_ERROR_FILE, derbyLogFilePath);

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
                return new File(System.getProperty(USER_HOME), ".xmc").getAbsolutePath();
            case HOME_WORKINGDIR:
                return System.getProperty(USER_DIR);
            case HOME_CUSTOM:
                String homePath = System.getProperty(HOME_PATH);
	            
	            if (StringUtils.isNotBlank(homePath)) {
		            File homeDir = new File(homePath);
		            homeDir.mkdirs();
		            
		            if (homeDir.isDirectory()) {
			            return homeDir.getAbsolutePath();
		            }
                }
                String message = String.format("Try to use custom home directory, but got invalid home path '%s'.", homePath);
                throw new RuntimeException(message);
            default:
                message = String.format("Got invalid home type '%s'. Please use one of the following: %s",
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
