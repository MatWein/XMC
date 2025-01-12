package io.github.matwein.xmc.common.stubs.depot.deliveries;

import io.github.matwein.xmc.common.stubs.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoDepotDeliveryOverview extends DtoDepotDelivery {
	private Money saldo;
	private LocalDateTime creationDate;
	
	public DtoDepotDeliveryOverview() {
	}
	
	public DtoDepotDeliveryOverview(Long id, LocalDateTime deliveryDate, BigDecimal saldo, LocalDateTime creationDate) {
		super(id, deliveryDate);
		
		this.saldo = new Money(saldo);
		this.creationDate = creationDate;
	}
	
	public Money getSaldo() {
		return saldo;
	}
	
	public void setSaldo(Money saldo) {
		this.saldo = saldo;
	}
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
}
