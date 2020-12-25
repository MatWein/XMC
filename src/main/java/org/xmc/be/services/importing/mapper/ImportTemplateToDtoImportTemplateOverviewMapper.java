package org.xmc.be.services.importing.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.importing.ImportTemplate;
import org.xmc.common.stubs.importing.DtoImportTemplateOverview;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImportTemplateToDtoImportTemplateOverviewMapper {
	public List<DtoImportTemplateOverview> mapAll(List<ImportTemplate> templates) {
		return templates.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}
	
	private DtoImportTemplateOverview map(ImportTemplate importTemplate) {
		var dto = new DtoImportTemplateOverview();
		
		dto.setId(importTemplate.getId());
		dto.setName(importTemplate.getName());
		dto.setType(importTemplate.getType());
		
		return dto;
	}
}
