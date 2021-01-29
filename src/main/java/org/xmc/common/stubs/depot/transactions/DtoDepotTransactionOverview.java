package org.xmc.common.stubs.depot.transactions;

import java.time.LocalDateTime;

public class DtoDepotTransactionOverview extends DtoDepotTransaction {
	private LocalDateTime creationDate;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
