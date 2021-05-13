package io.github.matwein.xmc.common.stubs.depot.transactions;

import java.time.LocalDateTime;

public class DtoDepotTransactionOverview extends DtoDepotTransaction {
	private LocalDateTime creationDate;
	
	private String wkn;
	private String name;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
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
