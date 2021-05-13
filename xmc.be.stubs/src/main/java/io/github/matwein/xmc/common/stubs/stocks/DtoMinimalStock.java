package io.github.matwein.xmc.common.stubs.stocks;

import java.io.Serializable;

public class DtoMinimalStock implements Serializable {
	private String isin;
	private String wkn;
	private String name;
	
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
