package io.github.matwein.xmc.common.stubs;

public interface IAsyncMonitor {
	void setProgress(double progress);
	
	void setProgressByItemCount(int processedItems, int itemCount);
	
	void setStatusText(String statusText);
}
