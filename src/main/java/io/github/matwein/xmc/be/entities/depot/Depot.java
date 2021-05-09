package io.github.matwein.xmc.be.entities.depot;

import com.google.common.collect.Sets;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = Depot.TABLE_NAME)
public class Depot extends DeletablePersistentObject {
	public static final String TABLE_NAME = "DEPOTS";
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BANK_ID")
	private Bank bank;
	
	@Column(name = "NUMBER", nullable = true)
	private String number;
	@Column(name = "NAME", nullable = false)
	private String name;
	@Column(name = "COLOR", nullable = true, length = 7)
	private String color;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "LAST_DELIVERY_ID")
	private DepotDelivery lastDelivery;
	
	@OneToMany(mappedBy = "depot")
	private Set<DepotDelivery> deliveries = Sets.newHashSet();
	
	@OneToMany(mappedBy = "depot")
	private Set<DepotTransaction> transactions = Sets.newHashSet();
	
	public Bank getBank() {
		return bank;
	}
	
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DepotDelivery getLastDelivery() {
		return lastDelivery;
	}
	
	public void setLastDelivery(DepotDelivery lastDelivery) {
		this.lastDelivery = lastDelivery;
	}
	
	public Set<DepotDelivery> getDeliveries() {
		return deliveries;
	}
	
	public void setDeliveries(Set<DepotDelivery> deliveries) {
		this.deliveries = deliveries;
	}
	
	public Set<DepotTransaction> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(Set<DepotTransaction> transactions) {
		this.transactions = transactions;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
}
