package io.github.matwein.xmc.fe;

import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@Disabled
@SpringBootTest
public class IntegrationTest {
    @Autowired
    protected ConfigurableApplicationContext applicationContext;
	
	@BeforeEach
	void setUp() {
		XmcFrontendContext.applicationContext = () -> applicationContext;
	}
	
	@AfterEach
	void tearDown() {
		XmcFrontendContext.applicationContext = null;
	}
}
