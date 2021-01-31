package org.xmc.common.stubs.depot.deliveries;

import org.xmc.common.stubs.Money;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DtoDepotDelivery implements Serializable {
	private Long id;
	
	private LocalDateTime deliveryDate;
	private Money saldo;
	
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
	
	public Money getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Money saldo) {
		this.saldo = saldo;
	}
}
