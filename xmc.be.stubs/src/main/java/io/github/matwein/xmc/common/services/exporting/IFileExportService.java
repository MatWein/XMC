package io.github.matwein.xmc.common.services.exporting;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;

import java.io.Serializable;
import java.util.List;

public interface IFileExportService {
	<T extends Serializable> byte[] exportToCsv(IAsyncMonitor monitor, Class<T> type, List<T> itemsToExport);
}
