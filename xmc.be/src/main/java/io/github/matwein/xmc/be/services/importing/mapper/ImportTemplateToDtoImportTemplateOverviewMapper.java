package io.github.matwein.xmc.be.services.importing.mapper;

import io.github.matwein.xmc.be.entities.importing.ImportTemplate;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplateOverview;
import org.springframework.stereotype.Component;

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
