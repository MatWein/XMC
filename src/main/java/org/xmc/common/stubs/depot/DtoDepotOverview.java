package org.xmc.common.stubs.depot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DtoDepotOverview extends DtoDepot {
	private LocalDateTime creationDate;
	private BigDecimal lastSaldo;
	private LocalDate lastSaldoDate;
	
	public LocalDateTime getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
	public BigDecimal getLastSaldo() {
		return lastSaldo;
	}
	
	public void setLastSaldo(BigDecimal lastSaldo) {
		this.lastSaldo = lastSaldo;
	}
	
	public LocalDate getLastSaldoDate() {
		return lastSaldoDate;
	}
	
	public void setLastSaldoDate(LocalDate lastSaldoDate) {
		this.lastSaldoDate = lastSaldoDate;
	}
}
