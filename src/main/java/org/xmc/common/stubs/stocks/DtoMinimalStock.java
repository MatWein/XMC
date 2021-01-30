package org.xmc.common.stubs.stocks;

import com.querydsl.core.annotations.QueryProjection;

import java.io.Serializable;

public class DtoMinimalStock implements Serializable {
	private String isin;
	private String wkn;
	private String name;
	
	public DtoMinimalStock() {
	}
	
	@QueryProjection
	public DtoMinimalStock(String isin, String wkn, String name) {
		this.isin = isin;
		this.wkn = wkn;
		this.name = name;
	}
	
	public String getIsin() {
		return isin;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	public String getWkn() {
		return wkn;
	}
	
	public void setWkn(String wkn) {
		this.wkn = wkn;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
