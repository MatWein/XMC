package io.github.matwein.xmc.common.services.importing;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplateOverview;
import io.github.matwein.xmc.common.stubs.importing.ImportTemplateType;

import java.util.List;

public interface IImportTemplateService {
	<T extends Enum<T>> List<DtoImportTemplate<T>> loadImportTemplates(
			IAsyncMonitor monitor,
			ImportTemplateType templateType,
			Class<T> columnType);
	
	List<DtoImportTemplateOverview> loadImportTemplatesOverview(IAsyncMonitor monitor);
	
	boolean rename(IAsyncMonitor monitor, long templateId, String newName);
	
	void delete(IAsyncMonitor monitor, long templateId);
}
