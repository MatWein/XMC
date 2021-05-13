package io.github.matwein.xmc;

import io.github.matwein.xmc.be.config.properties.XmcProperties;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({ XmcProperties.class })
@EnableAsync
@EnableScheduling
public class XmcApplication {
	public static ConfigurableApplicationContext applicationContext;
	public static String[] args;
	
	public static void start() {
		XmcApplication.applicationContext = new SpringApplicationBuilder(XmcApplication.class)
				.headless(false)
				.run(XmcApplication.args);
	}
	
	public static void destroy() {
		LoggerFactory.getLogger(XmcApplication.class).info("Closing application context.");
		
		if (applicationContext != null) {
			SpringApplication.exit(applicationContext);
			applicationContext = null;
		}
	}
}
