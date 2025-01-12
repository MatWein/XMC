package io.github.matwein.xmc.common.stubs.cashaccount;

import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class DtoCashAccount implements Serializable {
	private Long id;
	private DtoBank bank;
	
	private String iban;
	private String number;
	private String name;
	private String currency;
	private String color;
	
	public DtoCashAccount() {
	}
	
	public DtoCashAccount(
			Long id, String iban, String number, String name, String currency, String color,
			Long bankId, String bankName, String bic, String blz, byte[] logo) {
		
		this.id = id;
		this.iban = iban;
		this.number = number;
		this.name = name;
		this.currency = currency;
		this.color = color;
		this.bank = new DtoBank(bankId, bankName, bic, blz, logo);
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public DtoBank getBank() {
		return bank;
	}
	
	public void setBank(DtoBank bank) {
		this.bank = bank;
	}
	
	public String getIban() {
		return iban;
	}
	
	public void setIban(String iban) {
		this.iban = iban;
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
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", id)
				.append("bank", bank)
				.append("iban", iban)
				.append("number", number)
				.append("name", name)
				.append("currency", currency)
				.append("color", color)
				.toString();
	}
}
