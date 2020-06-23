package org.xmc.be.services.currency;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

import java.util.Currency;
import java.util.List;

class CurrencyServiceTest extends JUnitTestBase {
    private CurrencyService service;

    @BeforeEach
    void setUp() {
        service = new CurrencyService();
    }

    @Test
    void loadAllCurrencies() {
        List<Currency> currencies = service.loadAllCurrencies();

        Assert.assertEquals(227, currencies.size());
        Assert.assertEquals(Currency.getInstance("ADP"), Iterables.getFirst(currencies, null));
        Assert.assertEquals(Currency.getInstance("ZWR"), Iterables.getLast(currencies, null));
        Assert.assertTrue(currencies.contains(Currency.getInstance("EUR")));
    }
}