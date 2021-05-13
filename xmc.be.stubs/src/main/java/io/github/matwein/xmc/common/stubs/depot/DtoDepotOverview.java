package io.github.matwein.xmc.common.stubs.depot;

import com.querydsl.core.annotations.QueryProjection;
import io.github.matwein.xmc.common.stubs.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DtoDepotOverview extends DtoDepot {
	private LocalDateTime creationDate;
	private Money lastSaldo;
	private LocalDateTime lastSaldoDate;
	
	public DtoDepotOverview() {
	}
	
	@QueryProjection
	public DtoDepotOverview(
			Long id, String number, String name, String color,
			LocalDateTime creationDate, BigDecimal lastSaldo, LocalDateTime lastSaldoDate,
			Long bankId, String bankName, String bic, String blz, byte[] logo) {
		
		super(id, number, name, color, bankId, bankName, bic, blz, logo);
		
		this.creationDate = creationDate;
		this.lastSaldo = new Money(lastSaldo, "EUR");
		this.lastSaldoDate = lastSaldoDate;
	}
	
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
