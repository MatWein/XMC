package io.github.matwein.xmc.common.stubs.importing;

import io.github.matwein.xmc.be.entities.importing.ImportTemplateType;

import java.io.Serializable;

public class DtoImportTemplateOverview implements Serializable {
	private long id;
	
	private String name;
	private ImportTemplateType type;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ImportTemplateType getType() {
		return type;
	}
	
	public void setType(ImportTemplateType type) {
		this.type = type;
	}
}
