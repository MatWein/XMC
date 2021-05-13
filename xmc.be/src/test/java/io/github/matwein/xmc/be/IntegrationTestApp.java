package io.github.matwein.xmc.be;

import io.github.matwein.xmc.be.config.properties.XmcProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ XmcProperties.class })
public class IntegrationTestApp {
}
