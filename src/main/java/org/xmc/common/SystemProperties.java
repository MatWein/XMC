package org.xmc.common;

public interface SystemProperties {
	String SILENT_MODE = "xmc.silent-mode";
	
	String HOME_TYPE = "xmc.home.type";
	String HOME_PATH = "xmc.home.path";
	
	String HOME_HOME = "home";
	String HOME_WORKINGDIR = "workingdir";
	String HOME_CUSTOM = "custom";
	
	String SPRING_CONFIG_ADDITIONAL_LOCATION = "spring.config.additional-location";
	String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
	
	String SYSTEM_HOME_DIR = "system.home.dir";
	String SYSTEM_HOME_LOG_DIR = "system.home.log.dir";
	
	String DERBY_STREAM_ERROR_FILE = "derby.stream.error.file";
	String JDK_GTK_VERSION = "jdk.gtk.version";
	
	String USER_HOME = "user.home";
	String USER_DIR = "user.dir";
	String USER_NAME = "user.name";
	String USER_PASSWORD = "user.password";
	String USER_DATABASE_DIR = "user.database.dir";
}
