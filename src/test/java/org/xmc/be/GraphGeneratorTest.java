package org.xmc.be;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

class GraphGeneratorTest extends DerbyDatabaseTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphGeneratorTest.class);
	
	@Autowired 
	private GraphGenerator graphGenerator;
	
	@Test
	public void testCreateAllEntities() throws Exception {
		Method[] methods = graphGenerator.getClass().getDeclaredMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			if (method.getParameters().length == 0) {
				LOGGER.debug("Invoking {}", method);
				method.invoke(graphGenerator);
				flush();
			}
		}
	}
}
