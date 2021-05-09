package io.github.matwein.xmc.be.services.cashaccount.mapper;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccount;

@Component
public class DtoCashAccountToCashAccountMapper {
    public CashAccount map(Bank bank, DtoCashAccount dtoCashAccount) {
        CashAccount cashAccount = new CashAccount();
        
        update(cashAccount, bank, dtoCashAccount);
        
        return cashAccount;
    }

    public void update(CashAccount cashAccount, Bank bank, DtoCashAccount dtoCashAccount) {
        cashAccount.setBank(bank);
        cashAccount.setCurrency(dtoCashAccount.getCurrency());
        cashAccount.setIban(dtoCashAccount.getIban());
        cashAccount.setName(dtoCashAccount.getName());
        cashAccount.setNumber(dtoCashAccount.getNumber());
	    cashAccount.setColor(dtoCashAccount.getColor());
    }
}
