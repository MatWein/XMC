package org.xmc.fe.async;

import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.async.ProcessViewElement;

public class AsyncMonitor {
    private final ProcessViewElement processViewElement;

    public AsyncMonitor(ProcessViewElement processViewElement) {
        this.processViewElement = processViewElement;
    }

    public void setProgress(double progress) {
        processViewElement.setProgress(progress);
    }

    public void setStatusText(MessageKey statusText) {
        setStatusText(MessageAdapter.getByKey(statusText));
    }

    public void setStatusText(String statusText) {
        processViewElement.setText(statusText);
    }
}
