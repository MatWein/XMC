package org.xmc.be.entities.cashaccount;

import com.google.common.collect.Sets;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.DeletablePersistentObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
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

    @Column(name = "LAST_SALDO", nullable = true)
    private BigDecimal lastSaldo;
    @Column(name = "LAST_SALDO_DATE", nullable = true)
    private LocalDateTime lastSaldoDate;

    @OneToMany(mappedBy = "cashAccount")
    private Set<CashAccountTransaction> transactions = Sets.newHashSet();

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
        return currency == null ? null : Currency.getInstance(currency);
    }

    public void setCurrency(Currency currency) {
        this.currency = currency.getCurrencyCode();
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

    public Set<CashAccountTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<CashAccountTransaction> transactions) {
        this.transactions = transactions;
    }
}
