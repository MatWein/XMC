package org.xmc;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

@Ignore
public class JUnitTestBase {
	protected final DtoGraphGenerator dtoGraphGenerator = new DtoGraphGenerator();
	protected final TestObjectFactory testObjectFactory = new TestObjectFactory();

	@BeforeEach
	public void before() {
		MockitoAnnotations.initMocks(this);
	}
}
