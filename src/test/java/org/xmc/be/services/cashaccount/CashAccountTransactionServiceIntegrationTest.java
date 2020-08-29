package org.xmc.be.services.cashaccount;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.be.entities.cashaccount.CashAccount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

class CashAccountTransactionServiceIntegrationTest extends IntegrationTest {
    @Autowired
    private CashAccountTransactionService service;

    @Test
    void testCalculateSaldoPreview() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction1.setValue(new BigDecimal(10.0));
        cashAccountTransaction1.setSaldoBefore(new BigDecimal(0.0));
        cashAccountTransaction1.setSaldoAfter(new BigDecimal(10.0));
        session().saveOrUpdate(cashAccountTransaction1);

        var cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setValue(new BigDecimal(20.0));
        cashAccountTransaction2.setSaldoBefore(new BigDecimal(10.0));
        cashAccountTransaction2.setSaldoAfter(new BigDecimal(30.0));
        session().saveOrUpdate(cashAccountTransaction2);

        flush();

        Pair<BigDecimal, BigDecimal> result = service.calculateSaldoPreview(cashAccount.getId(), null, LocalDate.of(2020, Month.AUGUST, 3), new BigDecimal(100.0));

        Assert.assertEquals(30.0, result.getLeft().doubleValue(), 0);
        Assert.assertEquals(130.0, result.getRight().doubleValue(), 0);
    }

    @Test
    void testCalculateSaldoPreview_ValutaOnSameDay() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction1.setValue(new BigDecimal(10.0));
        cashAccountTransaction1.setSaldoBefore(new BigDecimal(0.0));
        cashAccountTransaction1.setSaldoAfter(new BigDecimal(10.0));
        session().saveOrUpdate(cashAccountTransaction1);

        var cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setValue(new BigDecimal(20.0));
        cashAccountTransaction2.setSaldoBefore(new BigDecimal(10.0));
        cashAccountTransaction2.setSaldoAfter(new BigDecimal(30.0));
        session().saveOrUpdate(cashAccountTransaction2);

        var cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction3.setValue(new BigDecimal(20.0));
        cashAccountTransaction3.setSaldoBefore(new BigDecimal(30.0));
        cashAccountTransaction3.setSaldoAfter(new BigDecimal(50.0));
        session().saveOrUpdate(cashAccountTransaction3);

        flush();

        Pair<BigDecimal, BigDecimal> result = service.calculateSaldoPreview(cashAccount.getId(), null, LocalDate.of(2020, Month.AUGUST, 2), new BigDecimal(100.0));

        Assert.assertEquals(50.0, result.getLeft().doubleValue(), 0);
        Assert.assertEquals(150.0, result.getRight().doubleValue(), 0);
    }

    @Test
    void testCalculateSaldoPreview_ValutaOnSameDay_ExistingTransaction() {
        CashAccount cashAccount = graphGenerator.createCashAccount();

        var cashAccountTransaction1 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction1.setValutaDate(LocalDate.of(2020, Month.AUGUST, 1));
        cashAccountTransaction1.setValue(new BigDecimal(10.0));
        cashAccountTransaction1.setSaldoBefore(new BigDecimal(0.0));
        cashAccountTransaction1.setSaldoAfter(new BigDecimal(10.0));
        session().saveOrUpdate(cashAccountTransaction1);

        var cashAccountTransaction2 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction2.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction2.setValue(new BigDecimal(20.0));
        cashAccountTransaction2.setSaldoBefore(new BigDecimal(10.0));
        cashAccountTransaction2.setSaldoAfter(new BigDecimal(30.0));
        session().saveOrUpdate(cashAccountTransaction2);

        var cashAccountTransaction3 = graphGenerator.createCashAccountTransaction(cashAccount);
        cashAccountTransaction3.setValutaDate(LocalDate.of(2020, Month.AUGUST, 2));
        cashAccountTransaction3.setValue(new BigDecimal(20.0));
        cashAccountTransaction3.setSaldoBefore(new BigDecimal(30.0));
        cashAccountTransaction3.setSaldoAfter(new BigDecimal(50.0));
        session().saveOrUpdate(cashAccountTransaction3);

        flush();

        Pair<BigDecimal, BigDecimal> result = service.calculateSaldoPreview(
                cashAccount.getId(),
                cashAccountTransaction3.getId(),
                LocalDate.of(2020, Month.AUGUST, 2),
                new BigDecimal(100.0));

        Assert.assertEquals(30.0, result.getLeft().doubleValue(), 0);
        Assert.assertEquals(130.0, result.getRight().doubleValue(), 0);
    }
}