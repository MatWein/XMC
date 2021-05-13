package io.github.matwein.xmc.fe;

import io.github.matwein.xmc.fe.common.SleepUtil;
import javafx.application.Platform;
import org.junit.jupiter.api.Disabled;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Disabled
public class JavaFxTestBase extends IntegrationTest {
	static {
		Platform.startup(() -> {});
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
