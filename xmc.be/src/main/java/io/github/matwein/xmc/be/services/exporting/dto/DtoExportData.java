package io.github.matwein.xmc.be.services.exporting.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DtoExportData implements Serializable {
	private String[] headers = new String[0];
	private List<Object[]> rows = new ArrayList<>();
	
	public String[] getHeaders() {
		return headers;
	}
	
	public void setHeaders(String[] headers) {
		this.headers = headers;
	}
	
	public List<Object[]> getRows() {
		return rows;
	}
	
	public void setRows(List<Object[]> rows) {
		this.rows = rows;
	}
}
