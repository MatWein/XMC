package io.github.matwein.xmc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.mockito.MockitoAnnotations;

@Disabled
public class JUnitTestBase {
	protected final TestObjectFactory testObjectFactory = new TestObjectFactory();

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
}
