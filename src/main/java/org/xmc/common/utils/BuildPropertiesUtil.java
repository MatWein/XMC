package org.xmc.common.utils;

import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

public class BuildPropertiesUtil {
    public static String loadVersionWithoutSprintContext() {
        try {
            return new ProjectInfoAutoConfiguration(new ProjectInfoProperties()).buildProperties().getVersion();
        } catch (Exception e) {
            return "development";
        }
    }
}
