package org.xmc.be;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.BinaryData;
import org.xmc.be.entities.Category;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.entities.user.ServiceCallLog;
import org.xmc.be.entities.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        var user = new User();

        user.setUsername(userName);
        user.setDisplayName(userName);

        session().persist(user);

        return user;
    }

    public BinaryData createBinaryData() {
        return createBinaryData("some data".getBytes());
    }

    public BinaryData createBinaryData(Bank bank) {
        var binaryData = createBinaryData();

        bank.setLogo(binaryData);
        session().saveOrUpdate(bank);

        return binaryData;
    }

    public BinaryData createBinaryData(byte[] data) {
        var binaryData = new BinaryData();

        binaryData.setDescription(UUID.randomUUID().toString());
        binaryData.setHash(UUID.randomUUID().toString());
        binaryData.setRawData(data);
        binaryData.setSize(data.length);

        session().persist(binaryData);

        return binaryData;
    }

    public Bank createBank() {
        var bank = new Bank();

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
        var cashAccount = new CashAccount();

        cashAccount.setBank(bank);
        cashAccount.setCurrency(Currency.getInstance("EUR"));
        cashAccount.setIban("iban");
        cashAccount.setName("name");
        cashAccount.setNumber("number");

        session().persist(cashAccount);

        return cashAccount;
    }

    public ServiceCallLog createServiceCallLog() {
        var serviceCallLog = new ServiceCallLog();

        serviceCallLog.setCallDuration(100);
        serviceCallLog.setServiceClass("test");
        serviceCallLog.setServiceMethod("method");

        session().persist(serviceCallLog);

        return serviceCallLog;
    }

    public Category createCategory() {
        return createCategory(createBinaryData());
    }

    public Category createCategory(BinaryData icon) {
        var category = new Category();

        category.setName(UUID.randomUUID().toString());
        category.setIcon(icon);

        session().persist(category);

        return category;
    }

    public CashAccountTransaction createCashAccountTransaction() {
        return createCashAccountTransaction(createCashAccount());
    }

    public CashAccountTransaction createCashAccountTransaction(CashAccount cashAccount) {
        return createCashAccountTransaction(cashAccount, createCategory());
    }

    public CashAccountTransaction createCashAccountTransaction(CashAccount cashAccount, Category category) {
        return createCashAccountTransaction(cashAccount, category, createBank());
    }

    public CashAccountTransaction createCashAccountTransaction(CashAccount cashAccount, Category category, Bank bank) {
        var cashAccountTransaction = new CashAccountTransaction();

        cashAccountTransaction.setCashAccount(cashAccount);
        cashAccountTransaction.setCategory(category);
        cashAccountTransaction.setReferenceBank(bank);
        cashAccountTransaction.setSaldoAfter(new BigDecimal(0.0));
        cashAccountTransaction.setSaldoBefore(new BigDecimal(0.0));
        cashAccountTransaction.setUsage("test");
        cashAccountTransaction.setValue(new BigDecimal(0.0));
        cashAccountTransaction.setValutaDate(LocalDateTime.now());

        session().persist(cashAccountTransaction);

        cashAccount.getTransactions().add(cashAccountTransaction);
        session().saveOrUpdate(cashAccount);

        return cashAccountTransaction;
    }
}
