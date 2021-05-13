package io.github.matwein.xmc.fe;

import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.XmcApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@Disabled
@SpringBootTest
public class IntegrationTest extends JUnitTestBase {
    @Autowired
    protected ConfigurableApplicationContext applicationContext;
	
	@BeforeEach
	void setUp() {
		XmcApplication.applicationContext = applicationContext;
	}
	
	@AfterEach
	void tearDown() {
		XmcApplication.applicationContext = null;
	}
}
