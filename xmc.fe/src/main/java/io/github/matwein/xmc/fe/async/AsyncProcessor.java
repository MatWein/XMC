package io.github.matwein.xmc.fe.async;

import io.github.matwein.xmc.fe.common.SleepUtil;
import io.github.matwein.xmc.fe.stages.main.MainController;
import io.github.matwein.xmc.fe.ui.components.async.ProcessViewElement;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
public class AsyncProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class);

    private final ExecutorService asyncThreadPool;

    @Autowired
    public AsyncProcessor(ExecutorService asyncThreadPool) {
        this.asyncThreadPool = asyncThreadPool;
    }

    public <RESULT_TYPE> void runAsync(
            IAsyncCallable<RESULT_TYPE> callable,
            Consumer<RESULT_TYPE> resultProcessor) {

        runAsync(
                () -> {},
                callable,
                resultProcessor,
                () -> {}
        );
    }

    public void runAsyncVoid(
            Runnable preProcessor,
            IAsyncCallableVoid callable,
            Runnable postProcessor) {

        runAsync(
                preProcessor,
                (IAsyncCallable<Void>) monitor -> {
                    callable.call(monitor);
                    return null;
                },
                (result) -> {},
                postProcessor
        );
    }
	
	public void runAsyncVoid(IAsyncCallableVoid callable) {
		runAsync(
				() -> {},
				(IAsyncCallable<Void>) monitor -> {
					callable.call(monitor);
					return null;
				},
				(result) -> {},
				() -> {}
		);
	}

    public <RESULT_TYPE> void runAsync(
            Runnable preProcessor,
            IAsyncCallable<RESULT_TYPE> callable,
            Consumer<RESULT_TYPE> resultProcessor,
            Runnable postProcessor) {

        asyncThreadPool.submit(() -> processAsync(preProcessor, callable, resultProcessor, postProcessor));
    }

    private <RESULT_TYPE> RESULT_TYPE processAsync(
            Runnable preProcessor,
            IAsyncCallable<RESULT_TYPE> callable,
            Consumer<RESULT_TYPE> resultProcessor,
            Runnable postProcessor) throws Exception {

        try {
            while (MainController.processViewRef == null) {
                SleepUtil.sleep(1000);
            }

            ProcessViewElement element = MainController.processViewRef.addNewElement();
            AsyncMonitor asyncMonitor = new AsyncMonitor(element);

            try {
                Platform.runLater(preProcessor);
                try {
                    RESULT_TYPE result = callable.call(asyncMonitor);
                    Platform.runLater(() -> resultProcessor.accept(result));
                    return result;
                } finally {
                    Platform.runLater(postProcessor);
                }
            } finally {
                Platform.runLater(() -> MainController.processViewRef.removeElement(element));
            }
        } catch (Exception e) {
            LOGGER.error("Unknown error on async execution.", e);
            throw e;
        }
    }
}
