package org.xmc;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmc.be.entities.PersistentObject;

import java.util.Random;

public class TestObjectFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestObjectFactory.class);

	protected final EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();

	public <T> T createRandom(Class<T> clazz) {
		return enhancedRandom.nextObject(clazz);
	}

	public <T extends PersistentObject> T create(Class<T> clazz) {
		long id = new Random(System.currentTimeMillis()).nextLong();
		return create(clazz, id);
	}
	
	public <T extends PersistentObject> T create(Class<T> clazz, Long id) {
		try {
			T objectInstance = clazz.getConstructor().newInstance();
			objectInstance.setId(id);
			return objectInstance;
		} catch (Throwable e) {
			String message = String.format("Could not create instance of type '%s' with id '%s'.", clazz.getName(), id);
			LOGGER.error(message, e);
			throw new RuntimeException(message, e);
		}
	}
}
