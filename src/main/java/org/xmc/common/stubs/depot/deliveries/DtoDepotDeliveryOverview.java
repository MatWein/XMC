package org.xmc.common.stubs.depot.deliveries;

import java.time.LocalDateTime;

public class DtoDepotDeliveryOverview extends DtoDepotDelivery {
	private LocalDateTime creationDate;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
