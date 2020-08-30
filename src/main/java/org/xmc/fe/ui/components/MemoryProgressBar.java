package org.xmc.fe.ui.components;

import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import org.apache.commons.io.FileUtils;
import org.xmc.common.utils.SleepUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.concurrent.TimeUnit;

public class MemoryProgressBar extends ProgressBar {
    public void initialize() {
        startMemoryBarThread();
    }

    private void startMemoryBarThread() {
        Thread memoryBarThread = new Thread(this::runProgressbarUpdate);
        memoryBarThread.setDaemon(true);
        memoryBarThread.start();
    }

    private void runProgressbarUpdate() {
        Runtime runtime = Runtime.getRuntime();

        while (true) {
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;

            String usedMemoryInMb = FileUtils.byteCountToDisplaySize(usedMemory);
            String totalMemoryInMb = FileUtils.byteCountToDisplaySize(totalMemory);
            double usedMemoryInPercent = usedMemory * 100.0 / totalMemory;

            Platform.runLater(() -> {
                String text = MessageAdapter.getByKey(MessageKey.MAIN_MEMORY, usedMemoryInMb, totalMemoryInMb, usedMemoryInPercent);
                this.setTooltip(new Tooltip(text));
                this.setProgress((double)usedMemory / (double)totalMemory);
            });
            SleepUtil.sleep(TimeUnit.SECONDS.toMillis(15));
        }
    }
}
