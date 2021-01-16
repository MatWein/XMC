package org.xmc.common.stubs.stocks;

import java.time.LocalDateTime;

public class DtoStockOverview extends DtoStock {
	private LocalDateTime creationDate;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
