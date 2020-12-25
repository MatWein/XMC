package org.xmc.be.entities.depot;

import com.google.common.collect.Sets;
import org.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = DepotDelivery.TABLE_NAME)
public class DepotDelivery extends DeletablePersistentObject {
	public static final String TABLE_NAME = "DEPOT_DELIVERIES";
	
	static final String COLUMN_DEPOT_ID = "DEPOT_ID";
	static final String COLUMN_DELIVERY_DATE = "DELIVERY_DATE";
	static final String COLUMN_SALDO = "SALDO";
	
	@ManyToOne(optional = false)
	@JoinColumn(name = COLUMN_DEPOT_ID)
	private Depot depot;
	
	@Column(name = COLUMN_DELIVERY_DATE, nullable = false)
	private LocalDateTime deliveryDate;
	@Column(name = COLUMN_SALDO, nullable = false)
	private BigDecimal saldo;
	
	@OneToMany(mappedBy = "delivery")
	private Set<DepotItem> depotItems = Sets.newHashSet();
	
	public Depot getDepot() {
		return depot;
	}
	
	public void setDepot(Depot depot) {
		this.depot = depot;
	}
	
	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}
	
	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public Set<DepotItem> getDepotItems() {
		return depotItems;
	}
	
	public void setDepotItems(Set<DepotItem> depotItems) {
		this.depotItems = depotItems;
	}
}
