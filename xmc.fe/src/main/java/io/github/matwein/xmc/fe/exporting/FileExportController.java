package io.github.matwein.xmc.fe.exporting;

import io.github.matwein.xmc.common.services.exporting.IFileExportService;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.ui.DialogHelper;
import io.github.matwein.xmc.fe.ui.ExtensionFilterType;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class FileExportController {
	private final AsyncProcessor asyncProcessor;
	private final IFileExportService fileExportService;
	
	@Autowired
	public FileExportController(
			AsyncProcessor asyncProcessor,
			IFileExportService fileExportService) {
		
		this.asyncProcessor = asyncProcessor;
		this.fileExportService = fileExportService;
	}
	
	public <T extends Serializable> void exportItemsToCsvFile(Window window,  Class<T> type, Function<AsyncMonitor, List<T>> itemLoader) {
		asyncProcessor.runAsync(
				monitor -> {
					List<T> itemsToExport = itemLoader.apply(monitor);
					return fileExportService.exportToCsv(monitor, type, itemsToExport);
				},
				result -> {
					Optional<File> fileToSave = DialogHelper.showSaveFileDialog(window, ExtensionFilterType.CSV);
					if (fileToSave.isPresent()) {
						try {
							FileUtils.writeByteArrayToFile(fileToSave.get(), result);
						} catch (IOException e) {
							String message = String.format("Error on saving byte array to file '%s'.", fileToSave.get());
							throw new RuntimeException(message, e);
						}
					}
				}
		);
	}
}
