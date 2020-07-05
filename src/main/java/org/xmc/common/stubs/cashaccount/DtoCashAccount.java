package org.xmc.common.stubs.cashaccount;

import com.querydsl.core.annotations.QueryProjection;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.xmc.common.stubs.DtoBank;

import java.io.Serializable;
import java.util.Currency;

public class DtoCashAccount implements Serializable {
    private Long id;
    private DtoBank bank;

    private String iban;
    private String number;
    private String name;
    private Currency currency;

    public DtoCashAccount() {
    }

    @QueryProjection
    public DtoCashAccount(
            Long id, String iban, String number, String name, String currency,
            Long bankId, String bankName, String bic, String blz, byte[] logo) {

        this.id = id;
        this.bank = new DtoBank(bankId, bankName, bic, blz, logo);
        this.iban = iban;
        this.number = number;
        this.name = name;
        this.currency = Currency.getInstance(currency);
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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
                .toString();
    }
}
