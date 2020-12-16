package org.xmc.be;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import java.lang.reflect.Method;
import java.util.Set;

class GraphGeneratorTest extends IntegrationTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(GraphGeneratorTest.class);
	
	@Autowired 
	private GraphGenerator graphGenerator;
	
	@Test
	void testCreateAllEntities() throws Exception {
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

	@Test
	void testAllEntitiesHaveGraphGeneratorMethod() {
		Reflections reflections = new Reflections("org.xmc.be.entities");

		Set<Class<?>> entityTypes = reflections.getTypesAnnotatedWith(Entity.class);
		for (Class<?> entityType : entityTypes) {
			String methodName = "create" + entityType.getSimpleName();
			try {
				graphGenerator.getClass().getDeclaredMethod(methodName);
			} catch (NoSuchMethodException e) {
				Assertions.fail(String.format("Graph generator has no method named '%s'.", methodName));
			}
		}
	}
}
