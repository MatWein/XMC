package io.github.matwein.xmc;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

public class TestObjectFactory {
	protected final EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();

	public <T> T createRandom(Class<T> clazz) {
		return enhancedRandom.nextObject(clazz);
	}
}
