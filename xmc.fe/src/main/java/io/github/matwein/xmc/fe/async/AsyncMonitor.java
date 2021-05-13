package io.github.matwein.xmc.fe.async;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.fe.ui.components.async.ProcessViewElement;
import scalc.SCalcBuilder;

public class AsyncMonitor implements IAsyncMonitor {
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

    public void setStatusText(String statusText) {
        processViewElement.setText(statusText);
    }
}
