package io.github.matwein.xmc.be.entities.cashaccount;

import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.DeletablePersistentObject;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = CashAccount.TABLE_NAME)
public class CashAccount extends DeletablePersistentObject {
    public static final String TABLE_NAME = "CASH_ACCOUNTS";

    @ManyToOne(optional = false)
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @Column(name = "IBAN", nullable = true, length = 50)
    private String iban;
    @Column(name = "NUMBER", nullable = true)
    private String number;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "CURRENCY", nullable = false, length = 5)
    private String currency;
	@Column(name = "COLOR", nullable = true, length = 7)
	private String color;

    @Column(name = "LAST_SALDO", nullable = true)
    private BigDecimal lastSaldo;
    @Column(name = "LAST_SALDO_DATE", nullable = true)
    private LocalDate lastSaldoDate;

    @OneToMany(mappedBy = "cashAccount")
    private Set<CashAccountTransaction> transactions = new HashSet<>();

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
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

    public Set<CashAccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<CashAccountTransaction> transactions) {
        this.transactions = transactions;
    }
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
}
