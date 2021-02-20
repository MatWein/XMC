package org.xmc.common.stubs.depot.items;

import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoDepotItemOverview extends DtoDepotItem {
	private LocalDateTime creationDate;
	
	private String wkn;
	private String name;
	
	public DtoDepotItemOverview() {
	}
	
	@QueryProjection
	public DtoDepotItemOverview(
			Long id, String isin, BigDecimal amount, BigDecimal course,
			BigDecimal value, String currency, LocalDateTime creationDate,
			String wkn, String name) {
		
		super(id, isin, amount, course, value, currency);
		
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
