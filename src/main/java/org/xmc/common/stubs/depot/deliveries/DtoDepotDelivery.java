package org.xmc.common.stubs.depot.deliveries;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DtoDepotDelivery implements Serializable {
	private Long id;
	
	private LocalDateTime deliveryDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}
	
	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
