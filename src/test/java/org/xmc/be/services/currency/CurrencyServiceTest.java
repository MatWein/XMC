package org.xmc.be.services.currency;

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

        Assert.assertTrue(currencies.size() > 200);
        Assert.assertTrue(currencies.contains(Currency.getInstance("EUR")));
        Assert.assertTrue(currencies.contains(Currency.getInstance("USD")));
    }
}