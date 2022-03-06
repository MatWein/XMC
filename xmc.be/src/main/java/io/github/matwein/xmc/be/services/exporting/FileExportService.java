package io.github.matwein.xmc.be.services.exporting;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.services.exporting.controller.BeanToCsvExportController;
import io.github.matwein.xmc.common.services.exporting.IFileExportService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class FileExportService implements IFileExportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileExportService.class);
	
	private final BeanToCsvExportController beanToCsvExportController;
	
	@Autowired
	public FileExportService(BeanToCsvExportController beanToCsvExportController) {
		this.beanToCsvExportController = beanToCsvExportController;
	}
	
	@Override
	public <T extends Serializable> byte[] exportToCsv(IAsyncMonitor monitor, Class<T> type, List<T> itemsToExport) {
		LOGGER.info("Exporting {} items to CSV file.", itemsToExport.size());
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_EXPORT_ITEMS_TO_FILE));
		
		return beanToCsvExportController.export(type, itemsToExport);
	}
}
