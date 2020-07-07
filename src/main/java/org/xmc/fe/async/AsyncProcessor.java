package org.xmc.fe.async;

import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.ui.components.async.IDisableAsync;
import org.xmc.fe.ui.components.async.ProcessViewElement;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
public class AsyncProcessor {
    private final ExecutorService asyncThreadPool;

    @Autowired
    public AsyncProcessor(ExecutorService asyncThreadPool) {
        this.asyncThreadPool = asyncThreadPool;
    }

    public <RESULT_TYPE> void runAsyncAndDisable(
            IDisableAsync component,
            IAsyncCallable<RESULT_TYPE> callable,
            Consumer<RESULT_TYPE> resultProcessor) {

        asyncThreadPool.submit(() -> processAsync(
                () -> component.setDisableAsync(true),
                callable,
                resultProcessor,
                () -> component.setDisableAsync(false)));
    }

    public <RESULT_TYPE> void runAsync(
            IAsyncCallable<RESULT_TYPE> callable,
            Consumer<RESULT_TYPE> resultProcessor) {

        asyncThreadPool.submit(() -> processAsync(
                () -> {},
                callable,
                resultProcessor,
                () -> {}));
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

        ProcessViewElement element = new ProcessViewElement(MainController.processViewRef);
        Platform.runLater(() -> MainController.processViewRef.getElements().add(element));
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
            Platform.runLater(() -> MainController.processViewRef.getElements().remove(element));
        }
    }
}
