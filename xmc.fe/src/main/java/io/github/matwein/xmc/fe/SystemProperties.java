package io.github.matwein.xmc.fe;

public interface SystemProperties {
	String XMC_HOME_TYPE = "xmc.home.type";
	String XMC_HOME_PATH = "xmc.home.path";
	String XMC_LANGUAGE = "xmc.language";
	String XMC_STYLE = "xmc.style";
	
	String HOME_HOME = "home";
	String HOME_WORKINGDIR = "workingdir";
	String HOME_CUSTOM = "custom";
	
	String SPRING_CONFIG_ADDITIONAL_LOCATION = "spring.config.additional-location";
	String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
	
	String SYSTEM_HOME_DIR = "system.home.dir";
	String SYSTEM_HOME_LOG_DIR = "system.home.log.dir";
	
	String DERBY_STREAM_ERROR_FILE = "derby.stream.error.file";
	
	String USER_HOME = "user.home";
	String USER_DIR = "user.dir";
	String USER_NAME = "user.name";
	String USER_PASSWORD = "user.password";
	String USER_DATABASE_DIR = "user.database.dir";
	String USER_DISPLAYNAME = "user.displayName";
}
