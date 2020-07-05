package org.xmc.config;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.common.stubs.DtoBankInformation;

import java.util.Currency;
import java.util.List;

class BeanConfigTest extends IntegrationTest {
    @Autowired
    private List<DtoBankInformation> bankInformation;

    @Autowired
    private List<Currency> currencies;

    @Test
    void testBankInformation() {
        Assert.assertEquals(4892, bankInformation.size());
    }

    @Test
    void testCurrencies() {
        Assert.assertTrue(currencies.size() > 200);
        Assert.assertTrue(currencies.contains(Currency.getInstance("EUR")));
        Assert.assertTrue(currencies.contains(Currency.getInstance("USD")));
    }
}