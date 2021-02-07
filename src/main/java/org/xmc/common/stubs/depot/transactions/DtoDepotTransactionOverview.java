package org.xmc.common.stubs.depot.transactions;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtoDepotTransactionOverview extends DtoDepotTransaction {
	private LocalDateTime creationDate;
	
	private String wkn;
	private String name;
	
	public DtoDepotTransactionOverview() {
	}
	
	@QueryProjection
	public DtoDepotTransactionOverview(
			Long id, String isin, LocalDate valutaDate, BigDecimal amount, BigDecimal course,
			BigDecimal value, String description, String currency, LocalDateTime creationDate,
			String wkn, String name) {
		
		super(id, isin, valutaDate, amount, course, value, description, currency);
		
		this.creationDate = creationDate;
		this.wkn = wkn;
		this.name = name;
	}
	
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
