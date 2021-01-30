package org.xmc.common.stubs.depot.transactions;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtoDepotTransactionOverview extends DtoDepotTransaction {
	private LocalDateTime creationDate;
	
	public DtoDepotTransactionOverview() {
	}
	
	@QueryProjection
	public DtoDepotTransactionOverview(
			Long id, String isin, LocalDate valutaDate, BigDecimal amount, BigDecimal course,
			BigDecimal value, String description, String currency, LocalDateTime creationDate) {
		
		super(id, isin, valutaDate, amount, course, value, description, currency);
		
		this.creationDate = creationDate;
	}
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
