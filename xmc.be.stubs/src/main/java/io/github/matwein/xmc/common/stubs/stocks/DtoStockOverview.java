package io.github.matwein.xmc.common.stubs.stocks;

import java.time.LocalDateTime;

public class DtoStockOverview extends DtoStock {
	private LocalDateTime creationDate;
	
	public DtoStockOverview() {
	}
	
	public DtoStockOverview(Long id, String isin, String name, String wkn, LocalDateTime creationDate, Long categoryId, String categoryName) {
		super(id, isin, name, wkn, categoryId, categoryName);
		
		this.creationDate = creationDate;
	}
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
