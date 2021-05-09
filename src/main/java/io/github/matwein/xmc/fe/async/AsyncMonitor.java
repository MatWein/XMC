package io.github.matwein.xmc.fe.async;

import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.components.async.ProcessViewElement;
import scalc.SCalcBuilder;

public class AsyncMonitor {
    private final ProcessViewElement processViewElement;

    public AsyncMonitor(ProcessViewElement processViewElement) {
        this.processViewElement = processViewElement;
    }

    public void setProgress(double progress) {
        processViewElement.setProgress(progress);
    }
	
	public void setProgressByItemCount(int processedItems, int itemCount) {
		setProgress(SCalcBuilder.doubleInstance()
				.expression("processedItems / itemCount")
				.build()
				.parameter("processedItems", processedItems)
				.parameter("itemCount", itemCount)
				.calc());
	}

    public void setStatusText(MessageKey statusText) {
        setStatusText(MessageAdapter.getByKey(statusText));
    }

    public void setStatusText(String statusText) {
        processViewElement.setText(statusText);
    }
}
