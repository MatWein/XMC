package org.xmc.common.stubs.depot.deliveries;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoDepotItemOverview extends DtoDepotItem {
	private LocalDateTime creationDate;
	
	public DtoDepotItemOverview() {
	}
	
	@QueryProjection
	public DtoDepotItemOverview(
			Long id, String isin, BigDecimal amount, BigDecimal course,
			BigDecimal value, String currency, LocalDateTime creationDate) {
		
		super(id, isin, amount, course, value, currency);
		
		this.creationDate = creationDate;
	}
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
