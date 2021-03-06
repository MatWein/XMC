package io.github.matwein.xmc.be.services.cashaccount.mapper;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DtoCashAccountTransactionToCashAccountTransactionMapper {
    public CashAccountTransaction map(CashAccount cashAccount, Category category, DtoCashAccountTransaction dtoTransaction) {
        var cashAccountTransaction = new CashAccountTransaction();

        cashAccountTransaction.setCashAccount(cashAccount);
        cashAccountTransaction.setSaldoBefore(BigDecimal.valueOf(0.0));
        cashAccountTransaction.setSaldoAfter(BigDecimal.valueOf(0.0));

        update(cashAccountTransaction, category, dtoTransaction);

        return cashAccountTransaction;
    }

    public void update(CashAccountTransaction cashAccountTransaction, Category category, DtoCashAccountTransaction dtoTransaction) {
        cashAccountTransaction.setCategory(category);
        cashAccountTransaction.setCreditorIdentifier(dtoTransaction.getCreditorIdentifier());
        cashAccountTransaction.setDescription(dtoTransaction.getDescription());
        cashAccountTransaction.setMandate(dtoTransaction.getMandate());
        cashAccountTransaction.setReference(dtoTransaction.getReference());
        cashAccountTransaction.setReferenceBank(dtoTransaction.getReferenceBank());
        cashAccountTransaction.setReferenceIban(dtoTransaction.getReferenceIban());
        cashAccountTransaction.setUsage(dtoTransaction.getUsage());
        cashAccountTransaction.setValue(dtoTransaction.getValue());
        cashAccountTransaction.setValutaDate(dtoTransaction.getValutaDate());
    }
}
