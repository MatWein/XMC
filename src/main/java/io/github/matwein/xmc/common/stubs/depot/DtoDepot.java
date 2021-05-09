package io.github.matwein.xmc.common.stubs.depot;

import com.querydsl.core.annotations.QueryProjection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;

import java.io.Serializable;

public class DtoDepot implements Serializable {
	private Long id;
	private DtoBank bank;
	
	private String number;
	private String name;
	private String color;
	
	public DtoDepot() {
	}
	
	@QueryProjection
	public DtoDepot(
			Long id, String number, String name, String color,
			Long bankId, String bankName, String bic, String blz, byte[] logo) {
		
		this.id = id;
		this.bank = new DtoBank(bankId, bankName, bic, blz, logo);
		this.number = number;
		this.name = name;
		this.color = color;
	}
	
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
				.append("number", number)
				.append("name", name)
				.append("color", color)
				.toString();
	}
}
