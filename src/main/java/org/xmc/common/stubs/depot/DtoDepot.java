package org.xmc.common.stubs.depot;

import org.xmc.common.stubs.bank.DtoBank;

import java.io.Serializable;

public class DtoDepot implements Serializable {
	private Long id;
	private DtoBank bank;
	
	private String number;
	private String name;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public DtoBank getBank() {
		return bank;
	}
	
	public void setBank(DtoBank bank) {
		this.bank = bank;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
}
