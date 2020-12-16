package org.xmc.be.services.cashaccount.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

class CashAccountTransactionSaldoUpdaterTest extends IntegrationTest {
    @Autowired
    private CashAccountTransactionSaldoUpdater updater;

    @Test
    void testUpdateAll() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        CashAccountTransaction cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction1.setValue(BigDecimal.valueOf(100.0));
        session().saveOrUpdate(cashAccountTransaction1);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction1.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);

        Assertions.assertEquals(0.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(100.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        CashAccountTransaction cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setValue(BigDecimal.valueOf(25.0));
        session().saveOrUpdate(cashAccountTransaction2);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction2.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);
        session().refresh(cashAccountTransaction2);

        Assertions.assertEquals(0.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(100.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(100.0, cashAccountTransaction2.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(125.0, cashAccountTransaction2.getSaldoAfter().doubleValue(), 0);

        CashAccountTransaction cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.JULY, 10));
        cashAccountTransaction3.setValue(BigDecimal.valueOf(-1000.0));
        session().saveOrUpdate(cashAccountTransaction3);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction3.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);
        session().refresh(cashAccountTransaction2);
        session().refresh(cashAccountTransaction3);

        Assertions.assertEquals(0.0, cashAccountTransaction3.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(-1000.0, cashAccountTransaction3.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(-1000.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(-900.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(-900.0, cashAccountTransaction2.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(-875.0, cashAccountTransaction2.getSaldoAfter().doubleValue(), 0);
    }

    @Test
    void testUpdateAll_SameValutaDate() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        CashAccountTransaction cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction1.setValue(BigDecimal.valueOf(100.0));
        session().saveOrUpdate(cashAccountTransaction1);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction1.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);

        Assertions.assertEquals(0.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(100.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        CashAccountTransaction cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction2.setValue(BigDecimal.valueOf(25.0));
        session().saveOrUpdate(cashAccountTransaction2);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction2.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);
        session().refresh(cashAccountTransaction2);

        Assertions.assertEquals(0.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(100.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(100.0, cashAccountTransaction2.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(125.0, cashAccountTransaction2.getSaldoAfter().doubleValue(), 0);

        CashAccountTransaction cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction3.setValue(BigDecimal.valueOf(-1000.0));
        session().saveOrUpdate(cashAccountTransaction3);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction3.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);
        session().refresh(cashAccountTransaction2);
        session().refresh(cashAccountTransaction3);

        Assertions.assertEquals(0.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(100.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(100.0, cashAccountTransaction2.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(125.0, cashAccountTransaction2.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(125.0, cashAccountTransaction3.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(-875.0, cashAccountTransaction3.getSaldoAfter().doubleValue(), 0);

        CashAccountTransaction cashAccountTransaction4 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction4.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction4.setValue(BigDecimal.valueOf(1000.0));
        session().saveOrUpdate(cashAccountTransaction4);

        flush();
        updater.updateAll(cashAccount, cashAccountTransaction4.getValutaDate());
        flush();

        session().refresh(cashAccountTransaction1);
        session().refresh(cashAccountTransaction2);
        session().refresh(cashAccountTransaction3);
        session().refresh(cashAccountTransaction4);

        Assertions.assertEquals(0.0, cashAccountTransaction1.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(100.0, cashAccountTransaction1.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(100.0, cashAccountTransaction2.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(125.0, cashAccountTransaction2.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(125.0, cashAccountTransaction3.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(-875.0, cashAccountTransaction3.getSaldoAfter().doubleValue(), 0);

        Assertions.assertEquals(-875.0, cashAccountTransaction4.getSaldoBefore().doubleValue(), 0);
        Assertions.assertEquals(125.0, cashAccountTransaction4.getSaldoAfter().doubleValue(), 0);
    }
}