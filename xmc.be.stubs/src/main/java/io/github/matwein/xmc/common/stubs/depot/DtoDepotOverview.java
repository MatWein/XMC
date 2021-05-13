package io.github.matwein.xmc.common.stubs.depot;

import io.github.matwein.xmc.common.stubs.Money;

import java.time.LocalDateTime;

public class DtoDepotOverview extends DtoDepot {
	private LocalDateTime creationDate;
	private Money lastSaldo;
	private LocalDateTime lastSaldoDate;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	public Money getLastSaldo() {
		return lastSaldo;
	}
	
	public void setLastSaldo(Money lastSaldo) {
		this.lastSaldo = lastSaldo;
	}
	
	public LocalDateTime getLastSaldoDate() {
		return lastSaldoDate;
	}
	
	public void setLastSaldoDate(LocalDateTime lastSaldoDate) {
		this.lastSaldoDate = lastSaldoDate;
	}
}
