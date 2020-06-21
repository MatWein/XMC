package org.xmc.be.entities.cashaccount;

import org.xmc.be.entities.Bank;
import org.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Table(name = CashAccount.TABLE_NAME)
public class CashAccount extends DeletablePersistentObject {
    public static final String TABLE_NAME = "CASH_ACCOUNTS";

    @ManyToOne(optional = false)
    @JoinColumn(name = "BANK_ID")
    private Bank bank;

    @Column(name = "IBAN", nullable = true)
    private String iban;
    @Column(name = "NUMBER", nullable = true)
    private String number;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "CURRENCY", nullable = false)
    private Currency currency;

    @Column(name = "LAST_SALDO", nullable = true)
    private BigDecimal lastSaldo;
    @Column(name = "LAST_SALDO_DATE", nullable = true)
    private LocalDateTime lastSaldoDate;

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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getLastSaldo() {
        return lastSaldo;
    }

    public void setLastSaldo(BigDecimal lastSaldo) {
        this.lastSaldo = lastSaldo;
    }

    public LocalDateTime getLastSaldoDate() {
        return lastSaldoDate;
    }

    public void setLastSaldoDate(LocalDateTime lastSaldoDate) {
        this.lastSaldoDate = lastSaldoDate;
    }
}
