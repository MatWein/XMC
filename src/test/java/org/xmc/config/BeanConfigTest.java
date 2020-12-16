package org.xmc.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.IntegrationTest;
import org.xmc.common.stubs.bank.DtoBankInformation;

import java.util.Currency;
import java.util.List;

class BeanConfigTest extends IntegrationTest {
    @Autowired
    private List<DtoBankInformation> bankInformation;

    @Autowired
    private List<Currency> currencies;

    @Test
    void testBankInformation() {
        Assertions.assertEquals(4892, bankInformation.size());
    }

    @Test
    void testCurrencies() {
        Assertions.assertTrue(currencies.size() > 200);
        Assertions.assertTrue(currencies.contains(Currency.getInstance("EUR")));
        Assertions.assertTrue(currencies.contains(Currency.getInstance("USD")));
    }
}