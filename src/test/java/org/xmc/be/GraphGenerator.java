package org.xmc.be;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.BinaryData;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Currency;
import java.util.UUID;

@SuppressWarnings({"UnusedReturnValue", "unused"})
@Component
public class GraphGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    protected Session session() {
        return entityManager.unwrap(Session.class);
    }

    public User createUser() {
        return createUser(UUID.randomUUID().toString());
    }

    public User createUser(String userName) {
        User user = new User();

        user.setUsername(userName);
        user.setDisplayName(userName);

        session().persist(user);

        return user;
    }

    public BinaryData createBinaryData() {
        return createBinaryData("some data".getBytes());
    }

    public BinaryData createBinaryData(byte[] data) {
        BinaryData binaryData = new BinaryData();

        binaryData.setDescription(UUID.randomUUID().toString());
        binaryData.setHash(UUID.randomUUID().toString());
        binaryData.setRawData(data);
        binaryData.setSize(data.length);

        session().persist(binaryData);

        return binaryData;
    }

    public Bank createBank() {
        Bank bank = new Bank();

        bank.setBic("bic");
        bank.setBlz("blz");
        bank.setLogo(createBinaryData());
        bank.setName("name");

        session().persist(bank);

        return bank;
    }

    public CashAccount createCashAccount() {
        return createCashAccount(createBank());
    }

    public CashAccount createCashAccount(Bank bank) {
        CashAccount cashAccount = new CashAccount();

        cashAccount.setBank(bank);
        cashAccount.setCurrency(Currency.getInstance("EUR"));
        cashAccount.setIban("iban");
        cashAccount.setName("name");
        cashAccount.setNumber("number");

        session().persist(cashAccount);

        return cashAccount;
    }
}
