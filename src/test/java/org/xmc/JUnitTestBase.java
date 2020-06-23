package org.xmc;

import javafx.application.Platform;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.xmc.common.utils.SleepUtil;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Ignore
public class JUnitTestBase {
	protected final DtoGraphGenerator dtoGraphGenerator = new DtoGraphGenerator();
	protected final TestObjectFactory testObjectFactory = new TestObjectFactory();

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	protected void runJavaFxCode(Runnable runnable) throws Throwable {
		AtomicBoolean finished = new AtomicBoolean(false);
		AtomicReference<Throwable> exception = new AtomicReference<>();

		Platform.startup(() -> {
			try {
				runnable.run();
			} catch (Throwable e) {
				exception.set(e);
			} finally {
				finished.set(true);
			}
		});

		while (!finished.get()) {
			SleepUtil.sleep(100);
		}

		if (exception.get() != null) {
			throw exception.get();
		}
	}
}
