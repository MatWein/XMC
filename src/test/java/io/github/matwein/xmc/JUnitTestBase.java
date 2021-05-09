package io.github.matwein.xmc;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.mockito.MockitoAnnotations;
import io.github.matwein.xmc.common.utils.SleepUtil;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Disabled
public class JUnitTestBase {
	protected final TestObjectFactory testObjectFactory = new TestObjectFactory();

	static {
		Platform.startup(() -> {});
	}

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	protected void runJavaFxCode(Runnable runnable) throws Throwable {
		AtomicBoolean finished = new AtomicBoolean(false);
		AtomicReference<Throwable> exception = new AtomicReference<>();

		Platform.runLater(() -> {
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
