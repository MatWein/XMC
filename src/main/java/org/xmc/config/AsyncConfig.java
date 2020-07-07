package org.xmc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class AsyncConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    private static final int MAX_THREADS = 5;
    private static final AtomicInteger CURRENT_THREAD_COUNTER = new AtomicInteger(0);

    @Bean
    public ExecutorService asyncThreadPool() {
        return Executors.newFixedThreadPool(MAX_THREADS, runnable -> {
            Thread thread = new Thread(runnable);

            thread.setDaemon(true);
            thread.setUncaughtExceptionHandler((t, e) -> LOGGER.error("Unexpected error on thread '{}'.", t.getName(), e));
            thread.setName(String.format("async-%s", CURRENT_THREAD_COUNTER.addAndGet(1)));

            return thread;
        });
    }

    public void shutdown(ExecutorService asyncThreadPool) {
        try {
            asyncThreadPool.shutdown();
        } finally {
            try {
                asyncThreadPool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("Error on shutting down thread pool.", e);
            } finally {
                asyncThreadPool.shutdownNow();
            }
        }
    }
}
