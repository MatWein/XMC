package org.xmc.common.stubs.stocks;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public class DtoStockOverview extends DtoStock {
	private LocalDateTime creationDate;
	
	public DtoStockOverview() {
	}
	
	@QueryProjection
	public DtoStockOverview(Long id, String isin, String name, String wkn, Long categoryId, String categoryName, LocalDateTime creationDate) {
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
